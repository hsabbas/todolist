package com.github.hsabbas.todolist.controller;

import com.github.hsabbas.todolist.constants.APIPaths;
import com.github.hsabbas.todolist.model.RegistrationRequest;
import com.github.hsabbas.todolist.model.User;
import com.github.hsabbas.todolist.repository.UserRepository;
import com.github.hsabbas.todolist.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RequiredArgsConstructor
@RestController
public class AuthController {
    private final AuthService authService;
    private final UserRepository userRepository;

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

    @RequestMapping(APIPaths.LOGIN)
    public ResponseEntity<User> login(Authentication authentication){
        Optional<User> optionalUser = userRepository.findByEmail(authentication.getName());
        return new ResponseEntity<>(optionalUser.orElse(null), HttpStatus.OK);
    }
}
