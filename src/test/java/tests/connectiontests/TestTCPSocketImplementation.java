package tests.connectiontests;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import api.PSPort;
import api.PSPortFactory;
import connection.Listener;
import connection.ListenerFactory;

public class TestTCPSocketImplementation {

	private PSPort port;
	private Listener serverSocket;

	@Before
	public void testAccept() throws Throwable {
		serverSocket = ListenerFactory.getListener(new String[]{"TCPListener"});
		new Thread() {
			public void run() {
				try {
					serverSocket.accept();
				} catch(Exception e) {}
			}
		}.start();
		port = PSPortFactory.getPort("PSPortTCP");
	}
	
	@Test
	public void testGetLastClientId() throws Throwable {
		assertTrue("Wrong ClientId "+serverSocket.getLastClientId(),serverSocket.getLastClientId().startsWith("/127.0.0.1:"));
	}
	
	@After
	public void testClose() {
		assertFalse("Listener prematurely closed",serverSocket.isClosed());
		serverSocket.close();
		assertTrue("Listener not closed",serverSocket.isClosed());
		port.disconnect();
	}
	
}
