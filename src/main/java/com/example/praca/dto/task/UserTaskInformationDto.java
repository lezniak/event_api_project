package com.example.praca.dto.task;

import com.example.praca.model.Task;
import lombok.Data;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Daniel Lezniak
 */
@Data
public class UserTaskInformationDto {
    Map<Long, List<TaskInformationDto>> taskList;

    public static UserTaskInformationDto of (List<Task> task) {
        UserTaskInformationDto dto = new UserTaskInformationDto();
        Map<Long, List<TaskInformationDto>> testMap = new HashMap<>();

        //Mapowanie poszczegolnych taskow na DTO
        task.stream()
                .map(x -> TaskInformationDto.of(x))
                .collect(Collectors.toList());
       Map<Long, List<Task>> testList = new HashMap<>();

       //Grupowanie tasków po organizationId zeby organizationId było kluczem w mapie
        testList = task.stream()
                .collect(Collectors.groupingBy(Task::getOrganizationId));

        testList.entrySet()
                        .stream()
                                .map(x -> testMap.put(x.getKey(), TaskInformationDto.of(x.getValue())))
                .collect(Collectors.toList());


        dto.setTaskList(testMap);
        return dto;
    }
}
