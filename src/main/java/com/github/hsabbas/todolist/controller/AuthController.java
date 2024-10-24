package com.github.hsabbas.todolist.controller;

import com.github.hsabbas.todolist.config.JWTTokenManager;
import com.github.hsabbas.todolist.constants.APIPaths;
import com.github.hsabbas.todolist.model.LoginRequest;
import com.github.hsabbas.todolist.model.LoginResponse;
import com.github.hsabbas.todolist.model.RegistrationRequest;
import com.github.hsabbas.todolist.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;


@RequiredArgsConstructor
@RestController
public class AuthController {
    private final AuthService authService;
    private final AuthenticationManager authenticationManager;
    private final JWTTokenManager jwtTokenManager;

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

    @PostMapping(APIPaths.LOGIN)
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest){
        Authentication authentication = UsernamePasswordAuthenticationToken.unauthenticated(loginRequest.getEmail(), loginRequest.getPassword());
        Authentication authenticatedAuthentication = authenticationManager.authenticate(authentication);

        if(!authenticatedAuthentication.isAuthenticated()){
            return new ResponseEntity<>(new LoginResponse(false, ""), HttpStatus.UNAUTHORIZED);
        }

        String jwtToken = jwtTokenManager.generateJWTToken(authenticatedAuthentication);
        return new ResponseEntity<>(new LoginResponse(true, jwtToken), HttpStatus.OK);
    }
}
