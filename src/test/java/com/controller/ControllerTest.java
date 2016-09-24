package com.controller;

import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.entity.User;

public class ControllerTest {
	@Test
	public void testLogin() {
		ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
		UserController userController = (UserController) ctx.getBean("userController");
		User user = new User("admin", "admin");
		userController.valid(user);
	}

	@Test
	public void testRegister() {
		ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
		UserController userController = (UserController) ctx.getBean("userController");
		if(userController.register(new User("admin","12345")))
				System.out.println("wrong!");
	}
}
