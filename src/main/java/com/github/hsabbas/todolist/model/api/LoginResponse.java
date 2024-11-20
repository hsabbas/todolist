package com.github.hsabbas.todolist.model.api;

public record LoginResponse (boolean authenticated, long id, String email){}
