package com.github.hsabbas.todolist.service;

import com.github.hsabbas.todolist.model.api.CreateTaskRequest;
import com.github.hsabbas.todolist.model.Task;
import com.github.hsabbas.todolist.model.api.UpdateTaskRequest;
import org.apache.coyote.BadRequestException;

import java.util.List;

public interface ToDoService {
    List<Task> getTasksByEmail(String email);
    Task addNewTask(CreateTaskRequest taskRequest, String userEmail);
    Task updateTask(UpdateTaskRequest updateRequest, String userEmail) throws BadRequestException;
    void deleteTask(Long taskId, String userEmail) throws BadRequestException;
}
