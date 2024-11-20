package com.github.hsabbas.todolist.controller;

import com.github.hsabbas.todolist.model.User;
import com.github.hsabbas.todolist.model.api.EmailAvailableResponse;
import com.github.hsabbas.todolist.model.api.LoginRequest;
import com.github.hsabbas.todolist.model.api.LoginResponse;
import com.github.hsabbas.todolist.model.api.RegistrationRequest;
import com.github.hsabbas.todolist.service.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {
    @Mock
    AuthService authService;

    @InjectMocks
    AuthController authController;

    @Test
    public void registerTestCreated(){
        RegistrationRequest registrationRequest = new RegistrationRequest("test@example.com", "password123");
        Mockito.when(authService.saveNewUser(registrationRequest)).thenReturn(true);
        ResponseEntity<String> response = authController.register(registrationRequest);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    public void registerTestBadRequest(){
        RegistrationRequest registrationRequest = new RegistrationRequest("test@example.com", "password123");
        Mockito.when(authService.saveNewUser(registrationRequest)).thenReturn(false);
        ResponseEntity<String> response = authController.register(registrationRequest);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void registerTestInternalServerError(){
        RegistrationRequest registrationRequest = new RegistrationRequest("test@example.com", "password123");
        Mockito.when(authService.saveNewUser(registrationRequest)).thenThrow(new RuntimeException());
        ResponseEntity<String> response = authController.register(registrationRequest);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void loginTest(){
        String email = "test@example.com";
        LoginRequest loginRequest = new LoginRequest(email, "password123");
        HttpServletResponse servletResponse = Mockito.mock(HttpServletResponse.class);
        LoginResponse loginResponse = new LoginResponse(true, 1, email);
        Mockito.when(authService.loginUser(loginRequest, servletResponse)).thenReturn(loginResponse);
        ResponseEntity<LoginResponse> response = authController.login(loginRequest, servletResponse);
        assertNotNull(response.getBody());
        assertTrue(response.getBody().authenticated());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void loginTestFailed(){
        String email = "test@example.com";
        LoginRequest loginRequest = new LoginRequest(email, "password123");
        HttpServletResponse servletResponse = Mockito.mock(HttpServletResponse.class);
        LoginResponse loginResponse = new LoginResponse(false, 0, "");
        Mockito.when(authService.loginUser(loginRequest, servletResponse)).thenReturn(loginResponse);
        ResponseEntity<LoginResponse> response = authController.login(loginRequest, servletResponse);
        assertNotNull(response.getBody());
        assertFalse(response.getBody().authenticated());
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    public void getUserTest(){
        String email = "test@example.com";
        Authentication authentication = Mockito.mock(Authentication.class);
        User user = new User();
        user.setId(1);
        user.setEmail(email);
        Mockito.when(authService.getUserInfo(authentication)).thenReturn(user);
        ResponseEntity<User> response = authController.getUser(authentication);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(email, response.getBody().getEmail());
        assertEquals(1, response.getBody().getId());
    }

    @Test
    public void logoutTest(){
        HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
        ResponseEntity<String> responseEntity = authController.logout(response);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Mockito.verify(authService).logoutUser(response);
    }

    @Test
    public void checkEmailAvailTest(){
        String email = "test@example.com";
        Mockito.when(authService.checkEmailIsAvailable(email)).thenReturn(true);
        ResponseEntity<EmailAvailableResponse> response = authController.checkEmailAvail(email);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().available());
    }
}