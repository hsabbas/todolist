package com.github.hsabbas.todolist.service;

import com.github.hsabbas.todolist.model.Task;

import java.util.List;

public interface ToDoService {
    List<Task> getTasksByUserId(long userId);
    List<Task> getTasksByEmail(String email);
    Task addNewTask(Task task);
    void deleteTask(Long taskId);
}
