package com.example.praca.dto.user;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Daniel Lezniak
 */
@Data
public class UpdateUserDto {
    private Long id;
    private String name;
    private String email;
    private String phoneNumber;
    private String password;
    private String matchingPassword;
//    private MultipartFile userPhoto;
    private String userPhoto;
}
