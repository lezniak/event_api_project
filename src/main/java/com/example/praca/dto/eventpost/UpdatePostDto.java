package com.example.praca.dto.eventpost;

import lombok.Data;

/**
 * @author Daniel Lezniak
 */
@Data
public class UpdatePostDto {
    private Long postId;
    private String content;
}
