package com.example.praca.dto.hobby;

import lombok.Data;

import java.util.List;

/**
 * @author Daniel Lezniak
 */
@Data
public class AddHobbyToUserDto {
    private Long userId;
    private List<Long> hobbies;
}
