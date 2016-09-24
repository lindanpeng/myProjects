package com.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import com.vo.Message;
@Entity
public class GroupMessage {
@Id
@GeneratedValue
private int id;
private String userid;
private String time;
private String content;
public GroupMessage(){
	
}
public GroupMessage(Message message) {
	this.userid=message.getFromUser();
	this.time=message.getTime();
	this.content=message.getContent();
}
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
public String getTime() {
	return time;
}
public void setTime(String time) {
	this.time = time;
}
public String getContent() {
	return content;
}
public void setContent(String content) {
	this.content = content;
}

}
