package com.example.praca.dto.user;

import lombok.Data;

/**
 * @author Daniel Lezniak
 */
@Data
public class UpdateUserPasswordDto {
    private String email;
    private Long id;
    private String currentPassword;
    private String password;
    private String matchingPassword;
}
