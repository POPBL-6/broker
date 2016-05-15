import static org.junit.Assert.*;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import connection.SocketConnection;
import data.MessagePublish;

public class TestSocketConnection {

	SocketConnection connection1, connection2;
	
	ServerSocket serverSocket;
	Socket socket1, socket2;
	
	@Before
	public void init() throws Exception {
		serverSocket = new ServerSocket(5434);
		new Thread() {
			public void run() {
				try {
					socket2 = serverSocket.accept();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}.start();
		socket1 = new Socket("127.0.0.1",5434);
		connection1 = new SocketConnection();
		connection2 = new SocketConnection();
		connection1.init(socket1, 10);
		connection2.init(socket2, 10);
	}
	
	@Test
	public void testSendAndReceiveMessage() throws Exception {
		MessagePublish message1 = new MessagePublish("Topic","Data");
		connection1.writeMessage(message1);
		MessagePublish message2 = (MessagePublish) connection2.readMessage();
		assertEquals("testSendAndReceiveMessage wrong charset",message1.getCharset(),message2.getCharset());
		assertEquals("testSendAndReceiveMessage wrong topic",message1.getTopic(),message2.getTopic());
		assertArrayEquals("testSendAndReceiveMessage wrong data",message1.getData(),message2.getData());
	}
	
	@After
	public void cleanUp() throws Exception {
		connection1.close();
		connection2.close();
		connection1 = null;
		connection2 = null;
		socket1 = null;
		socket2 = null;
		serverSocket = null;
	}
	
}
