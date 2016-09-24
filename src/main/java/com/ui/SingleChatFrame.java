package com.ui;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import com.controller.TcpController;
import com.util.FrameUtil;
import com.util.TimerUtils;
import com.vo.Message;
import com.vo.VoUser;

import constant.DataType;

public class SingleChatFrame extends JFrame{
private JButton btn_send;
private JButton btn_record;
private JTextArea chat_content;
private JTextArea send_content;
private TcpController tcpController;
private String user;

public SingleChatFrame(String user,TcpController tcpController){
	this.user=user;
	this.tcpController=tcpController;
	this.initComponent();
	this.addAction();
}
private void addAction() {
	btn_send.addActionListener(new ActionListener(){
		@Override
		public void actionPerformed(ActionEvent e) {
			Message message=new Message();
			message.setContent(send_content.getText());
			message.setType(DataType.SINGLE);
			message.setToUser(user);
			message.setTime(TimerUtils.getCurrentTime());
			tcpController.sendMessage(message);
			chat_content.append("我    "+message.getTime()+"\n"+message.getContent()+"\n");
			send_content.setText("");
		}});
	this.addWindowListener(new WindowAdapter(){
		@Override
		public void windowClosing(WindowEvent e){
			tcpController.getSingleChatFrames().remove(user);
			dispose();
		}
	});
	
}
private void initComponent() {
	btn_send=new JButton("发送");
	btn_record=new JButton("查看记录");
	chat_content=new JTextArea();
	send_content=new JTextArea();
	chat_content.setEditable(false);
	chat_content.setLineWrap(true);
	chat_content.setWrapStyleWord(true);
	send_content.setLineWrap(true);
	send_content.setWrapStyleWord(true);
	JScrollPane chatPane=new JScrollPane(chat_content);
	JScrollPane sendPane=new JScrollPane(send_content);
	JPanel btnPane=new JPanel();
	btnPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
	btnPane.add(btn_send);
	btnPane.add(btn_record);
	chatPane.setBounds(10,10,500,350);
	sendPane.setBounds(10,370,500,100);
	btnPane.setBounds(10,470,500,50);
	this.setLayout(null);
	add(chatPane);
	add(sendPane);
	add(btnPane);
	this.setTitle(user.toString());
	this.setSize(580,590);
	this.setResizable(false);
	FrameUtil.setFrameCenter(this);
	this.setVisible(true);
    this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
}
public void appendMessage(Message message){
	chat_content.append(message.getFromUser()+"   "+message.getTime()+"\n"+message.getContent()+"\n");
}

}
