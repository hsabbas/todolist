package com.github.hsabbas.todolist.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

import java.sql.Date;

@Entity
@Data
public class Task {
    @Id
    @Column(name = "task_id")
    private long taskId;
    @Column(name = "user_id")
    private long userId;
    private String name;
    private String description;
    @Column(name = "creation_date")
    private Date creationDate;
    @Column(name = "due_date")
    private Date dueDate;
}
