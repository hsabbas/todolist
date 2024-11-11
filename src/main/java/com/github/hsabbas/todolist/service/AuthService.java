package com.github.hsabbas.todolist.service;

import com.github.hsabbas.todolist.model.LoginRequest;
import com.github.hsabbas.todolist.model.LoginResponse;
import com.github.hsabbas.todolist.model.RegistrationRequest;
import com.github.hsabbas.todolist.model.User;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;

public interface AuthService {
    HttpStatus saveNewUser(RegistrationRequest registrationRequest);
    LoginResponse loginUser(LoginRequest loginRequest, HttpServletResponse response);
    User getUserInfo(Authentication authentication);
    void logoutUser(HttpServletResponse response);
}
