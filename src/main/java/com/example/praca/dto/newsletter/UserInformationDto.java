package com.example.praca.dto.newsletter;

import com.example.praca.dto.hobby.HobbyInformationDto;
import com.example.praca.dto.user.InformationUserDto;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @author Daniel Lezniak
 */
@Data
@Builder
public class UserInformationDto {
    private HobbyInformationDto hobbyInformationDto;
    private List<InformationUserDto> userInformationDtoList;
}
