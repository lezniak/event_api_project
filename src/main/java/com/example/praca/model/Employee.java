package com.example.praca.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "employee")
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;
    @Column
    private String lastName;
    @Column
    private String phoneNumber;
    @Column
    private Double salary;

    @ManyToOne
    @JoinColumn(name="boss_id", nullable=false)
    private User boss;


}
