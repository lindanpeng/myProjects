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

public class LoginFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	private TcpController tcpController;
	private JButton btn_login;
	private JButton btn_register;
	private JTextField jt_id;
	private JPasswordField jt_pw;
	public LoginFrame() {
		tcpController =new TcpController(this);
		this.initComponent();
		this.addAction();

	}

	private void initComponent() {
		btn_login = new JButton("登录");
		btn_register = new JButton("注册");
		jt_id = new JTextField(20);
		jt_pw = new JPasswordField(20);
		JLabel id_tip = new JLabel("用户名：");
		JLabel pw_tip = new JLabel("密码：");
		JPanel contentPanel = new JPanel();
		JPanel btnPanel = new JPanel();
		JPanel idPanel = new JPanel();
		JPanel pwPanel = new JPanel();
		pwPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		idPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		btnPanel.setLayout(new FlowLayout());
		idPanel.add(id_tip);
		idPanel.add(jt_id);
		pwPanel.add(pw_tip);
		pwPanel.add(jt_pw);
		btnPanel.add(btn_login);
		btnPanel.add(btn_register);
		GridLayout grid = new GridLayout(3, 1);
		contentPanel.setLayout(grid);
		contentPanel.add(idPanel);
		contentPanel.add(pwPanel);
		contentPanel.add(btnPanel);
		this.setContentPane(contentPanel);
		FrameUtil.setFrameCenter(this);
		this.setTitle("登录");
		this.setSize(250, 200);
		this.setVisible(true);
		this.setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	private void addAction() {
		btn_login.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				/*
				 * 判断账号密码是否为空
				 */
				String userid = jt_id.getText().trim();
				String password =new String(jt_pw.getPassword()); 
				if (userid.equals("")) {
					Toolkit.getDefaultToolkit().beep();
					JOptionPane.showMessageDialog(null, "请输入用户名！", "WARNING_MESSAGE", JOptionPane.WARNING_MESSAGE);
					return;
				}
				if (password.equals("")) {
					Toolkit.getDefaultToolkit().beep();
					JOptionPane.showMessageDialog(null, "请输入密码！", "WARNING_MESSAGE", JOptionPane.WARNING_MESSAGE);
					return;
				}
				User user = new User(userid, password);
				tcpController.launchClient(user);
				tcpController.login();
	
			}
		});
		btn_register.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				new RegisterFrame(tcpController);
			}
		});
	}
   public void notice(String result){
		 if (result.equals("repeat")) {
			Toolkit.getDefaultToolkit().beep();
			JOptionPane.showMessageDialog(null, "请勿重复登录！", "WARNING_MESSAGE", JOptionPane.WARNING_MESSAGE);
		}
		else {
			Toolkit.getDefaultToolkit().beep();
			JOptionPane.showMessageDialog(null, "账号或密码错误！", "WARNING_MESSAGE", JOptionPane.WARNING_MESSAGE);
		}
	 }
	public static void main(String[] args) {

		 try {
			org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper.launchBeautyEyeLNF();
		} catch (Exception e) {
	
			e.printStackTrace();
		}
		new LoginFrame();

	}
}
