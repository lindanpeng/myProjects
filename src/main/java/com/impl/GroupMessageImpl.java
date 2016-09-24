package com.impl;

import java.util.List;

import org.hibernate.Session;
import org.springframework.stereotype.Component;

import com.entity.GroupMessage;
@Component("groupMessageImpl")
public class GroupMessageImpl extends BaseDaoImpl<GroupMessage> {
public List<GroupMessage> getAll(){
	Session session=sessionFactory.getCurrentSession();
	List<GroupMessage> messages=session.createQuery("from GroupMessage").list();
	return messages;
}
}
