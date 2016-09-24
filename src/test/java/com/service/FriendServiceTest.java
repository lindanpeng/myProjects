package com.service;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;


public class FriendServiceTest {
private static FriendService friendService;
@BeforeClass
public static void setFriendService(){
	ClassPathXmlApplicationContext ctx=new ClassPathXmlApplicationContext("applicationContext.xml");
    friendService=(FriendService) ctx.getBean("friendService");
}
@Test
public void testDelete(){
	
	friendService.delete("xiaoming","hong");
}
@Test
public void testGetList(){

	System.out.println(friendService.getList("admin"));
}
@Test
public void testAddFriend(){
	friendService.add("xiaoming","hong");
}
}
