package tests.managertests;

import static org.junit.Assert.assertEquals;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import manager.MessagesManager;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import connection.Connection;
import connection.SocketConnection;
import data.MessagePublication;
import data.MessagePublish;
import data.MessageSubscribe;
import data.MessageUnsubscribe;

public class TestMessagesManager {
	
	private ServerSocket serverSocket;
	private Socket socket2;
	
	private SocketConnection connection1, connection2;
	private Map<Connection,List<String>> subscriptions;
	
	private MessagesManager manager;

	@Before
	public void init() throws Throwable {
		Socket socket1;
		serverSocket = new ServerSocket(5434);
		new Thread() {
			public void run() {
				try {
					socket2 = serverSocket.accept();
				} catch (Exception e) {}
			}
		}.start();
		socket1 = new Socket("127.0.0.1",5434);
		
		connection1 = new SocketConnection();
		connection2 = new SocketConnection();
		connection1.setConnectionId("1");
		connection2.setConnectionId("2");
		connection1.init(socket1, 10);
		connection2.init(socket2, 10);
		
		subscriptions = Collections.synchronizedMap(new HashMap<Connection,List<String>>());
		subscriptions.put(connection2, new ArrayList<String>());
		
		manager = new MessagesManager(connection2,subscriptions);
		manager.start();
	}
	
	@Test
	public void testManageMessagePublish() throws Throwable {
		connection1.writeMessage(new MessageSubscribe("Topic"));
		Thread.sleep(1500);
		connection1.writeMessage(new MessagePublish("Topic","Data"));
		MessagePublication response = (MessagePublication)connection1.readMessage();
		assertEquals("Wrong MessagePublication from MessagesManager","Topic",response.getTopic());
		assertEquals("Wrong MessagePublication from MessagesManager","Data",response.getDataObject());
	}
	
	@Test
	public void testManageMessagePublication() throws Throwable {
		connection1.writeMessage(new MessagePublication(new MessagePublish("Topic","Data"),"Sender",0));
		Thread.sleep(1500);
	}
	
	@Test
	public void testManageMessageSubscribe() throws Throwable {
		connection1.writeMessage(new MessageSubscribe("A","B"));
		Thread.sleep(400);
		connection1.writeMessage(new MessageSubscribe("C","D"));
		Thread.sleep(1500);
		List<String> subsConnection2 = subscriptions.get(connection2);
		assertEquals("Subscription failed","A",subsConnection2.get(0));
		assertEquals("Subscription failed","B",subsConnection2.get(1));
		assertEquals("Subscription failed","C",subsConnection2.get(2));
		assertEquals("Subscription failed","D",subsConnection2.get(3));
	}
	
	@Test
	public void testManageMessageUnsubscribe() throws Throwable {
		connection1.writeMessage(new MessageSubscribe("AC","DC"));
		Thread.sleep(1500);
		List<String> subsConnection2 = subscriptions.get(connection2);
		assertEquals("Subscription failed","AC",subsConnection2.get(0));
		assertEquals("Subscription failed","DC",subsConnection2.get(1));
		connection1.writeMessage(new MessageUnsubscribe("AC","DCD"));
		Thread.sleep(1500);
		subsConnection2 = subscriptions.get(connection2);
		assertEquals("Unsubscription failed",1,subsConnection2.size());
		assertEquals("Unsubscription failed","DC",subsConnection2.get(0));
	}
	
	@After
	public void cleanup() throws Throwable {
		connection1.close();
		connection2.close();
		serverSocket.close();
		Thread.sleep(3000);
		manager.interrupt();
		manager.join();
	}
	
}
