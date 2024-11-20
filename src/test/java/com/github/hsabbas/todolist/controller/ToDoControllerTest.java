package com.github.hsabbas.todolist.controller;

import com.github.hsabbas.todolist.model.Task;
import com.github.hsabbas.todolist.model.api.CreateTaskRequest;
import com.github.hsabbas.todolist.model.api.UpdateTaskRequest;
import com.github.hsabbas.todolist.service.impl.ToDoServiceImpl;
import org.apache.coyote.BadRequestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ToDoControllerTest {
    @Mock
    ToDoServiceImpl toDoService;

    @InjectMocks
    ToDoController toDoController;

    String email = "test@example.com";
    Authentication authentication;

    @BeforeEach
    public void setup(){
        authentication = Mockito.mock(Authentication.class);
        Mockito.when(authentication.getName()).thenReturn(email);
    }

    @Test
    public void getToDoListTest(){
        List<Task> tasks = new ArrayList<>();
        Task task1 = new Task();
        task1.setTaskId(1);
        task1.setName("task1");
        Task task2 = new Task();
        task2.setTaskId(2);
        task2.setName("task2");
        tasks.add(task1);
        tasks.add(task2);

        Mockito.when(toDoService.getTasksByEmail(email)).thenReturn(tasks);
        ResponseEntity<List<Task>> response = toDoController.getToDoList(authentication);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("task1", response.getBody().get(0).getName());
        assertEquals("task2", response.getBody().get(1).getName());
    }

    @Test
    public void postNewTaskTest(){
        CreateTaskRequest request = new CreateTaskRequest();
        Task task = new Task();
        task.setName("task1");
        task.setTaskId(1);
        Mockito.when(toDoService.addNewTask(request, email)).thenReturn(task);
        ResponseEntity<Task> response = toDoController.postNewTask(request, authentication);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getTaskId());
    }

    @Test
    public void postNewTaskTestFailure(){
        CreateTaskRequest request = new CreateTaskRequest();
        Task task = new Task();
        task.setName("task1");
        Mockito.when(toDoService.addNewTask(request, email)).thenReturn(task);
        ResponseEntity<Task> response = toDoController.postNewTask(request, authentication);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    public void updateTaskTest() throws BadRequestException {
        UpdateTaskRequest updateRequest = new UpdateTaskRequest();
        Task task = new Task();
        task.setTaskId(1);
        task.setName("task1");

        Mockito.when(toDoService.updateTask(updateRequest, email)).thenReturn(task);
        ResponseEntity<Task> response = toDoController.updateTask(updateRequest, authentication);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getTaskId());
        assertEquals("task1", response.getBody().getName());
    }

    @Test
    public void deleteTaskTest() throws BadRequestException {
        long taskId = 1;
        ResponseEntity<String> response = toDoController.deleteTask(taskId, authentication);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        Mockito.verify(toDoService).deleteTask(taskId, email);
    }
}