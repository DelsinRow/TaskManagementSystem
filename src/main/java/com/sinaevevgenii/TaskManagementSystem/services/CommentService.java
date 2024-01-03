package com.sinaevevgenii.TaskManagementSystem.services;

import com.sinaevevgenii.TaskManagementSystem.config.JwtService;
import com.sinaevevgenii.TaskManagementSystem.data.entity.Task;
import com.sinaevevgenii.TaskManagementSystem.data.entity.User;
import com.sinaevevgenii.TaskManagementSystem.repository.TaskRepository;
import com.sinaevevgenii.TaskManagementSystem.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class CommentService {
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final TaskRepository taskRepository;

    @Autowired
    public CommentService(JwtService jwtService, UserRepository userRepository, TaskRepository taskRepository) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.taskRepository = taskRepository;
    }

    public void seeComment(HttpServletRequest request, Integer taskId, String comment) {
        Task existingTask = taskRepository.findById(taskId)
                .orElseThrow(() -> new EntityNotFoundException("Task not found with id: " + taskId));

    }



}
