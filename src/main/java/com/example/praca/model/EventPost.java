package com.example.praca.model;

import com.example.praca.dto.eventpost.CreateEventPostDto;
import com.example.praca.dto.eventpost.EventPostInformationDto;
import com.example.praca.dto.eventpost.UpdatePostDto;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * @author Daniel Lezniak
 */

@Data
@Entity
@Table(name = "event_post")
public class EventPost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 3000)
    private String content;

    @CreatedBy
    private String createdBy;
    @CreationTimestamp
    @Column(updatable = false)
    private Date createdAt;

    @UpdateTimestamp
    private Date updatedAt;
    @ManyToOne
    @JoinColumn(name="event_id", nullable=false)
    private Event event;

    @ManyToOne
    @JoinColumn(name="user_id", nullable=false)
    private User user;

    public static EventPost of(CreateEventPostDto createEventPostDto, Event event, User user) {
        EventPost eventPost = new EventPost();
        eventPost.setEvent(event);
        eventPost.setContent(createEventPostDto.getContent());
        eventPost.setCreatedBy(user.getName());
        eventPost.setUser(user);
        return eventPost;
    }

    public static EventPost update(UpdatePostDto dto, EventPost eventPost) {
        eventPost.setContent(dto.getContent());
         return eventPost;
    }
}
