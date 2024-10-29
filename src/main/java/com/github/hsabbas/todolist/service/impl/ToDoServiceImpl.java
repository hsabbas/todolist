package com.github.hsabbas.todolist.service.impl;

import com.github.hsabbas.todolist.model.Task;
import com.github.hsabbas.todolist.repository.TaskRepository;
import com.github.hsabbas.todolist.service.ToDoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ToDoServiceImpl implements ToDoService {
    private final TaskRepository taskRepository;

    @Override
    public List<Task> getTasksByUserId(long userId) {
        return taskRepository.findAllByUserId(userId);
    }
}
