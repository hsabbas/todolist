package com.github.hsabbas.todolist.service.impl;

import com.github.hsabbas.todolist.model.User;
import com.github.hsabbas.todolist.model.UserPrincipal;
import com.github.hsabbas.todolist.security.JWTTokenManager;
import com.github.hsabbas.todolist.constants.JWTConstants;
import com.github.hsabbas.todolist.constants.Roles;
import com.github.hsabbas.todolist.model.api.LoginRequest;
import com.github.hsabbas.todolist.model.api.LoginResponse;
import com.github.hsabbas.todolist.model.api.RegistrationRequest;
import com.github.hsabbas.todolist.repository.UserRepository;
import com.github.hsabbas.todolist.service.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JWTTokenManager jwtTokenManager;
    private final AuthenticationManager authenticationManager;

    @Override
    public boolean saveNewUser(RegistrationRequest registrationRequest) {
        User user = new User();
        String hashedPassword = passwordEncoder.encode(registrationRequest.getPassword());
        user.setPassword(hashedPassword);
        user.setEmail(registrationRequest.getEmail());
        user.setRole(Roles.PREFIX + Roles.USER);
        User savedUser = userRepository.save(user);
        return savedUser.getId() > 0;
    }

    @Override
    public LoginResponse loginUser(LoginRequest loginRequest, HttpServletResponse response) {
        Authentication authentication = UsernamePasswordAuthenticationToken.unauthenticated(loginRequest.getEmail(), loginRequest.getPassword());
        Authentication authenticatedAuthentication = authenticationManager.authenticate(authentication);

        if(!authenticatedAuthentication.isAuthenticated()){
            return new LoginResponse(false, 0L, "");
        }

        String jwtToken = jwtTokenManager.generateJWTToken(authenticatedAuthentication);
        Cookie cookie = new Cookie(JWTConstants.COOKIE_NAME, jwtToken);
        cookie.setHttpOnly(true);
        cookie.setMaxAge((int)(JWTConstants.EXPIRATION_TIME / 1000));
        cookie.setAttribute("SameSite", "strict");
        response.addCookie(cookie);
        return new LoginResponse(true,((UserPrincipal) authenticatedAuthentication.getPrincipal()).getId(), loginRequest.getEmail());
    }

    @Override
    public User getUserInfo(Authentication authentication) {
        return userRepository.findByEmail(authentication.getName()).orElseThrow(() -> new UsernameNotFoundException("Unknown user"));
    }

    @Override
    public void logoutUser(HttpServletResponse response) {
        Cookie cookie = new Cookie(JWTConstants.COOKIE_NAME, null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }

    @Override
    public boolean checkEmailIsAvailable(String email) {
        return !userRepository.existsByEmail(email);
    }
}
