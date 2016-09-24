package com.service;

import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.entity.User;

public class UserServiceTest {
	@Test
 public void testAdd(){
	 ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
	 UserService userService=(UserService)ctx.getBean("userService");
	 User user=new User();
	 user.setUserid("admin");
	 user.setPassword("admin");
	 userService.add(user);
	 ctx.destroy();
 }
}
