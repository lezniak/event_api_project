package com.example.praca.model;

import lombok.Data;
import net.jcip.annotations.GuardedBy;

import javax.persistence.*;

/**
 * @author Daniel Lezniak
 */
@Data
@Entity
@Table(name = "places")
public class Places {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String address;

    @Column(columnDefinition= "Decimal(8,6)", nullable = false)
    private Double lat;

    @Column(columnDefinition = "Decimal(9,6)", nullable = false)
    private Double lng;

    @Column
    private int capacity;

    @Column
    private String phoneNumber;

    @Column
    private String email;
}
