package com.example.praca.dto.task;

import lombok.Data;

/**
 * @author Daniel Lezniak
 */
@Data
public class UpdateTaskStatusDto {
    private Long id;
    private String taskStatus;
}
