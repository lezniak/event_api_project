package com.example.praca.model;

import lombok.Data;
import org.hibernate.annotations.GeneratorType;

import javax.persistence.*;

/**
 * @author Daniel Lezniak
 */
@Data
@Entity
@Table(name = "event_members")
public class EventMembers {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Long user_id;

    @Column
    private Long event_id;

    @Column
    private boolean accepted;

    public static EventMembers of(User user, Event event) {
        EventMembers dto = new EventMembers();
        dto.setEvent_id(event.getId());
        dto.setUser_id(user.getId());
        if (event.isForAll())
            dto.setAccepted(true);
        return dto;
    }


}
