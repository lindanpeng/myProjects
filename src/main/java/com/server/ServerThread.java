package com.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.controller.MessageController;
import com.controller.UserController;
import com.entity.User;
import com.util.SerializeUtils;
import com.vo.Message;

import constant.Const;
import constant.DataType;

public class ServerThread implements Runnable {
	private Map<String, SocketChannel> users;
	private Selector selector;
	private ServerSocketChannel serverSocket;
	private UserController userController;
	private MessageController messageController;
	private Message message;

	public ServerThread(int port) {
		try {
			ClassPathXmlApplicationContext c = new ClassPathXmlApplicationContext("applicationContext.xml");
			userController = (UserController) c.getBean("userController");
			messageController=(MessageController)c.getBean("messageController");
			users = new HashMap<>();
			selector = Selector.open();
			serverSocket = ServerSocketChannel.open().bind(new InetSocketAddress(port));
			serverSocket.configureBlocking(false);
			serverSocket.register(selector, SelectionKey.OP_ACCEPT);

		} catch (IOException e) {

			e.printStackTrace();
		}
	}
	// TODO 为了方便

	@Override
	public void run() {
		while (true) {
			try {
				if (selector.select() <= 0)//select方法为阻塞方法
					continue;
			} catch (IOException e) {
				e.printStackTrace();
			}
/*			System.out.println("aaaaaaaaa");*/
			Iterator<SelectionKey> it = selector.selectedKeys().iterator();
			while (it.hasNext()) {
				SelectionKey key = it.next();
				try {
					if (key.isAcceptable())
						handleAccpet(key);
					if (key.isValid() && key.isReadable())// 通道什么时候为可读态
						handleRead(key);
				} catch (IOException e) {
					System.out.println("客户端通道已被关闭，无法进行读取！");
					try {
						key.channel().close();
						key.cancel();
					} catch (IOException e1) {
						e1.printStackTrace();
					}

				} finally {
					it.remove();
				}
			}
		}
	}

	private void handleRead(SelectionKey key) throws IOException {
		System.out.println("有新的可读通道!");
		SocketChannel client = (SocketChannel) key.channel();
		ByteBuffer buf = ByteBuffer.allocate(Const.BSIZE);
		buf.clear();
		int state = client.read(buf);// TODO 如果我的缓冲区过小怎么把它组装在一起？使用byte数组
										// 抛出异常在run方法中处理
		buf.flip();
		if (state == -1) {
			client.close();

		} else {
			message = (Message) SerializeUtils.ByteToObject(buf.array());
			DataType type = message.getType();
			switch (type) {
			case GROUP:
				messageController.saveGroupMsg(message);
				Iterator<SocketChannel> it1 = users.values().iterator();
				while (it1.hasNext()) {
					it1.next().write(ByteBuffer.wrap(SerializeUtils.ObjectToByte(message)));
				}
				break;
			case SINGLE:
				SocketChannel friend = users.get(message.getToUser());
				if (friend != null) {
					message = (Message) SerializeUtils.ByteToObject(buf.array());
					friend.write(ByteBuffer.wrap(SerializeUtils.ObjectToByte(message)));
				}
				break;
			case LOGIN:
				// 验证错误
				if (!userController.valid(new User(message.getFromUser(), message.getContent()))) {
					message.setType(DataType.LOGIN_RESULT);
					message.setContent("failure");
					client.write(ByteBuffer.wrap(SerializeUtils.ObjectToByte(message)));
					client.close();
				}
				
				else {
					// 重复登录
					if (users.containsKey(message.getFromUser())) {
						message.setType(DataType.LOGIN_RESULT);
						message.setContent("repeat");
						client.write(ByteBuffer.wrap(SerializeUtils.ObjectToByte(message)));
						client.close();
					}
					// 添加用户
					else {
						message.setType(DataType.LOGIN);
						Iterator<SocketChannel> it = users.values().iterator();
						SocketChannel socket = null;
						while (it.hasNext()) {
							socket = it.next();
							socket.write(ByteBuffer.wrap(SerializeUtils.ObjectToByte(message)));
						}
						users.put(message.getFromUser(), client);
						sendUserList(client);
						sendFriendList(client, message.getFromUser());
						List<Message> messages=messageController.getAllGroupMsg();//太快了导致消息被覆盖？
						try {
							Thread.sleep(200);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						for(Message message:messages){
							client.write(ByteBuffer.wrap(SerializeUtils.ObjectToByte(message)));
							try {
								Thread.sleep(100);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
					}
				}
				break;
			case LOGOUT:
				users.remove(message.getFromUser());
				client.close();
				Iterator<SocketChannel> it = users.values().iterator();
				while (it.hasNext()) {
					it.next().write(ByteBuffer.wrap(SerializeUtils.ObjectToByte(message)));
				}
				break;
			case REGISTER:
				message.setType(DataType.REGISTER_RESULT);
				if (userController.register(new User(message.getFromUser(), message.getContent())))
					message.setContent("success");
				else
					message.setContent("failure");
				client.write(ByteBuffer.wrap(SerializeUtils.ObjectToByte(message)));
				client.close();
				break;
			case ADD_FRIEND:
				if (userController.addFriend(message.getFromUser(), message.getToUser())) {

					message.setType(DataType.FRIEND_RESULT);
					message.setContent("add");
					users.get(message.getToUser()).write(ByteBuffer.wrap(SerializeUtils.ObjectToByte(message)));
					message.setFromUser(message.getToUser());
					client.write(ByteBuffer.wrap(SerializeUtils.ObjectToByte(message)));
				}
				break;
			case DEL_FRIEND:
				userController.deleteFriend(message.getFromUser(), message.getToUser());
				message.setType(DataType.FRIEND_RESULT);
				message.setContent("remove");
				SocketChannel frd = users.get(message.getToUser());
				if (frd != null)
					frd.write(ByteBuffer.wrap(SerializeUtils.ObjectToByte(message))); 
				message.setFromUser(message.getToUser());
				client.write(ByteBuffer.wrap(SerializeUtils.ObjectToByte(message)));

				break;
			default:
				break;
			}
		}

	}

	private void sendUserList(SocketChannel client) throws IOException {
		List<String> user = new ArrayList<>(users.keySet());
		message.setType(DataType.LOGIN_RESULT);
		message.setContent(user.toString());
		client.write(ByteBuffer.wrap(SerializeUtils.ObjectToByte(message)));
	}

	private void sendFriendList(SocketChannel client, String user) throws IOException {
		List<String> friends = this.userController.getFriendList(user);
		message.setContent(friends.toString());
		message.setType(DataType.FRIEND_LIST);
		client.write(ByteBuffer.wrap(SerializeUtils.ObjectToByte(message)));
	}

	private void handleAccpet(SelectionKey key) throws IOException {
		System.out.println("有新的连接！");
		SocketChannel socket = ((ServerSocketChannel) key.channel()).accept();
		// 设置为非阻塞模式
		socket.configureBlocking(false);
		socket.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);

	}

	public static void main(String[] args) {
		new Thread(new ServerThread(8000)).start();
	}
}
