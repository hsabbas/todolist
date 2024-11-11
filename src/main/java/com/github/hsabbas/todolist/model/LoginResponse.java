package com.github.hsabbas.todolist.model;

public record LoginResponse (boolean authenticated, long id, String email){}
