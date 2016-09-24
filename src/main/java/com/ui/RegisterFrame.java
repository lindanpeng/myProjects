package com.ui;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.controller.TcpController;
import com.entity.User;
import com.util.FrameUtil;

public class RegisterFrame extends JFrame {
private TcpController tcpController;
private JButton btn_submit;
private JTextField jt_id;
private JPasswordField jt_pw;
private JPasswordField jt_confirm;
public  RegisterFrame(TcpController tcpController){
	this.initComponent();
	this.addAction();
	this.tcpController=tcpController;
}
private void initComponent(){
	btn_submit=new JButton("提交");
	jt_id=new JTextField(20);
	jt_pw=new JPasswordField(20);
	jt_confirm=new JPasswordField(20);
	JLabel id_tip=new JLabel("用户名：");
	JLabel pw_tip=new JLabel("密码：");
	JLabel confrm_tip=new JLabel("确认密码：");
	JPanel main=new JPanel();
	JPanel idPanel=new JPanel();
	JPanel pwPanel=new JPanel();
	JPanel conPanel=new JPanel();
	JPanel btnPanel=new JPanel();
	idPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
	idPanel.add(id_tip);
	idPanel.add(jt_id);
	pwPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
	pwPanel.add(pw_tip);
	pwPanel.add(jt_pw);
	conPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
	conPanel.add(confrm_tip);
	conPanel.add(jt_confirm);
	btnPanel.add(btn_submit);
	main.setLayout(new GridLayout(4,1));
	main.add(idPanel);
	main.add(pwPanel);
	main.add(conPanel);
	main.add(btnPanel);
	this.setContentPane(main);
	this.setTitle("注册界面");
	this.setSize(260,250);
	this.setVisible(true);
	this.setResizable(false);
	FrameUtil.setFrameCenter(this);
	setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); 
}
public void addAction(){
	this.btn_submit.addActionListener(new ActionListener(){

		@Override
		public void actionPerformed(ActionEvent e) {
			String userid=jt_id.getText().trim();
			String password=jt_pw.getText().trim();
			String confirm=jt_confirm.getText().trim();
			if(userid.equals("")){
				Toolkit.getDefaultToolkit().beep();
				JOptionPane.showMessageDialog(null, "用户名不能为空！", "WARNING_MESSAGE", JOptionPane.WARNING_MESSAGE);
				return;
			}
			if(password.equals("")){
				Toolkit.getDefaultToolkit().beep();
				JOptionPane.showMessageDialog(null, "密码不能为空！", "WARNING_MESSAGE", JOptionPane.WARNING_MESSAGE);
				return;
			}
			if(!confirm.equals(password)){
				Toolkit.getDefaultToolkit().beep();
				JOptionPane.showMessageDialog(null, "两次密码不一致！", "WARNING_MESSAGE", JOptionPane.WARNING_MESSAGE);
				return;
			}
			User user =new User(userid,password);
			tcpController.setRegisterFrame(RegisterFrame.this);
			tcpController.launchClient(user);
	        tcpController.register();
			}
		
	});
}
public void notice(String result){
	if(result.equals("failure"))
	{
		Toolkit.getDefaultToolkit().beep();
		JOptionPane.showMessageDialog(null, "该用户已经存在！", "WARNING_MESSAGE", JOptionPane.WARNING_MESSAGE);
	}
	else{
		JOptionPane.showMessageDialog(null, "注册成功！", "WARNING_MESSAGE", JOptionPane.INFORMATION_MESSAGE);
		dispose();
	}
}
}
