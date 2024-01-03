package com.sinaevevgenii.TaskManagementSystem.repository;

import com.sinaevevgenii.TaskManagementSystem.data.entity.Comment;
import com.sinaevevgenii.TaskManagementSystem.data.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
    Optional<Comment> findByTask(Task task);
}
