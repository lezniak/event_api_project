package com.example.praca.service;


import com.example.praca.dto.common.PageableDto;
import com.example.praca.dto.event.CreateEventDto;
import com.example.praca.dto.event.InformationEventDto;
import com.example.praca.dto.event.QrCodeDto;
import com.example.praca.dto.event.UpdateEventDto;
import com.example.praca.dto.filter.EventFilterParam;
import com.example.praca.dto.newsletter.EventBasicInformationDto;
import com.example.praca.exception.EventNotFoundException;
import com.example.praca.exception.EventTypeNotFoundException;
import com.example.praca.exception.InvalidEventOwnerException;
import com.example.praca.model.*;
import com.example.praca.repository.*;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.nimbusds.oauth2.sdk.util.StringUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.impl.DefaultClaims;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.apache.bcel.generic.RET;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import javax.swing.text.html.Option;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Daniel Lezniak
 */
@Service
@AllArgsConstructor
@Slf4j
public class EventService {
    private final EventRepository EVENT_REPOSITORY;
    private final UserRepository USER_REPOSITORY;
    private final EventTypeRepository EVENT_TYPE_REPOSITORY;
    private final EventAddressService EVENT_ADDRESS_SERVICE;
    private final EventAddressRepository EVENT_ADDRESS_REPOSITORY;
    private final EventMembersRepository EVENT_MEMBERS_REPOSITORY;
    private final EventTypeService EVENT_TYPE_SERVICE;
    private final UserService USER_SERVICE;
    @Lazy
    private final QrCodeService QR_CODE_SERVICE;

    Map<String, String> validationError;
    private final QrCodeEventRepository QR_CODE_REPOSITORY;

    private final String qrMessage = "eventId=%s";

    public ReturnService createEvent(CreateEventDto dto) {
        Long ownerId = USER_SERVICE.getUserIdByEmail(USER_SERVICE.getUserEmailFromJwt());
        dto.setOwnerId(ownerId);
        log.info("create dto" + dto);
        clearError();
        validationError = ValidationService.createEventValidator(dto);
        if (!validationError.isEmpty())
            return ReturnService.returnError("error", validationError, dto, 0);
        clearError();
        validationError = ValidationService.validateEventAddress(dto.getEventAddress());
        if (!validationError.isEmpty())
            return ReturnService.returnError("error", validationError, dto, 0);

        log.info("create dto" + dto);
        dto.setEventTypeM(EVENT_TYPE_SERVICE.findById(dto.getEventType()));
        dto.setOwner(USER_SERVICE.findUserById(dto.getOwnerId()));

        dto.setStartDateD(ServiceFunctions.parseTimestamp(dto.getStartDate()));
        try {
            log.info("create dto" + dto);
            EventAddress eventAddress = EVENT_ADDRESS_REPOSITORY.save(EventAddress.of(dto.getEventAddress()));
            Event event = EVENT_REPOSITORY.save(Event.of(dto, eventAddress));
            if (dto.isGenarateQrCode())
                generateEventQrCode(event.getId());
            return ReturnService.returnInformation("Succ. create event", InformationEventDto.of(event, eventAddress, true), 1);
        } catch (Exception ex) {
            log.error("Błąd podczas tworzenia wydarzenia dla userId: %s", dto.getOwnerId());
            return ReturnService.returnError("Err. create event: " + ex.getMessage(), -1);
        }

    }

    public ReturnService updateEvent(UpdateEventDto dto) {
        clearError();
        Event eventDb = findEventById(dto.getEventId());
        if (!isEventOwner(eventDb.getUser().getId()))
            throw new InvalidEventOwnerException();

        validationError = ValidationService.updateEventValidator(dto);
        if (!validationError.isEmpty())
            return ReturnService.returnError("error", validationError, dto, 0);

        ReturnService updateEventAddressRet = EVENT_ADDRESS_SERVICE.updateEventAddress(eventDb, dto.getCreateEventDto().getEventAddress());
        if (updateEventAddressRet.getStatus() != 1)
            return ReturnService.returnError(updateEventAddressRet.getMessage(), -1);

        dto.getCreateEventDto().setStartDateD(ServiceFunctions.parseTimestamp(dto.getCreateEventDto().getStartDate()));
        dto.getCreateEventDto().setEventAddressM((EventAddress) updateEventAddressRet.getValue());
        try {
            Event updatedEvent = EVENT_REPOSITORY.save(Event.updateEvent(eventDb, dto));
            InformationEventDto informationEventDto = InformationEventDto.of(updatedEvent, (EventAddress) updateEventAddressRet.getValue(), true);
            return ReturnService.returnInformation("Succ. update event", informationEventDto, 1);
        } catch (Exception ex) {
            log.error("Błąd podczas aktualizaowania wydarzenia dla userId: %s, wydarzenie id: %s",dto.getCreateEventDto().getOwnerId(), eventDb.getId());
            return ReturnService.returnError("Err. update event: " + ex.getMessage(), -1);
        }

    }

    public ReturnService deleteEventById(Long eventId) {
        clearError();
        Event event = findEventById(eventId);
        if (!isEventOwner(event.getUser().getId()))
            throw new InvalidEventOwnerException();

        try {
            EVENT_REPOSITORY.delete(event);
            EVENT_MEMBERS_REPOSITORY.deleteByEventId(eventId);
            return ReturnService.returnInformation("Succ. delete event", 1);
        } catch (Exception ex) {
            log.error("Błąd podczas usuwania wydarzenia eventId: %s", eventId);
            return ReturnService.returnError("Err. delete event: " + ex.getMessage(), -1);
        }
    }

    public ReturnService getUserEvents() {
        String userEmail = USER_SERVICE.getUserEmailFromJwt();
        User user = USER_SERVICE.findUserByEmail(userEmail);

        List<Event> eventList = EVENT_REPOSITORY.findAllByUser(user);

        List<InformationEventDto> informationEventDtoList = eventList.stream()
                .filter(x -> !x.isHistory())
                .map(event -> InformationEventDto.of(event, event.getEvent_address(), true))
                .collect(Collectors.toList());

        if (informationEventDtoList.isEmpty())
            throw new EventNotFoundException();
        return ReturnService.returnInformation("Events", informationEventDtoList, 1);
    }

    public ReturnService getUserHistoryEvents() {
        String userEmail = USER_SERVICE.getUserEmailFromJwt();
        User user = USER_SERVICE.findUserByEmail(userEmail);

        List<Event> eventList = EVENT_REPOSITORY.findAllByUser(user);

        List<InformationEventDto> informationEventDtoList = eventList.stream()
                .filter(Event::isHistory)
                .map(event -> InformationEventDto.of(event, event.getEvent_address(), true))
                .collect(Collectors.toList());

        if (informationEventDtoList.isEmpty())
            throw new EventNotFoundException();
        return ReturnService.returnInformation("Events", informationEventDtoList, 1);
    }

    public ReturnService getAllEvents(int pageNo, int pageSize, String sortBy, String sortDir, EventFilterParam filterParam, String... value) {

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<Event> eventPage = null;
        switch (filterParam) {
            case A :
                eventPage = EVENT_REPOSITORY.findAllEvents(pageable);
                break;
            case C :
                eventPage = EVENT_REPOSITORY.findAllByCity(value[0], pageable);
                break;
            case D :
                String dateTimestamp = value[0] + " 00:00";
                String dateTimestampEnd = value[0] + " 23:59";
                eventPage = EVENT_REPOSITORY.findAllByDate(ServiceFunctions.parseTimestamp(dateTimestamp), ServiceFunctions.parseTimestamp(dateTimestampEnd), pageable);
                break;
            case DD :
                String dateTimestampStart = value[0] + " 00:00";
                String dateTimestampE = value[1] + " 23:59";
                eventPage = EVENT_REPOSITORY.findAllByDateRante(ServiceFunctions.parseTimestamp(dateTimestampStart), ServiceFunctions.parseTimestamp(dateTimestampE), pageable);
                break;
            case O :
                Long ownerId = Long.valueOf(value[0]);
                User owner = USER_SERVICE.findUserById(ownerId);
                eventPage = EVENT_REPOSITORY.findAllByOwner(owner, pageable);
                break;
            case T :
                List<Long> eventTypeList = new ArrayList<>();
                String[] eventNames = ServiceFunctions.parseToArray(value[0]);
                for (String name : eventNames) {
                    EventType eventType = findByName(name);
                    eventTypeList.add(eventType.getId());
                }

                eventPage = EVENT_REPOSITORY.findAllByType(eventTypeList, pageable);
                break;
            case P :
                User user = USER_SERVICE.findUserByEmail(value[0]);
                List<Long> eventsIdList = EVENT_MEMBERS_REPOSITORY.findAllByUserId(user.getId())
                        .stream()
                        .map(event -> event.getEvent_id())
                        .collect(Collectors.toList());

                eventPage = EVENT_REPOSITORY.findAllById(eventsIdList, pageable);
            break;
            case DDT:
                String dateStart = value[0] + " 00:00";
                String dateEnd = value[1] + " 23:59";
                Long eventTypeId = Long.valueOf(value[2]);
                eventPage = EVENT_REPOSITORY.findAllByEventTypeAndDateRange(eventTypeId, ServiceFunctions.parseTimestamp(dateStart), ServiceFunctions.parseTimestamp(dateEnd), pageable);
                break;

            case SCH:
                String dateStartS = value[0] + " 00:00";
                String dateEndS = value[1] + " 23:59";
                Long eventTypeIdS = Long.valueOf(value[2]);
                eventPage = EVENT_REPOSITORY.findAllByEventTypeAndDateRangeScheduler(eventTypeIdS, ServiceFunctions.parseTimestamp(dateStartS), ServiceFunctions.parseTimestamp(dateEndS), pageable);
                List<Event> eventListScheduler = eventPage.getContent();
                List<EventBasicInformationDto> eventBasicInformationDtos = eventListScheduler.stream()
                        .map(x -> EventBasicInformationDto.of(x))
                        .collect(Collectors.toList());
                return ReturnService.returnInformation("", PageableDto.of(eventBasicInformationDtos, eventPage),1);
        }

        if (eventPage == null)
            throw new EventNotFoundException();
        List<Event> eventList = eventPage.getContent();

        List<InformationEventDto> informationEventDtoList = eventList.stream()
                .map(x -> InformationEventDto.of(x,x.getEvent_address(), false))
                .collect(Collectors.toList());

        return ReturnService.returnInformation("", PageableDto.of(informationEventDtoList, eventPage),1);
    }

    public Event findEventById(Long id) {
        return EVENT_REPOSITORY.findById(id).orElseThrow(() -> new EventNotFoundException());
    }

    private void clearError() {
        validationError.clear();
    }

    public ReturnService getEventById(Long eventId) {
        Long userId = null;
        Optional<Event> optionalEvent = EVENT_REPOSITORY.findById(eventId);
        if (optionalEvent.isEmpty())
            throw new EventNotFoundException();
        Optional<EventAddress> eventAddressOptional = EVENT_ADDRESS_REPOSITORY.findAllByEvent(optionalEvent.get());

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        if (email != null && !email.equals("anonymousUser"))
             userId = USER_SERVICE.getUserIdByEmail(email);

        return ReturnService.returnInformation("", InformationEventDto.of(optionalEvent.get(), eventAddressOptional.get(), isMemberOf(optionalEvent.get().getId(), userId)), 1);
    }

    public boolean isEventOwner(Long eventOwnerId) {
        String token = JwtTokenService.getJwtokenFromHeader();
        Claims claims = new DefaultClaims();

        if (StringUtils.isNotBlank(token) && !token.isEmpty())
            claims = JwtTokenService.getClaimsFromJwtoken(token);

        Optional<User> user = USER_REPOSITORY.findByEmail(claims.get("sub").toString());
        if (user.isEmpty())
            return false;

        return eventOwnerId == user.get().getId();
    }

    private EventType findByName(String name) {
        return EVENT_TYPE_REPOSITORY.findByName(name).orElseThrow(EventTypeNotFoundException::new);
    }

    public void deleteAllById(List<Long> eventIds) {
        EVENT_REPOSITORY.deleteAllById(eventIds);
    }

    public ReturnService moveToHistory(List<Long> eventIds) {
        for (Long id : eventIds) {
            Event event = (Event) this.getEventById(id).getValue();
            event.setHistory(true);
            try {
                EVENT_REPOSITORY.save(event);
            } catch (Exception ex) {
                log.error("Błąd podczas przenoszenia wydarzenia do historii dla eventId: %s" ,id);
                return ReturnService.returnError("Err update event history: " + ex.getMessage(), -1);
            }
        }
        return ReturnService.returnInformation("Succ. move events to history:  ", 1);
    }

    public ReturnService getAllEventsInRange(Double userLat, Double userLng, Double range) {
        List<Event> eventList= EVENT_REPOSITORY.findAllInRange(userLat, userLng,range);
        if (eventList.isEmpty())
            throw new EventNotFoundException();

        List<InformationEventDto> eventDtoList = eventList.stream()
                .map(x -> InformationEventDto.of(x, EVENT_ADDRESS_SERVICE.findByEvent(x), false))
                .collect(Collectors.toList());

        return ReturnService.returnInformation("", eventDtoList, 1);
    }

    public void generateEventQrCode(Long eventId) {
        User user = USER_SERVICE.findUserByEmail(USER_SERVICE.getUserEmailFromJwt());
        if (!isEventOwner(user.getId()))
            throw new InvalidEventOwnerException();

        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        try {
            BitMatrix bitMatrix = qrCodeWriter.encode(createQrEventMessage(eventId.toString()), BarcodeFormat.QR_CODE, 250, 250);
            //save into db
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", bos);
            saveQrCodeInDb(bos.toByteArray(), user, eventId);

        } catch (WriterException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String createQrEventMessage(String message) {
        return String.format(qrMessage, message);
    }

    private void saveQrCodeInDb(byte[] image, User user, Long eventId) {
        QrCode qrCode = new QrCode();
        qrCode.setContent(image);
        qrCode.setUser(user);
        qrCode.setEventId(eventId);

        QR_CODE_REPOSITORY.save(qrCode);
    }

    public byte[] getQrCodeEvent(Long eventId) {
        User user = USER_SERVICE.findUserByEmail(USER_SERVICE.getUserEmailFromJwt());
        if (!isEventOwner(user.getId()))
            throw new InvalidEventOwnerException();
        QrCode qrCode = QR_CODE_REPOSITORY.findByEventId(eventId).orElseThrow(() -> new EventNotFoundException());
//        return ReturnService.returnInformation("", QrCodeDto.builder().active(true).qrCodeContent(qrCode.getContent()).build(), 1);
        return qrCode.getContent();
    }

    public boolean isMemberOf(Long eventId, Long userId) {
        if (userId == null)
            return false;
        Optional<EventMembers> eventMembers = EVENT_MEMBERS_REPOSITORY.findByUserIdAndEventId(userId, eventId);
        return eventMembers.isPresent();
    }

    public void canUserModifyEvent(Long eventId) {
        String email = USER_SERVICE.getUserEmailFromJwt();
        User user = USER_SERVICE.findUserByEmail(email);
        if (!isEventOwner(eventId))
            throw new InvalidEventOwnerException();
    }

    public Long getEventOwnerId(Long eventId) {
        return EVENT_REPOSITORY.getEventOwnerByEventId(eventId);
    }

}
