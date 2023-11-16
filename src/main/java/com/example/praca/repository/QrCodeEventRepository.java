package com.example.praca.repository;

import com.example.praca.model.QrCode;
import com.google.zxing.qrcode.encoder.QRCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author Daniel Lezniak
 */
@Repository
public interface QrCodeEventRepository extends JpaRepository<QrCode, Long> {

    @Query("select q from QrCode q where q.eventId = ?1")
    Optional<QrCode> findByEventId(Long eventId);
}
