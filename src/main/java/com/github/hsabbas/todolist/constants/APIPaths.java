package com.github.hsabbas.todolist.constants;

public class APIPaths {
    public static final String REGISTER = "/register";
    public static final String LOGIN = "/login";
    public static final String GET_TASKS = "/tasks";
    public static final String USER = "/user";
    public static final String LOGOUT = "/signout";
    public static final String TEST = "/test";
    public static final String POST_TASK = "/addTask";
    public static final String PUT_TASK = "/updateTask";
    public static final String DELETE_TASK = "/deleteTask";


    public static final String[] USER_APIS = {GET_TASKS, POST_TASK, PUT_TASK, DELETE_TASK};
    public static final String[] PUBLIC_APIS = {REGISTER, LOGIN, LOGOUT, "/error", TEST};
}
