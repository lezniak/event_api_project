package com.example.praca.dto.user;

import com.example.praca.model.UserRole;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author Daniel Lezniak
 */
@Data
public class CreateUserDto {
    private String name;
    private String email;
    private String password;
    private String matchingPassword;
    private String phoneNumber;
    private String city;
    private String birthdate;
    private List<UserRole> userRole;
//    private MultipartFile userPhoto;
    private String userPhotoS;
    public static CreateUserDto of(UpdateUserDto dto) {
        CreateUserDto user = new CreateUserDto();

        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());
        user.setMatchingPassword(dto.getMatchingPassword());
        user.setPassword(dto.getPassword());
        user.setUserPhotoS(dto.getUserPhoto());

        return user;
    }

}

