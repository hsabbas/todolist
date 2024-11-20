package com.github.hsabbas.todolist.service.impl;

import com.github.hsabbas.todolist.model.Task;
import com.github.hsabbas.todolist.model.User;
import com.github.hsabbas.todolist.model.api.CreateTaskRequest;
import com.github.hsabbas.todolist.model.api.UpdateTaskRequest;
import com.github.hsabbas.todolist.repository.TaskRepository;
import com.github.hsabbas.todolist.repository.UserRepository;
import org.apache.coyote.BadRequestException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ToDoServiceImplTest {
    @Mock
    TaskRepository taskRepository;

    @Mock
    UserRepository userRepository;

    @InjectMocks
    ToDoServiceImpl toDoService;

    private List<Task> getTasks(){
        List<Task> tasks = new ArrayList<>();
        Task task = new Task();
        task.setName("task1");
        Task task2 = new Task();
        task2.setName("task2");
        tasks.add(task);
        tasks.add(task2);
        return tasks;
    }

    @Test
    public void getTasksByEmailTestSuccess(){
        String email = "test@example.com";
        long id = 123;
        User testUser = new User();
        testUser.setId(id);
        Optional<User> testOptional = Optional.of(testUser);
        Mockito.when(userRepository.findByEmail(email)).thenReturn(testOptional);
        Mockito.when(taskRepository.findAllByUserId(id)).thenReturn(getTasks());
        List<Task> tasks = toDoService.getTasksByEmail(email);
        assertEquals(2, tasks.size());
        assertEquals("task1", tasks.get(0).getName());
        assertEquals("task2", tasks.get(1).getName());
    }

    @Test
    public void getTasksByEmailTestFailure(){
        Mockito.when(userRepository.findByEmail(Mockito.anyString())).thenReturn(Optional.empty());
        assertThrows(UsernameNotFoundException.class, () -> toDoService.getTasksByEmail("test"));
    }

    @Test
    public void addNewTaskTestSuccess(){
        String email = "test@example.com";
        long id = 123;
        User testUser = new User();
        testUser.setId(id);
        Mockito.when(userRepository.findByEmail(email)).thenReturn(Optional.of(testUser));

        Task task = new Task();
        task.setTaskId(123);
        task.setName("test task");
        Mockito.when(taskRepository.save(Mockito.any())).thenReturn(task);
        CreateTaskRequest taskRequest = new CreateTaskRequest();
        taskRequest.setName("test task");
        taskRequest.setDescription("test task description");
        Task savedTask = toDoService.addNewTask(taskRequest, email);
        assertEquals(123, savedTask.getTaskId());
        assertEquals("test task", savedTask.getName());
    }

    @Test
    public void addNewTaskTestFailure(){
        Mockito.when(userRepository.findByEmail(Mockito.anyString())).thenReturn(Optional.empty());
        assertThrows(UsernameNotFoundException.class, () -> toDoService.addNewTask(new CreateTaskRequest(), "test"));
    }

    @Test
    public void updateTaskTestSuccess() throws BadRequestException {
        long taskId = 123;
        String email = "test@example.com";
        long userId = 123;

        UpdateTaskRequest updateRequest = new UpdateTaskRequest();
        updateRequest.setTaskId(taskId);

        Task presavedTask = new Task();
        presavedTask.setTaskId(taskId);
        presavedTask.setUserId(userId);
        presavedTask.setName("test task");

        Task savedTask = new Task();
        savedTask.setTaskId(taskId);
        savedTask.setName("modified name");

        User testUser = new User();
        testUser.setId(userId);

        Mockito.when(taskRepository.findById(taskId)).thenReturn(Optional.of(presavedTask));
        Mockito.when(userRepository.findByEmail(email)).thenReturn(Optional.of(testUser));

        Mockito.when(taskRepository.save(Mockito.any())).thenReturn(savedTask);
        Task result = toDoService.updateTask(updateRequest, email);
        assertEquals(123, result.getTaskId());
        assertEquals("modified name", result.getName());
    }

    @Test
    public void updateTaskTestFailureTaskNotFound(){
        Mockito.when(taskRepository.findById(Mockito.any())).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> toDoService.updateTask(new UpdateTaskRequest(), "test"));
    }

    @Test
    public void updateTaskTestFailureUserNotFound(){
        Mockito.when(taskRepository.findById(Mockito.any())).thenReturn(Optional.of(new Task()));
        Mockito.when(userRepository.findByEmail(Mockito.anyString())).thenReturn(Optional.empty());
        assertThrows(UsernameNotFoundException.class, () -> toDoService.updateTask(new UpdateTaskRequest(), "test"));
    }

    @Test
    public void updateTaskTestFailureNonMatchingUserId(){
        Task task = new Task();
        task.setUserId(122);
        User user = new User();
        user.setId(123);

        Mockito.when(taskRepository.findById(Mockito.any())).thenReturn(Optional.of(task));
        Mockito.when(userRepository.findByEmail(Mockito.anyString())).thenReturn(Optional.of(user));
        assertThrows(BadRequestException.class, () -> toDoService.updateTask(new UpdateTaskRequest(), "test"));
    }

    @Test
    public void deleteTaskSuccess() throws BadRequestException {
        long taskId = 123;
        long userId = 123;
        String email = "test@example.com";
        Task task = new Task();
        task.setUserId(taskId);
        task.setUserId(userId);
        User user = new User();
        user.setId(userId);
        Mockito.when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));
        Mockito.when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        toDoService.deleteTask(taskId, email);
        Mockito.verify(taskRepository).deleteById(taskId);
    }

    @Test
    public void deleteTaskTestFailureTaskNotFound(){
        Mockito.when(taskRepository.findById(Mockito.any())).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> toDoService.deleteTask(123L, "test"));
    }

    @Test
    public void deleteTaskTestFailureUserNotFound(){
        Mockito.when(taskRepository.findById(Mockito.any())).thenReturn(Optional.of(new Task()));
        Mockito.when(userRepository.findByEmail(Mockito.anyString())).thenReturn(Optional.empty());
        assertThrows(UsernameNotFoundException.class, () -> toDoService.deleteTask(123L, "test"));
    }

    @Test
    public void deleteTaskTestFailureNonMatchingUserId(){
        Task task = new Task();
        task.setUserId(122);
        User user = new User();
        user.setId(123);

        Mockito.when(taskRepository.findById(Mockito.any())).thenReturn(Optional.of(task));
        Mockito.when(userRepository.findByEmail(Mockito.anyString())).thenReturn(Optional.of(user));
        assertThrows(BadRequestException.class, () -> toDoService.deleteTask(123L, "test"));
    }
}