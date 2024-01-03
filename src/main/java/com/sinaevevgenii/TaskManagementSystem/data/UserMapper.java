package com.sinaevevgenii.TaskManagementSystem.data;

import com.sinaevevgenii.TaskManagementSystem.data.dto.TaskDTO;
import com.sinaevevgenii.TaskManagementSystem.data.dto.UserDTO;
import com.sinaevevgenii.TaskManagementSystem.data.entity.Task;
import com.sinaevevgenii.TaskManagementSystem.data.entity.User;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class UserMapper {
    private final TaskMapper taskMapper;

    public UserMapper(TaskMapper taskMapper) {
        this.taskMapper = taskMapper;
    }

    public UserDTO convertToDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setEmail(user.getEmail());
        List<TaskDTO> authoredTask = new ArrayList<>();
        for (Task task : user.getAuthoredTasks()) {
            authoredTask.add(taskMapper.convertToDTO(task));
        }
        userDTO.setAuthoredTasks(authoredTask);

        Set<TaskDTO> assignedTask = new HashSet<>();
        for (Task task : user.getAssignedTasks()) {
            assignedTask.add(taskMapper.convertToDTO(task));
        }
        userDTO.setAssignedTasks(assignedTask);

        return userDTO;
    }
}
