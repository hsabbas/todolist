package com.github.hsabbas.todolist.service.impl;

import com.github.hsabbas.todolist.model.Task;
import com.github.hsabbas.todolist.model.User;
import com.github.hsabbas.todolist.repository.TaskRepository;
import com.github.hsabbas.todolist.repository.UserRepository;
import com.github.hsabbas.todolist.service.ToDoService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ToDoServiceImpl implements ToDoService {
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    @Override
    public List<Task> getTasksByUserId(long userId) {
        return taskRepository.findAllByUserId(userId);
    }

    @Override
    public List<Task> getTasksByEmail(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("Unknown user"));
        List<Task> tasks = taskRepository.findAllByUserId(user.getId());
        return tasks;
    }

    @Override
    public Task addNewTask(Task task) {
        return taskRepository.save(task);
    }

    @Override
    public void deleteTask(Long taskId) {
        taskRepository.deleteById(taskId);
    }
}
