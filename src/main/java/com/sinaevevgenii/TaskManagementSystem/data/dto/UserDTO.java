package com.sinaevevgenii.TaskManagementSystem.data.dto;

import com.sinaevevgenii.TaskManagementSystem.data.entity.Task;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Getter
@Setter
public class UserDTO {
    private Integer id;
    private String email;
    private List<TaskDTO> authoredTasks;
    private Set<TaskDTO> assignedTasks;
}
