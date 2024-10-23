package com.github.hsabbas.todolist.controller;

import com.github.hsabbas.todolist.constants.APIPaths;
import com.github.hsabbas.todolist.model.Task;
import com.github.hsabbas.todolist.service.ToDoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ToDoController {
    private final ToDoService toDoService;

    @GetMapping(APIPaths.GET_TASKS)
    public List<Task> getToDoList(@RequestParam long userId){
        return toDoService.getTasksByUserId(userId);
    }

    //Created for testing CSRF tokens.
    @PostMapping(APIPaths.TEST)
    public String getTEST(){
        return "Test successful!";
    }
}
