package com.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.entity.GroupMessage;
import com.impl.GroupMessageImpl;
@Component("groupMsgService")
public class GroupMsgService {
private GroupMessageImpl groupMessageImpl;

public GroupMessageImpl getGroupMessageImpl() {
	return groupMessageImpl;
}
@Resource
public void setGroupMessageImpl(GroupMessageImpl groupMessageImpl) {
	this.groupMessageImpl = groupMessageImpl;
}

public void add(GroupMessage groupMsg){
	this.groupMessageImpl.add(groupMsg);
}
public List<GroupMessage> getAll(){
	return this.groupMessageImpl.getAll();
}
}
