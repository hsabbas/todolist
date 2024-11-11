package com.github.hsabbas.todolist.controller;

import com.github.hsabbas.todolist.constants.APIPaths;
import com.github.hsabbas.todolist.model.Task;
import com.github.hsabbas.todolist.service.AuthService;
import com.github.hsabbas.todolist.service.ToDoService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ToDoController {
    private final ToDoService toDoService;
    private final AuthService authService;

    @GetMapping(APIPaths.GET_TASKS)
    public List<Task> getToDoList(@RequestParam long userId){
        return toDoService.getTasksByUserId(userId);
    }

    @PostMapping(APIPaths.TEST)
    public ResponseEntity<String> test(HttpServletResponse response) {
        authService.logoutUser(response);
        return new ResponseEntity<>("Test successful", HttpStatus.OK);
    }

    @PostMapping(APIPaths.POST_TASK)
    public ResponseEntity<Task> postNewTask(@RequestBody @Valid Task task) {
        Task savedTask = toDoService.addNewTask(task);
        if(savedTask.getTaskId() != 0) {
            return new ResponseEntity<>(savedTask, HttpStatus.CREATED);
        }
        return new ResponseEntity<>(task, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PutMapping(APIPaths.PUT_TASK)
    public ResponseEntity<Task> updateTask(@RequestBody @Valid Task task){
        Task savedTask = toDoService.addNewTask(task);
        return new ResponseEntity<>(savedTask, HttpStatus.CREATED);
    }

    @DeleteMapping(APIPaths.DELETE_TASK)
    public ResponseEntity<String> deleteTask(@RequestParam Long taskId) {
        toDoService.deleteTask(taskId);
        return new ResponseEntity("Task deleted", HttpStatus.OK);
    }
}
