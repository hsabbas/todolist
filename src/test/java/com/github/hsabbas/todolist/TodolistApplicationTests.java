package com.github.hsabbas.todolist;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = { "JWT_SECRET = Secret" })
class TodolistApplicationTests {

	@Test
	void contextLoads() {
	}

}
