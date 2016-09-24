package com.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.client.ClientThread;
import com.entity.User;
import com.ui.GroupChatFrame;
import com.ui.LoginFrame;
import com.ui.RegisterFrame;
import com.ui.SingleChatFrame;
import com.util.MessageUtil;
import com.vo.Message;
import com.vo.VoUser;

import constant.DataType;

/**
 * 客户端线程与窗口之间的控制转发器
 * 
 * @author Administrator
 *
 */
public class TcpController {
	private ClientThread client;
	private GroupChatFrame groupChatFrame;
	private LoginFrame loginFrame;
	private RegisterFrame registerFrame;
	private Map<String,SingleChatFrame> singleChatFrames;
	private User user;
	public TcpController(LoginFrame loginFrame){
		this.loginFrame=loginFrame;
		singleChatFrames=new HashMap<>();	
	}
	public void launchClient(User user) {
		this.user=user;
		client = new ClientThread("localhost", 8000, user, this);	
	}
   public void setRegisterFrame(RegisterFrame registerFrame){
	   this.registerFrame=registerFrame;
   }
	public void login() {
		User user=client.getUser();
		Message msg = new Message();
		msg.setType(DataType.LOGIN);
		msg.setContent(user.getPassword());
		msg.setFromUser(user.getUserid());
		try {
			client.sendMessage(msg);
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	}
	public void register() {
		User user=client.getUser();
		Message msg = new Message();
		msg.setType(DataType.REGISTER);
		msg.setContent(user.getPassword());
		msg.setFromUser(user.getUserid());
		try {
			client.sendMessage(msg);
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	}
	public void handleLoginResult() {
		String str=client.getMessage().getContent();
		if(str.equals("repeat")||str.equals("failure"))
		 	{
			  loginFrame.notice(str);
			  try {
				closeSocketChannel();
			} catch (IOException e) {
				e.printStackTrace();
			}
		 	}
		else
		{
			loginFrame.dispose();
			groupChatFrame = new GroupChatFrame(this);
			loadUserList(str);
		}
	}
  public void handleRegisterResult(){
	  String str=client.getMessage().getContent();
      registerFrame.notice(str);
	   
  }
	/**
	 * 注销用户
	 */
	public void logOut() {
		User user=client.getUser();
		Message msg = new Message();
		msg.setType(DataType.LOGOUT);
		msg.setContent(user.getPassword());
		msg.setFromUser(user.getUserid());
		try {
			client.sendMessage(msg);
			closeSocketChannel();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void loadUserList(String list) {
	
		String[] userList = MessageUtil.formUserList(list);
		groupChatFrame.loadUserList(userList);
	}
	public void LoadFriendList(String list) {
		String[] friendlist=MessageUtil.formUserList(list); 
		groupChatFrame.loadFriendList(friendlist);
		
	}
	public void userLogin(String user) {
		groupChatFrame.appendUser(user);
		
	}
	public void userLogout(String user) {
		groupChatFrame.deleteUser(user);
	}

	public void newGroupMsg(Message message) {
       groupChatFrame.appendMessage(message);
	}

	public void newFriendMsg(Message message) {
		if(singleChatFrames.get(message.getFromUser())==null)
			{
			  singleChatFrames.put(message.getFromUser(),new SingleChatFrame(message.getFromUser(),this));
			}
       singleChatFrames.get(message.getFromUser()).appendMessage(message);
	}
	public  void sendMessage(Message msg) {
		msg.setFromUser(client.getUser().getUserid());
		try {
			client.sendMessage(msg);
		} catch (IOException e) {
			System.out.println("发送信息出现错误！");
			e.printStackTrace();
		}
		
	}

	public void addFriend(String friend) {
	if(friend.equals(client.getUser().getUserid()))return;
	    Message message=new Message();
	    message.setFromUser(client.getUser().getUserid());
	    message.setToUser(friend);
	    message.setType(DataType.ADD_FRIEND);
	   try {
		client.sendMessage(message);
	} catch (IOException e) {
		e.printStackTrace();
	}
	}
	public void delFriend(String friend) {
		  Message message=new Message();
		   message.setFromUser(client.getUser().getUserid());
		   message.setToUser(friend);
		   message.setType(DataType.DEL_FRIEND);
		   try {
				client.sendMessage(message);
			} catch (IOException e) {
				e.printStackTrace();
			}

	}
	public void appendFriend(String friend){
		groupChatFrame.appendFriend(friend);
	}
	public void removeFriend(String friend){
		if(singleChatFrames.containsKey(friend))
		{
			singleChatFrames.get(friend).dispose();
			singleChatFrames.remove(friend);
		}
		groupChatFrame.deleteFriend(friend);
	}
	public Map<String, SingleChatFrame> getSingleChatFrames() {
		return singleChatFrames;
	}
	
	public User getUser() {
		return user;
	}
	private void closeSocketChannel() throws IOException {
		client.getSocket().socket().close();
		client.getSocket().close();
		client = null;
	}
	
}
