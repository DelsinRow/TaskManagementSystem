package com.sinaevevgenii.TaskManagementSystem.data.dto;

import com.sinaevevgenii.TaskManagementSystem.data.entity.User;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Getter
@Setter
public class TaskDTO {
    private Integer id;
    private String title;
    private String description;
    private String status;
    private String priority;
    private String author;
    private Set<String> assigneesName;
    private List<String> commentsArray;

}
