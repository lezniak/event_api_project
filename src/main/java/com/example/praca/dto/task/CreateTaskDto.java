package com.example.praca.dto.task;

import lombok.Data;

/**
 * @author Daniel Lezniak
 */
@Data
public class CreateTaskDto {
    private String content;
    private Long memberId;
    private Long organizationId;
}
