package com.example.praca.dto.hobby;

import lombok.Data;

/**
 * @author Daniel Lezniak
 */
@Data
public class DeleteHobbyUserDto {
    private Long userId;
    private Long hobbyId;
}
