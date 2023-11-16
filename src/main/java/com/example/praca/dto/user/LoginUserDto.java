package com.example.praca.dto.user;

import lombok.Data;

/**
 * @author Daniel Lezniak
 */
@Data
public class LoginUserDto {
    private String email;
    private String phoneNumber;
    private String password;
    private Double lat;
    private Double lng;


    public static LoginUserDto of(UpdateUserDto dto) {
        LoginUserDto user = new LoginUserDto();

        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());
        user.setPhoneNumber(dto.getPhoneNumber());

        return user;
    }

    public static LoginUserDto of(UpdateUserPasswordDto dto) {
        LoginUserDto user = new LoginUserDto();

        user.setEmail(dto.getEmail());
        user.setPassword(dto.getCurrentPassword());

        return user;
    }
}
