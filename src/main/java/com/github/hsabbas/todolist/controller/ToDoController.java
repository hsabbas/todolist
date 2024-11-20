package com.github.hsabbas.todolist.controller;

import com.github.hsabbas.todolist.constants.APIPaths;
import com.github.hsabbas.todolist.model.api.CreateTaskRequest;
import com.github.hsabbas.todolist.model.Task;
import com.github.hsabbas.todolist.model.api.UpdateTaskRequest;
import com.github.hsabbas.todolist.service.ToDoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ToDoController {
    private final ToDoService toDoService;

    @GetMapping(APIPaths.GET_TASKS)
    public ResponseEntity<List<Task>> getToDoList(Authentication authentication){
        return new ResponseEntity<>(toDoService.getTasksByEmail(authentication.getName()), HttpStatus.OK);
    }

    @PostMapping(APIPaths.POST_TASK)
    public ResponseEntity<Task> postNewTask(@RequestBody @Valid CreateTaskRequest task, Authentication authentication) {
        Task savedTask = toDoService.addNewTask(task, authentication.getName());
        if(savedTask.getTaskId() != 0) {
            return new ResponseEntity<>(savedTask, HttpStatus.CREATED);
        }
        return new ResponseEntity<>(new Task(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PutMapping(APIPaths.PUT_TASK)
    public ResponseEntity<Task> updateTask(@RequestBody @Valid UpdateTaskRequest task, Authentication authentication) throws BadRequestException {
        Task updatedTask = toDoService.updateTask(task, authentication.getName());
        return new ResponseEntity<>(updatedTask, HttpStatus.CREATED);
    }

    @DeleteMapping(APIPaths.DELETE_TASK)
    public ResponseEntity<String> deleteTask(@RequestParam Long taskId, Authentication authentication) throws BadRequestException {
        toDoService.deleteTask(taskId, authentication.getName());

        return new ResponseEntity<>("Task deleted", HttpStatus.OK);
    }
}
