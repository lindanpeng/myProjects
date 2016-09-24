package com.ui;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Map;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.TitledBorder;

import com.controller.TcpController;
import com.util.FrameUtil;
import com.util.TimerUtils;
import com.vo.Message;
import com.vo.VoUser;

import constant.DataType;

public class GroupChatFrame extends JFrame {

private static final long serialVersionUID = 1L;
private JButton btn_record;
private JButton btn_send;
private JTextArea chat_content;
private JTextArea send_content;
private DefaultListModel<VoUser> userListModel;
private DefaultListModel<VoUser> friendListModel;
private JList<VoUser> userList;
private JList<VoUser> friendList;
private JPopupMenu ulmenu;
private JPopupMenu flmenu;
private JMenuItem addFriend;
private JMenuItem delFriend;
private JMenuItem toChat;
private TcpController tcpController;
public GroupChatFrame(TcpController tcpController){
	this.initComponent();
	this.addAction();
	this.tcpController=tcpController;
}
private void initComponent(){
	userListModel=new DefaultListModel<>();
	friendListModel=new DefaultListModel<>();
	btn_send=new JButton("发送");
	btn_record=new JButton("查看记录");
	chat_content=new JTextArea();
	send_content=new JTextArea();
	chat_content.setEditable(false);
	chat_content.setLineWrap(true);
	chat_content.setWrapStyleWord(true);
	send_content.setLineWrap(true);
	send_content.setWrapStyleWord(true);
	userList=new JList<VoUser>(userListModel);
	friendList=new JList<VoUser>(friendListModel);
	ulmenu=new JPopupMenu();
	flmenu=new JPopupMenu();
	addFriend=new JMenuItem("添加好友");
	delFriend=new JMenuItem("删除好友");
	toChat=new JMenuItem("私人聊天");
	ulmenu.add(addFriend);
	flmenu.add(delFriend);
	flmenu.add(toChat);
	JPanel btnPane=new JPanel();
	JScrollPane ulPane=new JScrollPane(userList);
	JScrollPane flPane=new JScrollPane(friendList);
	JScrollPane chatPane=new JScrollPane(chat_content);
	JScrollPane sendPane=new JScrollPane(send_content);
	ulPane.setBorder(new TitledBorder("在线用户"));
	flPane.setBorder(new TitledBorder("好友列表"));
	//组件与组件之间的间隔为10
	chatPane.setBounds(10,10,400,300);
	sendPane.setBounds(10,320,400,150);
	ulPane.setBounds(420,10,100,300);
	flPane.setBounds(420,320,100,150);
	btnPane.setBounds(10,470,400,50);
	btnPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
	btnPane.add(btn_send);
	btnPane.add(btn_record);
	JPanel main=new JPanel();
	main.setLayout(null);
	main.add(chatPane);
	main.add(sendPane);
	main.add(ulPane);
	main.add(flPane);
	main.add(btnPane);
	this.setContentPane(main);
	this.setTitle("群聊");
	this.setSize(590,590);
	this.setResizable(false);
	this.setVisible(true);
    FrameUtil.setFrameCenter(this);	
	setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE); 
}
private void addAction(){
   btn_send.addActionListener(new ActionListener(){
	@Override
	public void actionPerformed(ActionEvent e) {
		Message msg=new Message();
		msg.setContent(send_content.getText());
		msg.setType(DataType.GROUP);
		msg.setTime(TimerUtils.getCurrentTime());
		tcpController.sendMessage(msg);
		send_content.setText("");
	}});
   this.addWindowListener(new WindowAdapter(){
@Override
public void windowClosing(WindowEvent e){
	int n = JOptionPane.showConfirmDialog(null, "确认退出群聊吗?", "确认对话框", JOptionPane.YES_NO_OPTION);   
	if (n == JOptionPane.YES_OPTION) {   
		tcpController.logOut();
		System.exit(0);
	} 
}
   });
   userList.addMouseListener(new MouseAdapter(){
  @Override
  public void mouseClicked(MouseEvent e){
	   if (e.getButton()==MouseEvent.BUTTON3) {
		     //弹出右键菜单
		    if (userList.getSelectedIndex()!=-1) {       
	            //获取选择项的值
	            ulmenu.show(e.getComponent(),e.getX(), e.getY());
	           }
		    }
  }
   });
 friendList.addMouseListener(new MouseAdapter(){
	 @Override
	  public void mouseClicked(MouseEvent e){
		   if (e.getButton()==MouseEvent.BUTTON3) {
			     //弹出右键菜单
			    if (friendList.getSelectedIndex()!=-1) {       
		            //获取选择项的值
		            flmenu.show(e.getComponent(),e.getX(), e.getY());
		           }
			    }
	 }
 });
   addFriend.addActionListener(new ActionListener(){

	@Override
	public void actionPerformed(ActionEvent e) {
		tcpController.addFriend(((VoUser)userList.getSelectedValue()).getUserid());
	}
	   
   });
   delFriend.addActionListener(new ActionListener(){

	@Override
	public void actionPerformed(ActionEvent e) {
		tcpController.delFriend(((VoUser)friendList.getSelectedValue()).getUserid());
		
	}
	   
   });
   toChat.addActionListener(new ActionListener(){

	@Override
	public void actionPerformed(ActionEvent e) {
     Map<String,SingleChatFrame> map=tcpController.getSingleChatFrames();
     String user=((VoUser)friendList.getSelectedValue()).getUserid();
     if(!map.containsKey(user)){
    	 map.put(user,new SingleChatFrame(user,tcpController));
     }
	}
	   
   });
}

public void appendMessage(Message message){
	chat_content.append(message.getFromUser()+"   "+message.getTime()+"\n"+message.getContent()+"\n");
}
public void appendUser(String user) {
    VoUser u=new VoUser(user);
    int index=friendListModel.indexOf(u);
    if(index!=-1){
    	friendListModel.getElementAt(index).setIsOnline(true);
    }
	userListModel.addElement(u);
}
public void deleteUser(String user){
	 VoUser u=new VoUser(user);
	    int index=friendListModel.indexOf(u);
	    if(index!=-1){
	    	friendListModel.getElementAt(index).setIsOnline(false);
	    }
	userListModel.removeElement(new VoUser(user));
}
public void appendFriend(String user) {
	VoUser friend=new VoUser(user);
	if(!userListModel.contains(friend))
		friend.setIsOnline(false);
	friendListModel.addElement(friend);
}
public void deleteFriend(String user){
	friendListModel.removeElement(new VoUser(user));
}
public void changeFriendState(){
	
}
public void loadUserList(String[] userlist){
	
	for(int i=0;i<userlist.length;i++){
		if(userlist[i].equals(tcpController.getUser().getUserid()))
			userlist[i]+="(我)";
		  appendUser(userlist[i]);
	}
}
public void loadFriendList(String[] friendlist) {
	if(friendlist[0].equals(""))return;
	for(int i=0;i<friendlist.length;i++){
		appendFriend(friendlist[i]);
	}
}
public static void main(String[] args){
	
	new GroupChatFrame(new TcpController(new LoginFrame()));
}
}
