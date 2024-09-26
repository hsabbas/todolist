package com.github.hsabbas.todolist.controller;

import com.github.hsabbas.todolist.constants.APIPaths;
import com.github.hsabbas.todolist.model.RegistrationRequest;
import com.github.hsabbas.todolist.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class AuthController {
    private final AuthService authService;

    @PostMapping(APIPaths.REGISTER)
    public ResponseEntity<String> register(@Valid @RequestBody RegistrationRequest registrationRequest){
        try{
            HttpStatus status = authService.saveNewUser(registrationRequest);
            if(status.is2xxSuccessful()){
                return ResponseEntity.status(status).body("Registration successful!");
            } else {
                return ResponseEntity.status(status).body("Registration failed");
            }
        } catch (Exception ex){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occured: " + ex.getMessage());
        }
    }
}
