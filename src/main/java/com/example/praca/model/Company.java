package com.example.praca.model;

import lombok.Data;

import javax.persistence.*;

/**
 * @author Daniel Lezniak
 */
@Data
@Entity
@Table(name = "company")
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String name;

}
