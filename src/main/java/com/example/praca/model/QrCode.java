package com.example.praca.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * @author Daniel Lezniak
 */
@Data
@Entity
@Table(name = "qr_code_events")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QrCode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    byte[] content;

    @Column(nullable = false)
    private Long eventId;

    @ManyToOne
    @JoinColumn(name="user_id", nullable=false)
    private User user;
}
