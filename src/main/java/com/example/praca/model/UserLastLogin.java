package com.example.praca.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

/**
 * @author Daniel Lezniak
 */

@Data
@Entity
@Table(name = "user_last_login")
public class UserLastLogin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition= "Decimal(8,6)", nullable = false)
    private Double lat;

    @Column(columnDefinition = "Decimal(9,6)", nullable = false)
    private Double lng;

    @EqualsAndHashCode.Exclude
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    public static UserLastLogin update(UserLastLogin userLastLogin, UserLastLogin updatedLastLogin) {
        userLastLogin.setLng(updatedLastLogin.getLng());
        userLastLogin.setLat(updatedLastLogin.getLat());

        return userLastLogin;
    }

}
