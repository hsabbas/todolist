package com.github.hsabbas.todolist.service.impl;

import com.github.hsabbas.todolist.model.RegistrationRequest;
import com.github.hsabbas.todolist.model.User;
import com.github.hsabbas.todolist.repository.UserRepository;
import com.github.hsabbas.todolist.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Override
    public HttpStatus saveNewUser(RegistrationRequest registrationRequest) {
        User user = new User();
        String hashedPassword = passwordEncoder.encode(registrationRequest.getPassword());
        user.setPassword(hashedPassword);
        user.setEmail(registrationRequest.getEmail());
        user.setRole("USER");
        User savedUser = userRepository.save(user);
        if(savedUser.getId() > 0){
            return HttpStatus.CREATED;
        } else {
            return HttpStatus.BAD_REQUEST;
        }
    }
}
