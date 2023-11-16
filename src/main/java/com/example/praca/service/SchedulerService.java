package com.example.praca.service;

import com.example.praca.dto.common.PageableDto;
import com.example.praca.dto.event.InformationEventDto;
import com.example.praca.dto.eventtype.EventTypeInformationDto;
import com.example.praca.dto.filter.EventFilterParam;
import com.example.praca.dto.filter.UserFilterParam;
import com.example.praca.dto.newsletter.EventBasicInformationDto;
import com.example.praca.dto.user.InformationUserDto;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;


/**
 * @author Daniel Lezniak
 */
@Service
@AllArgsConstructor
public class SchedulerService {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private final EventService EVENT_SERVICE;

    private final UserService USER_SERVICE;
    private final EventTypeService EVENT_TYPE_SERVICE;

    private final EmailService EMAIL_SERVICE;

    private final HobbyService HOBBY_SERVICE;
//    @Scheduled(cron="*/5 * * * * ?")
    private void finishedEvents() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime past = now.minusDays(7);

        List<Long> eventsIdList = getEventsIdByDateRange(now, past);
        try {
            EVENT_SERVICE.deleteAllById(eventsIdList);
        } catch (Exception ex)  {
            ReturnService.returnError("Delete events scheduler ex: " + ex.getMessage(), -1);
        }
    }

    //Codziennie o 24
    @Scheduled(cron="0 0 0 * * *")
    private void eventHistory() {
           LocalDateTime now = LocalDateTime.now();
           LocalDateTime past = now.minusDays(1);

           List<Long> eventsIdList = getEventsIdByDateRange(now, past);
           try {
               EVENT_SERVICE.moveToHistory(eventsIdList);
           } catch (Exception ex) {
               ReturnService.returnError("Move to history events: " + ex.getMessage(), -1);
           }
    }

    @Scheduled(cron="0 0 0 * * *")
    private void newEventsNewsletter() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime future = now.plusDays(3);
        List<EventTypeInformationDto> eventTypeInformationDtos = (List<EventTypeInformationDto>) EVENT_TYPE_SERVICE.getAllEventTypesInformation().getValue();

        //get events by type
        for (EventTypeInformationDto eventTypeInformationDto : eventTypeInformationDtos) {
            ReturnService eventReturn = EVENT_SERVICE.getAllEvents(0, 9999, "id", "dsc", EventFilterParam.SCH,
                    new String[] {now.format(DATE_TIME_FORMATTER).toString(), future.format(DATE_TIME_FORMATTER).toString(), eventTypeInformationDto.getId().toString()});
            PageableDto eventPageableDto = (PageableDto) eventReturn.getValue();
            List<EventBasicInformationDto> eventDtoList = (List<EventBasicInformationDto>) eventPageableDto.getObjectList();

            //Get hobbyId by name
            Long hobbyId = HOBBY_SERVICE.getHobbyIdByName(eventTypeInformationDto.getName());
            //Get all users by hobby id and newsletter = true
            ReturnService usersReturn = USER_SERVICE.getAllUsers(0,9999, "id", "dsc", UserFilterParam.AHN, new String[] {hobbyId.toString(), "true"});
            PageableDto userPageableDto = (PageableDto) usersReturn.getValue();
            List<InformationUserDto> userDtoList = (List<InformationUserDto>) userPageableDto.getObjectList();

            //Sending mail to users with event information
             EMAIL_SERVICE.sendNewEventsNewsletter(eventTypeInformationDto, eventDtoList, userDtoList);

        }
    }

    private List<Long> getEventsIdByDateRange(LocalDateTime now, LocalDateTime past) {

        ReturnService eventsRet = EVENT_SERVICE.getAllEvents(0,9999,"id","dsc",EventFilterParam.DD,new String[] {past.format(DATE_TIME_FORMATTER).toString(), now.format(DATE_TIME_FORMATTER).toString()});
        PageableDto pageableDto = (PageableDto) eventsRet.getValue();

        List<InformationEventDto> eventDtoList = (List<InformationEventDto>)pageableDto.getObjectList();

        return eventDtoList.stream()
               .map(x -> x.getId())
               .collect(Collectors.toList());
    }

}
