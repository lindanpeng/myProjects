package com.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.entity.GroupMessage;
import com.service.GroupMsgService;
import com.vo.Message;
@Component("messageController")
public class MessageController {
private GroupMsgService  groupMsgService;

public GroupMsgService getGroupMsgService() {
	return groupMsgService;
}
@Resource
public void setGroupMsgService(GroupMsgService groupMsgService) {
	this.groupMsgService = groupMsgService;
}

public void saveGroupMsg(Message message){
	GroupMessage groupMsg=new GroupMessage(message);
	groupMsgService.add(groupMsg);
}
public List<Message> getAllGroupMsg(){
	List<Message> messages=new ArrayList<>();
	List<GroupMessage> groupMsgs=groupMsgService.getAll();
	for(GroupMessage groupMsg:groupMsgs){
		messages.add(new Message(groupMsg));
	}
	return messages;
}
}
