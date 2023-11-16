package com.example.praca.model;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

/**
 * @author Daniel Lezniak
 */

@Data
@Entity
@Table(name = "event_payment")
public class EventPayment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Double price;

    @Column
    private String currency;

    @Column
    private String description;

    @CreationTimestamp
    @Column(updatable = false)
    private Date createdAt;

    @Column
    private Boolean success;

    @Column
    private String paymentLink;

    @Column
    private String paymentId;

    @Column
    private String payerId;

    @Column
    private String paymentToken;

    @ManyToOne
    @JoinColumn(name="event_payment", nullable=false)
    private Event eventPayment;

    @ManyToOne
    @JoinColumn(name="event_user_payment", nullable=false)
    private User userPayment;

}
