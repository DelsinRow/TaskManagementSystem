package com.sinaevevgenii.TaskManagementSystem.services;

import com.sinaevevgenii.TaskManagementSystem.config.JwtService;

import com.sinaevevgenii.TaskManagementSystem.data.TaskMapper;
import com.sinaevevgenii.TaskManagementSystem.data.UserMapper;
import com.sinaevevgenii.TaskManagementSystem.data.dto.TaskDTO;
import com.sinaevevgenii.TaskManagementSystem.data.dto.UserDTO;
import com.sinaevevgenii.TaskManagementSystem.data.entity.Comment;
import com.sinaevevgenii.TaskManagementSystem.data.entity.StatusOfTask;
import com.sinaevevgenii.TaskManagementSystem.data.entity.Task;
import com.sinaevevgenii.TaskManagementSystem.data.entity.User;
import com.sinaevevgenii.TaskManagementSystem.repository.TaskRepository;
import com.sinaevevgenii.TaskManagementSystem.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class TaskService {
    private final JwtService jwtService;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final TaskMapper taskMapper;
    private final UserMapper userMapper;

    public TaskService(JwtService jwtService, TaskRepository taskRepository, UserRepository userRepository, TaskMapper taskMapper, UserMapper userMapper) {
        this.jwtService = jwtService;
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.taskMapper = taskMapper;
        this.userMapper = userMapper;
    }

    public TaskDTO getTask(HttpServletRequest request, Integer taskId) {
        if (token(request) != null) {
            Task existingTask = taskRepository.findById(taskId)
                    .orElseThrow(() -> new EntityNotFoundException("Task not found with id: " + taskId));
            return taskMapper.convertToDTO(existingTask);
        } else {
            throw new AccessDeniedException("User is not authorized");
        }
    }

    public TaskDTO createTask(HttpServletRequest request, Task newTask) {
        if (newTask.getTitle() == null || newTask.getTitle().isEmpty()) {
            throw new IllegalArgumentException("Title is required");
        }
        if (newTask.getStatus() == null) {
            throw new IllegalArgumentException("Status is required");
        }
        if (newTask.getPriority() == null || newTask.getPriority().isEmpty()) {
            throw new IllegalArgumentException("Priority is required");
        }
        String userName = currentUser(request);
        User author = userRepository.findByEmail(userName)
                .orElseThrow(() -> new EntityNotFoundException("User with email " + userName + " not found"));
        newTask.setAuthor(author);
        taskRepository.save(newTask);
        return taskMapper.convertToDTO(newTask);
    }

    public void updateTask(HttpServletRequest request, Integer taskId, Map<String, String> updates) {
        String authorName = currentUser(request);
        Task existingTask = taskRepository.findById(taskId)
                .orElseThrow(() -> new EntityNotFoundException("Task with id " + taskId + " not found"));
        if (userIsAuthorOfTask(taskId, authorName)) {
            for (Map.Entry<String, String> entry : updates.entrySet()) {
                String field = entry.getKey();
                String value = entry.getValue();

                switch (field) {
                    case "title" -> existingTask.setTitle(value);
                    case "description" -> existingTask.setDescription(value);
                    case "priority" -> existingTask.setPriority(value);
                }
            }
        }
        taskRepository.save(existingTask);
    }

    public void updateTaskStatus(HttpServletRequest request, Integer taskId, String status) {
        String userName = currentUser(request);
        Task existingTask = taskRepository.findById(taskId)
                .orElseThrow(() -> new EntityNotFoundException("Task with id " + taskId + " not found"));
        if (userIsAuthorOfTask(taskId, userName)) {
            existingTask.setStatus(StatusOfTask.valueOf(status.toUpperCase()));
        } else {
            throw new IllegalArgumentException("Users do not match");
        }

    }

    public void deleteTask(HttpServletRequest request, Integer taskId) {
        String userName = currentUser(request);
        if (!taskRepository.existsById(taskId)) {
            throw new EntityNotFoundException("Task with id " + taskId + " not found");
        } else {
            if (userIsAuthorOfTask(taskId, userName)) {
                taskRepository.deleteById(taskId);
            } else {
                throw new IllegalArgumentException("Users do not match");
            }
        }
    }

    public void updateAssignees(HttpServletRequest request, Integer taskId, Set<Integer> assigneeIds) {
        String userName = currentUser(request);
        Task existingTask = taskRepository.findById(taskId)
                .orElseThrow(() -> new EntityNotFoundException("Task with id " + taskId + " not found"));
        User user = userRepository.findByEmail(userName).orElseThrow();
        if (userIsAuthorOfTask(taskId, userName)) {
            Set<User> assignees = new HashSet<>(userRepository.findAllById(assigneeIds));
            existingTask.setAssignees(assignees);
            taskRepository.save(existingTask);
        } else {
            throw new IllegalArgumentException("Users do not match");
        }
    }

    public void addAssignees(HttpServletRequest request, Integer taskId, Set<Integer> newAssigneeIds) {
        String userName = currentUser(request);
        Task existingTask = taskRepository.findById(taskId)
                .orElseThrow(() -> new EntityNotFoundException("Task with id " + taskId + " not found"));
        if (userIsAuthorOfTask(taskId, userName)) {
            Set<User> newAssignees = new HashSet<>(userRepository.findAllById(newAssigneeIds));
            existingTask.getAssignees().addAll(newAssignees);
            taskRepository.save(existingTask);
        } else {
            throw new IllegalArgumentException("Users do not match");
        }
    }

    public void deleteAssignees(HttpServletRequest request, Integer taskId, Set<Integer> assigneeIdsToRemove) {
        String userName = currentUser(request);
        Task existingTask = taskRepository.findById(taskId)
                .orElseThrow(() -> new EntityNotFoundException("Task with id " + taskId + " not found"));
        if (userIsAuthorOfTask(taskId, userName)) {
            existingTask.getAssignees().removeIf(user -> assigneeIdsToRemove.contains(user.getId()));
            taskRepository.save(existingTask);
        } else {
            throw new IllegalArgumentException("Users do not match");
        }


    }

    public void addComment(HttpServletRequest request, Integer taskId, String comment) {
        String userName = currentUser(request);
        Task existingTask = taskRepository.findById(taskId)
                .orElseThrow(() -> new EntityNotFoundException("Task not found with id: " + taskId));
        if (userIsAuthorOfTask(taskId, userName) || userIsAssignedOfTask(taskId, userName)) {
            Comment newComment = new Comment();
            newComment.setText(comment);
            if (!existingTask.getComments().isEmpty()) {
                existingTask.getComments().add(newComment);
            } else {
                existingTask.setComments(new ArrayList<>(List.of(newComment)));
            }
        }
    }

    public UserDTO getInfoByUser(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found with email: " + email));
        return userMapper.convertToDTO(user);
    }

    public List<TaskDTO> findTaskByAuthorOrAssignedUser(String userName) {
        User user = userRepository.findByEmail(userName)
                .orElseThrow(() -> new EntityNotFoundException("User not found with email: " + userName));
        List<TaskDTO> authorTasks = userMapper.convertToDTO(user).getAuthoredTasks();
        Set<TaskDTO> assignedTasks = userMapper.convertToDTO(user).getAssignedTasks();
        List<TaskDTO> allUsersTask = new ArrayList<>();
        allUsersTask.addAll(authorTasks);
        allUsersTask.addAll(assignedTasks);
        return allUsersTask;
    }

    public String currentUser(HttpServletRequest request) {
        return jwtService.extractUsername(token(request));
    }

    public boolean userIsAuthorOfTask(Integer taskId, String username) {
        Task existingTask = taskRepository.findById(taskId)
                .orElseThrow(() -> new EntityNotFoundException("Task with id " + taskId + " not found"));
        return username.equals(existingTask.getAuthor().getUsername());
    }

    public boolean userIsAssignedOfTask(Integer taskId, String username) {
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new EntityNotFoundException("User with email: " + taskId + " not found"));
        Task existingTask = taskRepository.findById(taskId)
                .orElseThrow(() -> new EntityNotFoundException("Task with id " + taskId + " not found"));
        Set<User> assignedUsers = new HashSet<>(existingTask.getAssignees());
        return assignedUsers.contains(user);
    }

    public String token(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        String token = null;
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7);
        }
        return token;
    }
}
