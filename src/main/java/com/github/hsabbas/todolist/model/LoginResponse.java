package com.github.hsabbas.todolist.model;

public record LoginResponse (boolean authenticated, String jwtToken){}
