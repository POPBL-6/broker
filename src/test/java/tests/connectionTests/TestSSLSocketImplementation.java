package tests.connectionTests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import api.PSPort;
import api.PSPortFactory;
import connection.SocketImplementation;
import connection.SocketImplementationFactory;

public class TestSSLSocketImplementation {
	
	PSPort port;
	SocketImplementation serverSocket;
	
	@Before
	public void testAccept() throws Throwable {
		serverSocket = SocketImplementationFactory.getSocketImplementation(new String[]{"SSLSocketImplementation"});
		new Thread() {
			public void run() {
				try {
					serverSocket.accept();
				} catch(Exception e) {}
			}
		}.start();
		port = PSPortFactory.getPort("PSPortSSL");
	}
	
	@Test
	public void testGetLastClientId() throws Throwable {
		assertEquals("Wrong ClientId"
				,"CN=Middleware,OU=Middleware,O=Middleware,L=Arrasate,ST=Basque Country,C=EU"
				,serverSocket.getLastClientId());
	}
	
	@After
	public void testClose() throws Throwable {
		assertTrue("SocketImplementation prematurely closed",!serverSocket.isClosed());
		serverSocket.close();
		assertTrue("SocketImplementation not closed",serverSocket.isClosed());
		port.disconnect();
	}
	
}
