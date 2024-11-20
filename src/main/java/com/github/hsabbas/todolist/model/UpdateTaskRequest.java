package com.github.hsabbas.todolist.model;

import lombok.Getter;
import lombok.Setter;

import java.sql.Date;

@Getter @Setter
public class UpdateTaskRequest {
    private long taskId;
    private String name;
    private String description;
    private Date dueDate;
    private boolean complete;
}
