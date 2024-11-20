package com.github.hsabbas.todolist.model.api;

import lombok.Getter;
import lombok.Setter;

import java.sql.Date;

@Getter @Setter
public class CreateTaskRequest {
    private String name;
    private String description;
    private Date dueDate;
}
