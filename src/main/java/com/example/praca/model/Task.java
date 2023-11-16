package com.example.praca.model;

import com.example.praca.dto.task.CreateTaskDto;
import com.example.praca.dto.task.TaskInformationDto;
import com.example.praca.dto.task.TaskStatus;
import com.example.praca.dto.task.UpdateTaskDto;
import lombok.*;

import javax.persistence.*;

/**
 * @author Daniel Lezniak
 */
@Data
@Entity
@Table(name = "task")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String content;
    @Column
    private String status;

    @Column
    private Long organizationId;
    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name="member", nullable=false)
    private OrganizationMember member;
    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name="organization", nullable=false)
    private Organization organization;



    public static Task createTaskMapper(CreateTaskDto dto, OrganizationMember organizationMember, Organization organization) {
        Task task = new Task();
        task.setContent(dto.getContent());
        task.setMember(organizationMember);
        task.setStatus(TaskStatus.W.name);
        task.setOrganization(organization);

        return task;
    }


    public static Task updateTask(Task task, UpdateTaskDto dto, OrganizationMember organizationMember) {
        task.setContent(dto.getContent());
        task.setMember(organizationMember);

        return task;
    }


}
