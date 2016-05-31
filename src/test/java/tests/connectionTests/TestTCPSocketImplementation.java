package tests.connectionTests;

import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import api.PSPort;
import api.PSPortFactory;
import connection.SocketImplementation;
import connection.SocketImplementationFactory;

public class TestTCPSocketImplementation {

	PSPort port;
	SocketImplementation serverSocket;

	@Before
	public void testAccept() throws Throwable {
		serverSocket = SocketImplementationFactory.getSocketImplementation(new String[]{"TCPSocketImplementation"});
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
		assertTrue("Wrong ClientId",serverSocket.getLastClientId().startsWith("/127.0.0.1:"));
	}
	
	@After
	public void testClose() {
		assertTrue("SocketImplementation prematurely closed",!serverSocket.isClosed());
		serverSocket.close();
		assertTrue("SocketImplementation not closed",serverSocket.isClosed());
		port.disconnect();
	}
	
}
