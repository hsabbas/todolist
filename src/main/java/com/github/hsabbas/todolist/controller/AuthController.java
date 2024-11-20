package com.github.hsabbas.todolist.controller;

import com.github.hsabbas.todolist.constants.APIPaths;
import com.github.hsabbas.todolist.model.*;
import com.github.hsabbas.todolist.service.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;


@RequiredArgsConstructor
@RestController
public class AuthController {
    private final AuthService authService;

    @PostMapping(APIPaths.REGISTER)
    public ResponseEntity<String> register(@Valid @RequestBody RegistrationRequest registrationRequest){
        try{
            boolean saved = authService.saveNewUser(registrationRequest);
            if(saved){
                return new ResponseEntity<>("Registration successful!", HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>("Registration failed", HttpStatus.BAD_REQUEST);
            }
        } catch (Exception ex){
            return new ResponseEntity<>("An error occured: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(APIPaths.LOGIN)
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest, HttpServletResponse response){
        LoginResponse loginResponse = authService.loginUser(loginRequest, response);
        if(loginResponse.authenticated()){
            return new ResponseEntity<>(loginResponse, HttpStatus.OK);
        }
        return new ResponseEntity<>(loginResponse, HttpStatus.UNAUTHORIZED);
    }

    @GetMapping(APIPaths.USER)
    public ResponseEntity<User> getUser(Authentication authentication) {
        return new ResponseEntity<>(authService.getUserInfo(authentication), HttpStatus.OK);
    }

    @PostMapping(APIPaths.LOGOUT)
    public ResponseEntity<String> logout(HttpServletResponse response){
        authService.logoutUser(response);
        return new ResponseEntity<>("User logged out", HttpStatus.OK);
    }

    @GetMapping(APIPaths.CHECK_EMAIL)
    public ResponseEntity<EmailAvailableResponse> checkEmailAvail(@RequestParam String email) {
        boolean emailIsAvailable = authService.checkEmailIsAvailable(email);
        return new ResponseEntity<>(new EmailAvailableResponse(emailIsAvailable), HttpStatus.OK);
    }
}
