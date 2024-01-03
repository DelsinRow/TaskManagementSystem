package com.sinaevevgenii.TaskManagementSystem.controllers;


import com.sinaevevgenii.TaskManagementSystem.data.dto.TaskDTO;
import com.sinaevevgenii.TaskManagementSystem.data.dto.UserDTO;
import com.sinaevevgenii.TaskManagementSystem.data.entity.Task;
import com.sinaevevgenii.TaskManagementSystem.services.TaskService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/api/public/task")
public class TaskController {
    private final TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("/get/{taskId}")
    public TaskDTO getTask(HttpServletRequest request,
                           @PathVariable Integer taskId) {
            return taskService.getTask(request, taskId);
    }

    @PostMapping("/create")
    public ResponseEntity<Void> createTask(HttpServletRequest request,
                                              @RequestBody Task newTask) {
        try {
            taskService.createTask(request, newTask);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/upd/{taskId}")
    public ResponseEntity<Void> updateTask(HttpServletRequest request,
                                              @PathVariable Integer taskId,
                                              @RequestBody Map<String, String> updates) {
        try {
            taskService.updateTask(request, taskId, updates);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/upd-status/{taskId}")
    public ResponseEntity<Void> updateTaskStatus(HttpServletRequest request,
                                                 @PathVariable Integer taskId,
                                                 @RequestParam String updStatus) {
        try {
            taskService.updateTaskStatus(request, taskId, updStatus);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    @DeleteMapping("/del/{taskId}")
    public ResponseEntity<Void> deleteTask(HttpServletRequest request,
                                           @PathVariable Integer taskId) {
        try {
            taskService.deleteTask(request, taskId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/upd-assignees/{taskId}")
    public ResponseEntity<Void> updateAssignees(HttpServletRequest request,
                                                @PathVariable Integer taskId,
                                                @RequestBody Set<Integer> newAssigneeIds) {
        try {
            taskService.updateAssignees(request, taskId, newAssigneeIds);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/add-assignees/{taskId}")
    public ResponseEntity<Void> addAssignees(HttpServletRequest request,
                                             @PathVariable Integer taskId,
                                             @RequestBody Set<Integer> newAssigneeIds) {
        try {
            taskService.addAssignees(request, taskId, newAssigneeIds);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/del-assignees/{taskId}")
    public ResponseEntity<Void> delAssignees(HttpServletRequest request,
                                             @PathVariable Integer taskId,
                                             @RequestBody Set<Integer> newAssigneeIds) {
        try {
            taskService.deleteAssignees(request, taskId, newAssigneeIds);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/add-comment/{taskId}")
    public ResponseEntity<Void> addComment(HttpServletRequest request,
                                           @PathVariable Integer taskId,
                                           @RequestBody String comment) {
        try {
            taskService.addComment(request, taskId, comment);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping
    public List<TaskDTO> findTaskByAuthorOrAssignedUser (@RequestParam String tasksBy) {
        return taskService.findTaskByAuthorOrAssignedUser(tasksBy);
    }

    @GetMapping("/user-info/")
    public UserDTO findUser(@RequestParam String email) {
        return taskService.getInfoByUser(email);
    }

}
