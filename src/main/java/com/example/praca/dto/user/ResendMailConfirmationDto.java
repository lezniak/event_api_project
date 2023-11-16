package com.example.praca.dto.user;

import lombok.Data;

/**
 * @author Daniel Lezniak
 */
@Data
public class ResendMailConfirmationDto {

    private String email;
    private String password;

}
