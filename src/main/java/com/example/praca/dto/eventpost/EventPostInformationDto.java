package com.example.praca.dto.eventpost;

import com.example.praca.model.EventPost;
import lombok.Data;

import java.util.Date;


/**
 * @author Daniel Lezniak
 */
@Data
public class EventPostInformationDto {
    private String content;
    private String creator;
    private String eventName;
    private Long eventId;
    private Date createdAt;

    public static EventPostInformationDto of(EventPost eventPost) {
        EventPostInformationDto dto = new EventPostInformationDto();
        dto.setContent(eventPost.getContent());
        dto.setCreator(eventPost.getCreatedBy());
        dto.setEventName(eventPost.getEvent().getName());
        dto.setEventId(eventPost.getEvent().getId());
        dto.setCreatedAt(eventPost.getCreatedAt());

        return dto;
    }
}
