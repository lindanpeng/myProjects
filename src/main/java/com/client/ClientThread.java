package com.client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

import com.controller.TcpController;
import com.entity.User;
import com.util.MessageUtil;
import com.util.SerializeUtils;
import com.vo.Message;

import constant.Const;
import constant.DataType;

public class ClientThread implements Runnable {
	private Selector selector;
	private SocketChannel socket;
	private Message message;
	private TcpController tcpController;
	private User user;

	public ClientThread(String ip, int port, User user, TcpController tcpController) {
		try {
			this.tcpController = tcpController;
			this.user = user;
			selector = Selector.open();
			socket = SocketChannel.open(new InetSocketAddress(ip, port));
			socket.configureBlocking(false);
			socket.register(selector, SelectionKey.OP_READ, user);
			new Thread(this).start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		while (true) {
			try {

				if (selector.select() <= 0)
					continue;
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			Iterator<SelectionKey> it = selector.selectedKeys().iterator();
			while (it.hasNext()) {
				SelectionKey key = it.next();
				try {
					if (key.isReadable())
						handleRead(key);
				} catch (Throwable e) {
					e.printStackTrace();

				} finally {

					it.remove();
				}

			}

		}

	}

	private void handleRead(SelectionKey key) throws IOException {
		SocketChannel client = (SocketChannel) key.channel();
		ByteBuffer buf = ByteBuffer.allocate(Const.BSIZE);
		buf.clear();
		int state = client.read(buf);// TODO 如果我的缓冲区过小怎么把它组装在一起？使用byte数组叠加？
		buf.flip();
		if (state == -1) {
			client.close();
		} else {
			message = (Message) SerializeUtils.ByteToObject(buf.array());
			DataType type = message.getType();
			System.out.println(type);
			switch (type) {
			// 通知控制器有新用户登陆
			case LOGIN:
				tcpController.userLogin(message.getFromUser());
				break;
			case GROUP:
				tcpController.newGroupMsg(message);
				break;
			case SINGLE:
				tcpController.newFriendMsg(message);
				break;
			case LOGOUT:
				tcpController.userLogout(message.getFromUser());
				break;
			case FRIEND_LIST:
				tcpController.LoadFriendList(message.getContent());
				break;
			case FRIEND_RESULT:
				if(message.getContent().equals("add"))
				tcpController.appendFriend(message.getFromUser());
				else
				tcpController.removeFriend(message.getFromUser());
				break;
			case LOGIN_RESULT:
				tcpController.handleLoginResult();
				break;	
			case REGISTER_RESULT:
				tcpController.handleRegisterResult();
			default:
				break;
			}
		}

	}

	// 不需要使用isWriteable()吗
	public void sendMessage(Message message) throws IOException {
		byte[] bytes = SerializeUtils.ObjectToByte(message);
		ByteBuffer buffer = ByteBuffer.wrap(bytes);
		socket.write(buffer);
	}

	public SocketChannel getSocket() {
		return socket;
	}

	public Message getMessage() {
		return message;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
}