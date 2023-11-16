package com.example.praca.service;

import com.example.praca.dto.filter.EventFilterParam;
import com.example.praca.dto.common.PageableDto;
import com.example.praca.dto.event.EventMemberInformation;
import com.example.praca.dto.hobby.AcceptEventMemberDto;
import com.example.praca.exception.EventFinishedException;
import com.example.praca.exception.EventMembersNotFoundException;
import com.example.praca.exception.InvalidEventOwnerException;
import com.example.praca.exception.UserNotFoundException;
import com.example.praca.model.Event;
import com.example.praca.model.EventMembers;
import com.example.praca.model.User;
import com.example.praca.repository.EventMembersRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Daniel Lezniak
 */

@Service
@AllArgsConstructor
@Slf4j
public class EventMemberService {
    private final EventService EVENT_SERVICE;
    private final UserService USER_SERVICE;
    private final EventMembersRepository EVENT_MEMBERS_REPOSITORY;
    public ReturnService joinEvent(String email, Long eventId) {
        Event event = EVENT_SERVICE.findEventById(eventId);
        User user = USER_SERVICE.findUserByEmail(email);
        USER_SERVICE.userAlreadyJoin(user.getId(), eventId);
        if (eventFinished(event.getId()))
            throw new EventFinishedException();


        log.info("Join event: user_id: {}, event_id: {}", user.getId(), event.getId());
        try {
            EVENT_MEMBERS_REPOSITORY.save(EventMembers.of(user, event));
            return ReturnService.returnInformation("Succ. join", 1);
        } catch (Exception ex) {
            return ReturnService.returnError("Err. join event: " + ex.getMessage(), -1);
        }
    }

    public ReturnService leaveEvent(String email, Long eventId) {
        User user = USER_SERVICE.findUserByEmail(email);
        Event event = EVENT_SERVICE.findEventById(eventId);
        USER_SERVICE.userInEvent(user.getId(), eventId);
        log.info("Leave event: user_id: {}, event_id: {}", user.getId(), eventId);

        try {
            EVENT_MEMBERS_REPOSITORY.delete(EVENT_MEMBERS_REPOSITORY.findByUserIdAndEventId(user.getId(), eventId).get());
            return ReturnService.returnInformation("Succ. leave event", 1);
        } catch (Exception ex) {
            return ReturnService.returnError("Err. leave event: " + ex.getMessage(), -1);
        }

    }

    public ReturnService getAllEventMembers(int pageNo, int pageSize, String sortBy, String sortDir, EventFilterParam filterParam, String... value) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<EventMembers> eventMembersPage = null;
        Event event;
        switch (filterParam) {
            case EM :
                 event = EVENT_SERVICE.findEventById(Long.valueOf(value[0]));
                 Long userId = USER_SERVICE.getUserIdByEmail(USER_SERVICE.getUserEmailFromJwt());
                USER_SERVICE.userInEvent(userId, event.getId());
                eventMembersPage = EVENT_MEMBERS_REPOSITORY.findEventMembersByEventId(event.getId(), pageable);
            break;

            case TA:
                 event = EVENT_SERVICE.findEventById(Long.valueOf(value[0]));
                if (!isEventOwner(event.getUser().getId()))
                    throw new InvalidEventOwnerException();
                eventMembersPage = EVENT_MEMBERS_REPOSITORY.findEventMembersByEventIdToAccepted(event.getId(), pageable);
                break;
            case ACCEPTED:
                event = EVENT_SERVICE.findEventById(Long.valueOf(value[0]));
                if (!isEventOwner(event.getUser().getId()))
                    throw new InvalidEventOwnerException();
                eventMembersPage = EVENT_MEMBERS_REPOSITORY.getAllAccepted(event.getId(), pageable);
                break;

        }
        if (eventMembersPage == null)
            throw new EventMembersNotFoundException();

        List<EventMembers> eventMembersList = eventMembersPage.getContent();
        List<EventMemberInformation> eventMemberInformationsList = eventMembersList.stream()
                .map(x -> EventMemberInformation.of(USER_SERVICE.findUserById(x.getUser_id()), x)).collect(Collectors.toList());

        return ReturnService.returnInformation("", PageableDto.of(eventMemberInformationsList, eventMembersPage),1);
    }

    private boolean eventFinished(Long eventId) {
        Event event = EVENT_SERVICE.findEventById(eventId);
        return ServiceFunctions.dateBefore(event.getStartDate());
    }

    public ReturnService acceptUser(String ownerEmail, AcceptEventMemberDto acceptEventMemberDto) {
        Event event = EVENT_SERVICE.findEventById(acceptEventMemberDto.getEventId());
        if (!EVENT_SERVICE.isEventOwner(event.getUser().getId()))
            throw new InvalidEventOwnerException();

        EventMembers eventMembers = EVENT_MEMBERS_REPOSITORY.findByUserIdAndEventId(acceptEventMemberDto.getUserId(), acceptEventMemberDto.getEventId())
                .orElseThrow(() -> new UserNotFoundException());

        try {
            eventMembers.setAccepted(true);
            EVENT_MEMBERS_REPOSITORY.save(eventMembers);
            return ReturnService.returnInformation("Succ. accept user",1);
        } catch (Exception ex) {
            log.error("Err: accept user %s to event %s", acceptEventMemberDto.getUserId(), acceptEventMemberDto.getEventId());
            return ReturnService.returnError("Err. accept user " + ex.getMessage(), -1);
        }
    }

    public ReturnService deleteMember(String ownerEmail, AcceptEventMemberDto acceptEventMemberDto) {
        Event event = EVENT_SERVICE.findEventById(acceptEventMemberDto.getEventId());
        if (!EVENT_SERVICE.isEventOwner(event.getUser().getId()))
            throw new InvalidEventOwnerException();
        EventMembers eventMembers = EVENT_MEMBERS_REPOSITORY.findByUserIdAndEventId(acceptEventMemberDto.getUserId(), acceptEventMemberDto.getEventId())
                .orElseThrow(() -> new UserNotFoundException());

        try {
            EVENT_MEMBERS_REPOSITORY.delete(eventMembers);
            return ReturnService.returnInformation("Succ. delete user",1);
        } catch (Exception ex) {
            log.error("Err: delte user user %s to event %s", acceptEventMemberDto.getUserId(), acceptEventMemberDto.getEventId());
            return ReturnService.returnError("Err. delete user " + ex.getMessage(), -1);
        }

    }

    public ReturnService userInformation(Long eventId, Long userId) {
        boolean userInEvent = EVENT_MEMBERS_REPOSITORY.findByUserIdAndEventId(userId, eventId).isPresent();
        return null;
    }

    public boolean isEventOwner(Long eventOwnerId) {
        Long ownerId = USER_SERVICE.getUserIdByEmail(USER_SERVICE.getUserEmailFromJwt());

        return ownerId == eventOwnerId;
    }

    public EventMembers getEventMemberByUserIdAndEventId(Long userId, Long eventId) {
        return EVENT_MEMBERS_REPOSITORY.findByUserIdAndEventId(userId, eventId).orElseThrow(() -> new EventMembersNotFoundException());
    }

}
