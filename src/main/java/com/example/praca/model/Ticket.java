package com.example.praca.model;

import lombok.Data;

import javax.persistence.*;

/**
 * @author Daniel Lezniak
 */
@Data
@Entity
@Table(name = "ticket")
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    byte[] content;

    @Column
    private Boolean used;
    @ManyToOne
    @JoinColumn(name="ticket", nullable=false)
    private User user;

    @ManyToOne
    @JoinColumn(name="event", nullable=false)
    private Event event;

    public static Ticket createTicket (User user, Event event, byte[] content) {
        Ticket ticket = new Ticket();

        ticket.setUsed(false);
        ticket.setEvent(event);
        ticket.setUser(user);
        ticket.setContent(content);

        return ticket;
    }
}
