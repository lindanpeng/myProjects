package com.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.entity.User;
import com.impl.UserDaoImpl;
@Component("userService")
public class UserService {
private UserDaoImpl userDaoImpl;
@Resource
public void setUserDaoImpl(UserDaoImpl userDaoImpl){
	this.userDaoImpl=userDaoImpl;
}
public void add(User user){
	this.userDaoImpl.add(user);
}
public void delete(User user){
	this.userDaoImpl.delete(user);
}
public void update(User user){
	this.userDaoImpl.update(user);
}
public User get(String userid){
    return	this.userDaoImpl.get(User.class, userid);
}
}
