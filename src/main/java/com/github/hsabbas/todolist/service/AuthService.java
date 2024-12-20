package com.github.hsabbas.todolist.service;

import com.github.hsabbas.todolist.model.api.LoginRequest;
import com.github.hsabbas.todolist.model.api.LoginResponse;
import com.github.hsabbas.todolist.model.api.RegistrationRequest;
import com.github.hsabbas.todolist.model.User;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;

public interface AuthService {
    boolean saveNewUser(RegistrationRequest registrationRequest);
    LoginResponse loginUser(LoginRequest loginRequest, HttpServletResponse response);
    User getUserInfo(Authentication authentication);
    void logoutUser(HttpServletResponse response);
    boolean checkEmailIsAvailable(String email);
}
