package com.example.praca.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

/**
 * @author Daniel Lezniak
 */
@Data
@Entity
@Table(name = "event_type")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

//    @EqualsAndHashCode.Exclude
//    @ToString.Exclude
//    @ManyToMany(mappedBy = "event_types", fetch = FetchType.EAGER)
//    List<Event> events;

    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "eventType")
    private List<Event> events;
    @Override
    public String toString() {
        return "EventType{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

}
