package com.example.praca.controller;

import com.example.praca.dto.common.IdDto;
import com.example.praca.dto.filter.EventFilterParam;
import com.example.praca.dto.task.CreateTaskDto;
import com.example.praca.dto.task.TaskFilterParam;
import com.example.praca.dto.task.UpdateTaskDto;
import com.example.praca.dto.task.UpdateTaskStatusDto;
import com.example.praca.service.ReturnService;
import com.example.praca.service.TaskService;
import io.netty.util.internal.StringUtil;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * @author Daniel Lezniak
 */
@RestController
@AllArgsConstructor
@RequestMapping("/task")
public class TaskController {
    private final TaskService TASK_SERVICE;

    @PreAuthorize("isAuthenticated() && hasAuthority('USER')")
    @PostMapping("")
    public ReturnService createTask(@RequestBody CreateTaskDto dto) {
        return TASK_SERVICE.createTask(dto);
    }

    @DeleteMapping("")
    public ReturnService deleteTask(@RequestBody IdDto dto) {
        return TASK_SERVICE.deleteTask(dto);
    }

    @PutMapping("")
    public ReturnService updateTask(@RequestBody UpdateTaskDto dto) {
        return TASK_SERVICE.updateTask(dto);
    }

    /**
     * Pobiera taski dla danego usera
     * @param pageNo
     * @param pageSize
     * @param sortBy
     * @param sortDir
     * @param user
     * @param organization
     * @return
     */
    @GetMapping("/user-organization")
    public ReturnService getTaskForUserInOrganization(@RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
                                        @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
                                        @RequestParam(value = "sortBy", defaultValue = "id", required = false) String sortBy,
                                        @RequestParam(value = "sortDir", defaultValue = "dsc", required = false) String sortDir,
                                        @RequestParam(value = "user", defaultValue = "", required = true) String user,
                                        @RequestParam(value = "organization", defaultValue = "", required = true) String organization) {
        return TASK_SERVICE.getAllTask(pageNo, pageSize, sortBy, sortDir, TaskFilterParam.UO, new String[] {user, organization});
    }

    /**
     * pobiera taski całej oragnizacji
     * @param pageNo
     * @param pageSize
     * @param sortBy
     * @param sortDir
     * @param organization
     * @return
     */
    @GetMapping("/organization")
    public ReturnService getTaskForOrganization(@RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
                                        @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
                                        @RequestParam(value = "sortBy", defaultValue = "id", required = false) String sortBy,
                                        @RequestParam(value = "sortDir", defaultValue = "dsc", required = false) String sortDir,
                                        @RequestParam(value = "organization", defaultValue = "", required = true) String organization) {
        return TASK_SERVICE.getAllTask(pageNo, pageSize, sortBy, sortDir, TaskFilterParam.O, new String[] {organization});
    }
    @GetMapping("/my-tasks")
    public ReturnService getUserTaskForOrganizationUser(@RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
                                                @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
                                                @RequestParam(value = "sortBy", defaultValue = "id", required = false) String sortBy,
                                                @RequestParam(value = "sortDir", defaultValue = "dsc", required = false) String sortDir,
                                                @RequestParam(value = "eventId", defaultValue = "", required = false) String eventId
                                               ) {
        if (StringUtil.isNullOrEmpty(eventId))
            return TASK_SERVICE.getAllTask(pageNo, pageSize, sortBy, sortDir, TaskFilterParam.U, new String[] {});

        return TASK_SERVICE.getAllTask(pageNo, pageSize, sortBy, sortDir, TaskFilterParam.UE, new String[] {eventId});

    }

    @PutMapping("/update-status")
    public ReturnService updateTaskStatus(@RequestBody UpdateTaskStatusDto dto) {
        return TASK_SERVICE.updateTaskStatus(dto);
    }

}
