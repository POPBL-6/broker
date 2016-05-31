package tests.managerTests;

import static org.easymock.EasyMock.expect;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

import manager.ConnectionsManager;

import org.easymock.EasyMock;
import org.easymock.EasyMockSupport;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import connection.SocketImplementation;

public class TestConnectionsManager extends EasyMockSupport {

	SocketImplementation serverSocket;
	Socket socket;
	ConnectionsManager manager;
	
	@Before
	public void init() throws Throwable {
		serverSocket = createStrictMock(SocketImplementation.class);
		socket = createMock(Socket.class);
		manager = new ConnectionsManager(serverSocket);
	}
	
	@Test(timeout=8000)
	public void testRun() throws Throwable {
		expect(serverSocket.isClosed()).andReturn(false);
		expect(serverSocket.accept()).andReturn(socket);
		expect(serverSocket.getLastClientId()).andReturn("");
		expect(serverSocket.isClosed()).andReturn(false);
		expect(serverSocket.accept()).andThrow(new IOException("Dummy connection error"));
		expect(serverSocket.isClosed()).andReturn(true);
		serverSocket.close();
		EasyMock.expectLastCall();
		
		expect(socket.getInetAddress()).andReturn(InetAddress.getLocalHost());
		socket.close();
		EasyMock.expectLastCall();
		
		replayAll();
		manager.start();
		Thread.sleep(1000);
		manager.close();
		manager.join();
		verifyAll();
	}
	
	@After
	public void cleanup() {
		
	}
	
}
