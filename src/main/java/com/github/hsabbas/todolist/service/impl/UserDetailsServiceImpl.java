package com.github.hsabbas.todolist.service.impl;

import com.github.hsabbas.todolist.model.User;
import com.github.hsabbas.todolist.model.UserPrincipal;
import com.github.hsabbas.todolist.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user =  userRepository.findByEmail(username).
                orElseThrow(() -> new UsernameNotFoundException("User: " + username + " does not exist"));
        return new UserPrincipal(user);
    }
}
