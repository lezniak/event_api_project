package com.example.praca.service;

import com.example.praca.dto.common.IdDto;
import com.example.praca.dto.common.PageableDto;
import com.example.praca.dto.task.*;
import com.example.praca.exception.InvalidOrganizationOwnerException;
import com.example.praca.exception.TaskNotFoundException;
import com.example.praca.model.Organization;
import com.example.praca.model.OrganizationMember;
import com.example.praca.model.Task;
import com.example.praca.repository.TaskRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Daniel Lezniak
 */
@Slf4j
@Service
@AllArgsConstructor
public class TaskService {
    private final TaskRepository TASK_REPOSITORY;
    private final OrganizationService ORGANIZATION_SERVICE;
    private final UserService USER_SERVICE;
    private final OrganizationMemberService ORGANIZATION_MEMBER_SERVICE;
    public ReturnService createTask(CreateTaskDto createTaskDto) {
        Long creatorId = USER_SERVICE.getUserIdByEmail(USER_SERVICE.getUserEmailFromJwt());
        if (!ORGANIZATION_SERVICE.isOrganizationOwner(creatorId))
            throw new InvalidOrganizationOwnerException();
        OrganizationMember organizationMember = ORGANIZATION_MEMBER_SERVICE.getOrganizationMemberById(createTaskDto.getMemberId());
        Organization organization = ORGANIZATION_SERVICE.getOrganizationById(createTaskDto.getOrganizationId());
        try {
            Task createdTask = TASK_REPOSITORY.save(Task.createTaskMapper(createTaskDto, organizationMember, organization));
            return ReturnService.returnInformation("Succ. create task", TaskInformationDto.of(createdTask), 1);
        } catch (Exception ex) {
            log.error("Err. creating task for: " + createTaskDto.getMemberId() + ex.getMessage());
            return ReturnService.returnError("Err. creating task: " + ex.getMessage(), -1);
        }
    }

    public ReturnService deleteTask(IdDto dto) {
        Long user = USER_SERVICE.getUserIdByEmail(USER_SERVICE.getUserEmailFromJwt());
        if (!ORGANIZATION_SERVICE.isOrganizationOwner(user))
            throw new InvalidOrganizationOwnerException();

        try {
            TASK_REPOSITORY.deleteById(dto.getId());
            return ReturnService.returnInformation("Succ. delete task ", 1);
        } catch (Exception ex) {
            log.error("Err. delete task id: " + dto.getId() + " " + ex.getMessage());
            return ReturnService.returnError("Err. delete task:  " + ex.getMessage(), -1);
        }
    }

    public ReturnService updateTask(UpdateTaskDto dto) {
        Long user = USER_SERVICE.getUserIdByEmail(USER_SERVICE.getUserEmailFromJwt());
        if (!ORGANIZATION_SERVICE.isOrganizationOwner(user))
            throw new InvalidOrganizationOwnerException();

        Task task = getTaskById(dto.getTaskId());
        OrganizationMember organizationMember = ORGANIZATION_MEMBER_SERVICE.getOrganizationMemberById(dto.getMemberId());

        try {
            Task updatedTask = TASK_REPOSITORY.save(Task.updateTask(task, dto, organizationMember));
            return ReturnService.returnInformation("Succ. update task", TaskInformationDto.of(updatedTask), 1);
        } catch (Exception ex) {
            log.error("Err. update task: " + dto.getTaskId() + ex.getMessage());
            return ReturnService.returnError("Err. update task", -1);
        }
    }

    public ReturnService updateTaskStatus(UpdateTaskStatusDto dto) {
        Task task = getTaskById(dto.getId());

        if (!ORGANIZATION_SERVICE.isOrganizationOwner(task.getOrganization().getOwner().getId()))
            throw new InvalidOrganizationOwnerException();
        task.setStatus(dto.getTaskStatus());
        try {
            TASK_REPOSITORY.save(task);
            return ReturnService.returnInformation("succ. update status", 1);
        } catch (Exception ex) {
            log.error("Err. update task: " + dto.getId() + ex.getMessage());
            return ReturnService.returnError("Err. update task", -1);
        }


    }
    public Task getTaskById(Long id) {
        return TASK_REPOSITORY.findById(id).orElseThrow(() -> new TaskNotFoundException());
    }
    public ReturnService getAllTask(int pageno, int pageSize, String sortBy, String sortDir, TaskFilterParam filterParam, String... value) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageno, pageSize, sort);
        Page<Task> taskPage = null;

        switch (filterParam) {
            case UO:
                Long userId = Long.parseLong(value[0]);
                Long organizatio = Long.parseLong(value[1]);
                Long ownerId = USER_SERVICE.getUserIdByEmail(USER_SERVICE.getUserEmailFromJwt());
                if (!ORGANIZATION_SERVICE.isOrganizationOwner(ownerId))
                    throw new InvalidOrganizationOwnerException();
                taskPage = TASK_REPOSITORY.findAllByUserIdAndOrganizationId(userId, organizatio, pageable);
                break;
            case O:
                Long id = USER_SERVICE.getUserIdByEmail(USER_SERVICE.getUserEmailFromJwt());
                if (!ORGANIZATION_SERVICE.isOrganizationOwner(id))
                    throw new InvalidOrganizationOwnerException();
                Long organizationId = Long.parseLong(value[0]);
                taskPage = TASK_REPOSITORY.findAllByOrganizationId(organizationId, pageable);
                break;
            case U:
                Long memberId = USER_SERVICE.getUserIdByEmail(USER_SERVICE.getUserEmailFromJwt());
//                taskPage = TASK_REPOSITORY.findAllByUserId(memberId, pageable);
                Page<Task> informationTaskPage = null;
                informationTaskPage = TASK_REPOSITORY.findAllByMemberId(memberId, pageable);
                List<Task> taskList = informationTaskPage.getContent();
//                UserTaskInformationDto dto1 = UserTaskInformationDto.of(taskList);
                UserTaskInformationDto dto1 = UserTaskInformationDto.of(taskList);
                return ReturnService.returnInformation("",dto1, 1);

            case UE:
                Long eventId = Long.valueOf(value[0]);
                Long userTaskId = USER_SERVICE.getUserIdByEmail(USER_SERVICE.getUserEmailFromJwt());
                taskPage = TASK_REPOSITORY.findAllByUserIdAndEventId(userTaskId, eventId, pageable);
                break;

        }

        if (taskPage == null)
            throw new TaskNotFoundException();
        List<Task> taskList = taskPage.getContent();
        List<TaskInformationDto> taskInformationDtos = taskList.stream()
                .map(x -> TaskInformationDto.of(x))
                .collect(Collectors.toList());

        return ReturnService.returnInformation("", PageableDto.of(taskInformationDtos, taskPage), 1);
    }

}
