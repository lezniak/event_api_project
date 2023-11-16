package com.example.praca.dto.event;

import com.example.praca.model.Event;
import com.example.praca.model.EventMembers;
import com.example.praca.model.Hobby;
import com.example.praca.model.User;
import lombok.Data;

import java.util.List;

/**
 * @author Daniel Lezniak
 */
@Data
public class EventMemberInformation {
    private Long userId;
    private String name;
    private List<Hobby> hobbyList;
    private boolean accepted;

    public static EventMemberInformation of(User user, EventMembers eventMembers) {
        EventMemberInformation dto = new EventMemberInformation();

        dto.setUserId(user.getId());
        dto.setName(user.getName());
        dto.setHobbyList(user.getHobbies());
        dto.setAccepted(eventMembers.isAccepted());

        return dto;

    }
}
