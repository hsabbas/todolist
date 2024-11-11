package com.github.hsabbas.todolist.model;

import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@AllArgsConstructor
public class UserPrincipal implements UserDetails {
    private long userId;
    private String email;
    private String password;
    private Collection<? extends GrantedAuthority> grantedAuthorities;

    public UserPrincipal(User user) {
        userId = user.getId();
        email = user.getEmail();
        password = user.getPassword();
        grantedAuthorities = List.of(new SimpleGrantedAuthority(user.getRole()));
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return grantedAuthorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    public long getId(){
        return userId;
    }
}
