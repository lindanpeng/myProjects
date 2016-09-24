package com.util;

import com.vo.Message;

public class MessageUtil {
public static String formMessage(Message message){
	String str="";
	return str;
}
public static  String[] formUserList(String userList){
  String[] list=userList.substring(1,userList.length()-1).split(", ");
  return list;
}
}
