package tests.connectiontests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import java.util.concurrent.Semaphore;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import api.PSPort;
import api.PSPortFactory;
import connection.Listener;
import connection.ListenerFactory;

public class TestSSLListener {
	
	private PSPort port;
	private Listener serverSocket;
	private Semaphore semaphore;
	
	@Before
	public void testAccept() throws Throwable {
		semaphore = new Semaphore(0);
		serverSocket = ListenerFactory.getListener(new String[]{"SSLListener"});
		
		new Thread() {
			public void run() {
				try {
					semaphore.release();
					serverSocket.accept();
				} catch(Exception e) {}
			}
		}.start();
		
		semaphore.acquire();
		Thread.sleep(1000);
		port = PSPortFactory.getPort("PSPortSSL");
	}
	
	@Test
	public void testGetLastClientId() throws Throwable {
		assertEquals("Wrong ClientId"
				,"CN=Broker"
				,serverSocket.getLastClientId());
	}
	
	@After
	public void testClose() throws Throwable {
		assertFalse("Listener prematurely closed",serverSocket.isClosed());
		serverSocket.close();
		assertTrue("Listener not closed",serverSocket.isClosed());
		port.disconnect();
	}
	
}
