package com.example.praca.dto.hobby;

import com.example.praca.model.Hobby;
import lombok.Data;

/**
 * @author Daniel Lezniak
 */
@Data
public class HobbyInformationDto {
    private Long id;
    private String name;

    public static HobbyInformationDto of(Hobby hobby) {
        HobbyInformationDto dto = new HobbyInformationDto();

        dto.setId(hobby.getId());
        dto.setName(hobby.getName());

        return dto;
    }
}
