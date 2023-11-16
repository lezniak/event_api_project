package com.example.praca.dto.event;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class QrCodeDto {
    private byte[] qrCodeContent;
    private boolean active;
}
