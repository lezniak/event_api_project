package com.example.praca.dto.task;

import lombok.Data;

/**
 * @author Daniel Lezniak
 */
@Data
public class UpdateTaskDto {
    private Long taskId;
    private String content;
    private Long memberId;
}
