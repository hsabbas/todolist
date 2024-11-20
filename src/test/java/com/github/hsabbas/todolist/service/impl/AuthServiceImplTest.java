package com.github.hsabbas.todolist.service.impl;

import com.github.hsabbas.todolist.model.User;
import com.github.hsabbas.todolist.model.UserPrincipal;
import com.github.hsabbas.todolist.model.api.LoginRequest;
import com.github.hsabbas.todolist.model.api.LoginResponse;
import com.github.hsabbas.todolist.model.api.RegistrationRequest;
import com.github.hsabbas.todolist.repository.UserRepository;
import com.github.hsabbas.todolist.security.JWTTokenManager;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {
    @Mock
    PasswordEncoder passwordEncoder;
    @Mock
    UserRepository userRepository;
    @Mock
    JWTTokenManager jwtTokenManager;
    @Mock
    AuthenticationManager authenticationManager;

    @InjectMocks
    AuthServiceImpl authService;

    @Test
    public void saveNewUserTest() {
        String email = "test@example.com";
        String password = "password123";
        RegistrationRequest registrationRequest = new RegistrationRequest(email, password);

        User newUser = new User();
        newUser.setId(1);
        newUser.setEmail(email);
        Mockito.when(userRepository.save(Mockito.any(User.class))).thenReturn(newUser);
        assertTrue(authService.saveNewUser(registrationRequest));
    }

    @Test
    public void loginUserTestSuccess() {
        String email = "test@example.com";
        String password = "password123";
        String jwtToken = "test token";
        UserPrincipal userPrincipal = new UserPrincipal(1, email, password, List.of(new SimpleGrantedAuthority("USER")));
        LoginRequest loginRequest = new LoginRequest(email, password);
        HttpServletResponse servletResponse = Mockito.mock(HttpServletResponse.class);
        Authentication successfulAuthentication = Mockito.mock(Authentication.class);
        Mockito.when(successfulAuthentication.isAuthenticated()).thenReturn(true);
        Mockito.when(successfulAuthentication.getPrincipal()).thenReturn(userPrincipal);
        Mockito.when(authenticationManager.authenticate(Mockito.any())).thenReturn(successfulAuthentication);
        Mockito.when(jwtTokenManager.generateJWTToken(successfulAuthentication)).thenReturn(jwtToken);
        LoginResponse loginResponse = authService.loginUser(loginRequest, servletResponse);
        assertEquals(1, loginResponse.id());
        assertEquals(email, loginResponse.email());
        assertTrue(loginResponse.authenticated());
        Mockito.verify(servletResponse).addCookie(Mockito.any());
    }

    @Test
    public void loginUserTestFailure() {
        String email = "test@example.com";
        String password = "password123";
        LoginRequest loginRequest = new LoginRequest(email, password);
        HttpServletResponse servletResponse = Mockito.mock(HttpServletResponse.class);
        Authentication failedAuthentication = Mockito.mock(Authentication.class);
        Mockito.when(failedAuthentication.isAuthenticated()).thenReturn(false);
        Mockito.when(authenticationManager.authenticate(Mockito.any())).thenReturn(failedAuthentication);
        LoginResponse loginResponse = authService.loginUser(loginRequest, servletResponse);
        assertFalse(loginResponse.authenticated());
    }

    @Test
    public void getUserInfoTestSuccess() {
        String email = "test@example.com";
        User user = new User();
        user.setId(1);
        user.setEmail(email);
        Authentication authentication = Mockito.mock(Authentication.class);
        Mockito.when(authentication.getName()).thenReturn(email);
        Mockito.when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        User returnedUser = authService.getUserInfo(authentication);
        assertEquals(email, returnedUser.getEmail());
        assertEquals(1, returnedUser.getId());
    }

    @Test
    public void getUserInfoTestFailure() {
        String email = "test@example.com";
        Authentication authentication = Mockito.mock(Authentication.class);
        Mockito.when(authentication.getName()).thenReturn(email);
        Mockito.when(userRepository.findByEmail(email)).thenReturn(Optional.empty());
        assertThrows(UsernameNotFoundException.class, () -> authService.getUserInfo(authentication));
    }

    @Test
    public void logoutUserTest() {
        HttpServletResponse mockResponse = Mockito.mock(HttpServletResponse.class);

        authService.logoutUser(mockResponse);
        Mockito.verify(mockResponse).addCookie(Mockito.any());
    }

    @Test
    public void checkEmailIsAvailableTest(){
        String email = "test@example.com";
        Mockito.when(userRepository.existsByEmail(email)).thenReturn(true);
        assertFalse(authService.checkEmailIsAvailable(email));

        Mockito.when(userRepository.existsByEmail(email)).thenReturn(false);
        assertTrue(authService.checkEmailIsAvailable(email));
    }
}