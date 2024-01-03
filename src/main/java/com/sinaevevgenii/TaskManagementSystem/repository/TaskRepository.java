package com.sinaevevgenii.TaskManagementSystem.repository;

import com.sinaevevgenii.TaskManagementSystem.data.entity.Task;
import com.sinaevevgenii.TaskManagementSystem.data.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Integer> {

}
