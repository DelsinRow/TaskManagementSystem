package com.sinaevevgenii.TaskManagementSystem.data.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentDTO {
    private Integer id;
    private String text;
    private Integer taskId;
}
