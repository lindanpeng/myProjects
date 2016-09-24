package com.vo;

import java.io.Serializable;

import com.entity.GroupMessage;

import constant.DataType;

public class Message implements Serializable{
//数据类型
private DataType type;
private String content;
private String time;
private String fromUser;
private String toUser;
public Message(){
	
}
public Message(GroupMessage groupMessage){
	this.type=DataType.GROUP;
	this.content=groupMessage.getContent();
	this.fromUser=groupMessage.getUserid();
	this.time=groupMessage.getTime();
}
public DataType getType() {
	return type;
}
public void setType(DataType type) {
	this.type = type;
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
public String getFromUser() {
	return fromUser;
}
public void setFromUser(String fromUser) {
	this.fromUser = fromUser;
}
public String getToUser() {
	return toUser;
}
public void setToUser(String toUser) {
	this.toUser = toUser;
}

}
