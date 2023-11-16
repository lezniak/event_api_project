package com.example.praca.dto.user;

import com.example.praca.model.User;
import com.example.praca.service.ServiceFunctions;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.zxing.maxicode.decoder.Decoder;

import lombok.Builder;
import lombok.Data;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import java.util.Base64;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Daniel Lezniak
 */
@Data
public class InformationUserDto {
    private Long id;
    private String name;
    private String email;
    private String phoneNumber;
    private String token;
    private String refreshToken;
    private List<String> authorities;
//    private MultipartFile userPhoto;
//    private String userPhoto;
    private Double lastLat;
    private Double lastLng;

    public static InformationUserDto of(User user, String... refreshToken) {
        InformationUserDto informationUserDto = new InformationUserDto();
        informationUserDto.setId(user.getId());
        informationUserDto.setName(user.getName());
        informationUserDto.setEmail(user.getEmail());
        informationUserDto.setPhoneNumber(user.getPhoneNumber());
        informationUserDto.setAuthorities(user.getAuthorities().stream().map(s -> s.getAuthority()).collect(Collectors.toList()));
        informationUserDto.setRefreshToken((refreshToken != null && refreshToken.length > 0) ? refreshToken[0] : "");
//        informationUserDto.setUserPhoto(informationUserDto.decodeBase64(user.getUserPhoto()));
        return informationUserDto;
    }

    public static InformationUserDto of(User user, String newJwtToken) {
        InformationUserDto informationUserDto = new InformationUserDto();
        informationUserDto.setId(user.getId());
        informationUserDto.setName(user.getName());
        informationUserDto.setEmail(user.getEmail());
        informationUserDto.setPhoneNumber(user.getPhoneNumber());
        informationUserDto.setAuthorities(user.getAuthorities().stream().map(s -> s.getAuthority()).collect(Collectors.toList()));
        informationUserDto.setToken((newJwtToken != null && newJwtToken.length() > 0) ? newJwtToken : "");

        return informationUserDto;
    }

    public static InformationUserDto of(User user) {
        InformationUserDto informationUserDto = new InformationUserDto();
        informationUserDto.setId(user.getId());
        informationUserDto.setName(user.getName());
        informationUserDto.setEmail(user.getEmail());
        informationUserDto.setPhoneNumber(user.getPhoneNumber());
        informationUserDto.setAuthorities(user.getAuthorities().stream().map(s -> s.getAuthority()).collect(Collectors.toList()));
        informationUserDto.setLastLng(user.getUserLastLogin().getLng());
        informationUserDto.setLastLat(user.getUserLastLogin().getLat());

        return informationUserDto;

    }

    public static InformationUserDto ofAfterRegister(User user) {
        InformationUserDto informationUserDto = new InformationUserDto();
        informationUserDto.setId(user.getId());
        informationUserDto.setName(user.getName());
        informationUserDto.setEmail(user.getEmail());
        informationUserDto.setPhoneNumber(user.getPhoneNumber());
        informationUserDto.setAuthorities(user.getAuthorities().stream().map(s -> s.getAuthority()).collect(Collectors.toList()));
        informationUserDto.setLastLng(00.0000);
        informationUserDto.setLastLat(00.0000);

        return informationUserDto;

    }

    private String decodeBase64(byte[] bytes) {
        Base64.Decoder dec = Base64.getDecoder();
        return new String(dec.decode(bytes));
    }




}
