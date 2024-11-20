package com.github.hsabbas.todolist.service.impl;

import com.github.hsabbas.todolist.model.CreateTaskRequest;
import com.github.hsabbas.todolist.model.Task;
import com.github.hsabbas.todolist.model.UpdateTaskRequest;
import com.github.hsabbas.todolist.model.User;
import com.github.hsabbas.todolist.repository.TaskRepository;
import com.github.hsabbas.todolist.repository.UserRepository;
import com.github.hsabbas.todolist.service.ToDoService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.Calendar;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ToDoServiceImpl implements ToDoService {
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    @Override
    public List<Task> getTasksByEmail(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("Unknown user"));
        return taskRepository.findAllByUserId(user.getId());
    }

    @Override
    public Task addNewTask(CreateTaskRequest taskRequest, String userEmail) {
        User user = userRepository.findByEmail(userEmail).orElseThrow(() -> new UsernameNotFoundException("User: " + userEmail + " not found"));
        Task task = new Task();
        task.setName(taskRequest.getName());
        task.setDescription(taskRequest.getDescription());
        task.setDueDate(taskRequest.getDueDate());
        task.setCreationDate(new Date(Calendar.getInstance().getTime().getTime()));
        task.setComplete(false);
        task.setUserId(user.getId());
        return taskRepository.save(task);
    }

    @Override
    public Task updateTask(UpdateTaskRequest updateRequest, String userEmail) throws BadRequestException {
        Task task = taskRepository.findById(updateRequest.getTaskId()).orElseThrow(() -> new RuntimeException("Unknown Task: " + updateRequest.getTaskId()));
        User user = userRepository.findByEmail(userEmail).orElseThrow(() -> new UsernameNotFoundException("User: " + userEmail + " not found"));
        if(task.getUserId() != user.getId()) {
            throw new BadRequestException("Task does not belong to user");
        }
        task.setName(updateRequest.getName());
        task.setDescription(updateRequest.getDescription());
        task.setDueDate(updateRequest.getDueDate());
        task.setComplete(updateRequest.isComplete());
        return taskRepository.save(task);
    }

    @Override
    public void deleteTask(Long taskId, String userEmail) throws BadRequestException {
        Task task = taskRepository.findById(taskId).orElseThrow(() -> new RuntimeException("Unknown Task: " + taskId));
        User user = userRepository.findByEmail(userEmail).orElseThrow(() -> new UsernameNotFoundException("User: " + userEmail + " not found"));
        if(task.getUserId() != user.getId()) {
            throw new BadRequestException("Task does not belong to user");
        }
        taskRepository.deleteById(taskId);
    }
}
