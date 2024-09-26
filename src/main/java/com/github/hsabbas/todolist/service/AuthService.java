package com.github.hsabbas.todolist.service;

import com.github.hsabbas.todolist.model.RegistrationRequest;
import org.springframework.http.HttpStatus;

public interface AuthService {
    HttpStatus saveNewUser(RegistrationRequest registrationRequest);
}
