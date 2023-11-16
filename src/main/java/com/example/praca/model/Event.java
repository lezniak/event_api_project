package com.example.praca.model;

import com.example.praca.dto.event.CreateEventDto;
import com.example.praca.dto.event.InformationEventDto;
import com.example.praca.dto.event.UpdateEventDto;
import com.google.api.client.util.DateTime;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import net.minidev.json.annotate.JsonIgnore;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.CrossOrigin;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;
import java.util.Set;

/**
 * @author Daniel Lezniak
 */
@Data
@Entity
@Table(name = "event")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column
    private String eventDescription;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private Timestamp startDate;

    @Column
    private Integer maxMembers;

    @Column
    private boolean history;

    @Column
    private boolean forAll;

    @ManyToOne
    @JoinColumn(name="owner_id", nullable=false)
    private User user;

//    @EqualsAndHashCode.Exclude
//    @ToString.Exclude
//    @ManyToMany(cascade = {CascadeType.DETACH,CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REFRESH})
//    @JoinTable(
//            name = "events_types",
//            joinColumns = @JoinColumn(name = "event_id"),
//            inverseJoinColumns = @JoinColumn(name = "event_type_id")
//    )
//    Set<EventType> event_types;
    @EqualsAndHashCode.Exclude
    @ManyToOne
    @JoinColumn(name = "event_type_id", nullable = false)
    private EventType eventType;

    @EqualsAndHashCode.Exclude
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id", referencedColumnName = "id")
    private EventAddress event_address;

    @OneToMany(mappedBy="event")
    private List<EventPost> posts;

    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "event")
    private List<Organization> organizations;

    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "event")
    private List<Ticket> eventTicket;

    public static Event of(CreateEventDto dto, EventAddress eventAddress) {
        Event event = new Event();

        event.setName(dto.getName());
        event.setEventDescription(dto.getEventDescription());
        event.setStartDate(dto.getStartDateD());
        event.setMaxMembers(dto.getMaxMembers());
        event.setUser(dto.getOwner());
        event.setEvent_address(eventAddress);
        event.setEventType(dto.getEventTypeM());
        event.setEvent_address(eventAddress);
        event.setForAll(dto.isForAll());
        return event;
    }

    public static Event updateEvent(Event event, UpdateEventDto dto) {
        event.setName(dto.getCreateEventDto().getName());
        event.setEventDescription(dto.getCreateEventDto().getEventDescription());
        event.setStartDate(dto.getCreateEventDto().getStartDateD());
        event.setMaxMembers(dto.getCreateEventDto().getMaxMembers());
        event.setEvent_address(dto.getCreateEventDto().getEventAddressM());
        event.setForAll(dto.getCreateEventDto().isForAll());
        return event;
    }

    @Override
    public String toString() {
        return "Event{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", eventDescription='" + eventDescription + '\'' +
                ", startDate=" + startDate +
                ", maxMembers=" + maxMembers +
                ", history=" + history +
                ", user=" + user +
                '}';
    }
}
