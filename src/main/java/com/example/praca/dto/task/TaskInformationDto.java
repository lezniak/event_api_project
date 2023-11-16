package com.example.praca.dto.task;

import com.example.praca.model.Task;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Daniel Lezniak
 */
@Data
public class TaskInformationDto {
    private String content;
    private String status;
    private Long taskId;
    private Long memberId;
    private String memberName;
    private Long organizationId;
    private String organizationName;

    public static TaskInformationDto of(Task task) {
        TaskInformationDto dto = new TaskInformationDto();

        dto.setContent(task.getContent());
        dto.setStatus(task.getStatus());
        dto.setTaskId(task.getId());
        dto.setMemberId(task.getMember().getId());
        dto.setMemberName(task.getMember().getName());
        dto.setOrganizationId(task.getOrganization().getId());
        dto.setOrganizationName(task.getOrganization().getName());

        return dto;
    }

    public static List<TaskInformationDto> of (List<Task> taskList) {
        List<TaskInformationDto> taskInformationDtos = new ArrayList<>();

        return taskInformationDtos = taskList.stream()
                .map(x -> of(x))
                .collect(Collectors.toList());
    }
}
