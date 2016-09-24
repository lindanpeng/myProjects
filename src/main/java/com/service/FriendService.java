package com.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.entity.Friend;
import com.impl.FriendDaoImpl;

@Component("friendService")
public class FriendService {
private FriendDaoImpl friendDaoImpl;
@Resource
public void setFriendDaoImpl(FriendDaoImpl friendDaoImpl) {
	this.friendDaoImpl = friendDaoImpl;
}
public void add(String userid,String friendid){
	Friend friend=new Friend();
	friend.setUserid(userid);
	friend.setFriendid(friendid);
	this.friendDaoImpl.add(friend);
	Friend friend2=new Friend();
	friend2.setUserid(friendid);
	friend2.setFriendid(userid);
	this.friendDaoImpl.add(friend2);
}
public void delete(String userid,String friendid){
 friendDaoImpl.delete(userid, friendid);
 friendDaoImpl.delete(friendid, userid);
}
public  List<String> getList(String userid){
	return this.friendDaoImpl.getList(userid);
	
}
public boolean isFriends(String user,String friend){
	return this.friendDaoImpl.isFriends(user,friend);
}
}
