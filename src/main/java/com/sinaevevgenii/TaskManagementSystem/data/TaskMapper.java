package com.sinaevevgenii.TaskManagementSystem.data;

import com.sinaevevgenii.TaskManagementSystem.data.dto.TaskDTO;
import com.sinaevevgenii.TaskManagementSystem.data.dto.UserDTO;
import com.sinaevevgenii.TaskManagementSystem.data.entity.Comment;
import com.sinaevevgenii.TaskManagementSystem.data.entity.Task;
import com.sinaevevgenii.TaskManagementSystem.data.entity.User;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class TaskMapper {

    public TaskDTO convertToDTO(Task task) {
        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setId(task.getId());
        taskDTO.setTitle(task.getTitle());
        taskDTO.setDescription(task.getDescription());
        taskDTO.setStatus(String.valueOf(task.getStatus()));
        taskDTO.setPriority(task.getPriority());
        taskDTO.setAuthor(task.getAuthor().getUsername());

        Set<String> assignedUsers = new HashSet<>();
        for (User user : task.getAssignees()){
            assignedUsers.add(user.getEmail());
        }

        taskDTO.setAssigneesName(assignedUsers);

        List<String> commentsArray = task.getComments().stream()
                .map(Comment::getText).toList();
        taskDTO.setCommentsArray(commentsArray);

        return taskDTO;
    }


}