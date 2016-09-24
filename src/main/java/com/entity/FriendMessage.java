package com.entity;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;

public class FriendMessage {
@Id
@GeneratedValue
private int  id;
private String userid;
private String friendid;
private String content;
private String time;
public int getId() {
	return id;
}
public void setId(int id) {
	this.id = id;
}
public String getUserid() {
	return userid;
}
public void setUserid(String userid) {
	this.userid = userid;
}
public String getFriendid() {
	return friendid;
}
public void setFriendid(String friendid) {
	this.friendid = friendid;
}
public String getContent() {
	return content;
}
public void setContent(String content) {
	this.content = content;
}
public String getTime() {
	return time;
}
public void setTime(String time) {
	this.time = time;
}

}
