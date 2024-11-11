package com.github.hsabbas.todolist.constants;

public class JWTConstants {
    public static final long EXPIRATION_TIME = 86400000L;
    public static final String JWT_HEADER = "Authorization";
    public static final String EMAIL_CLAIM = "Email";
    public static final String AUTHORITIES_CLAIM = "Authorities";
    public static final String COOKIE_NAME = "ACCESS-TOKEN";
}
