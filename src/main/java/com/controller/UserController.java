package com.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.entity.User;
import com.service.FriendService;
import com.service.UserService;
/**
 * 处理用户注册登录
 */
@Component("userController")
public class UserController {
private UserService userService;
private FriendService friendService;
public UserService getUserService() {
	return userService;
}
@Resource
public void setUserService(UserService userService) {
	this.userService = userService;
}

public FriendService getFriendService() {
	return friendService;
}
@Resource
public void setFriendService(FriendService friendService) {
	this.friendService = friendService;
}
public boolean valid(User user){
	User u=userService.get(user.getUserid());
	if(u!=null&&user.getPassword().equals(u.getPassword())){
		return true;
	}
	else
		return false;
}

public boolean register(User user) {
	if(userService.get(user.getUserid()) != null){
		System.out.println("该用户已经存在！");
		return false;
	}
	else
	{
		userService.add(user);
		return true;
	}
}
public boolean addFriend(String user,String friend){
	if(friendService.isFriends(user, friend)){
		System.out.println("已经为好友了,不能再次添加！");
		return false;
	}
	else
	{
		friendService.add(user,friend);
		return true;
	}
}
public void deleteFriend(String user,String friend){
	friendService.delete(user, friend);
}
public List<String> getFriendList(String userid) {
	List<String> friends=friendService.getList(userid);
	return friends;
}
}
