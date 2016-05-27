package connection;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TCPSocketImplementation implements SocketImplementation {
	
	private static final Logger logger = LogManager.getRootLogger();

	private ServerSocket serverSocket;
	
	String lastClientId;
	
	public TCPSocketImplementation(int port) throws IOException {
		serverSocket = new ServerSocket(port);
		logger.info("ServerSocket bound to port "+port);
	}

	public Socket accept() throws IOException {
		Socket socket = serverSocket.accept();
	    lastClientId = socket.getInetAddress()+":"+socket.getPort();
	    return socket;
	}

	public String getLastClientId() {
		return lastClientId;
	}
	
	public static SocketImplementation getInstance(String[] args) throws Exception {
		int port = SocketConnection.DEFAULT_PORT;
		for(int i = 0 ; i < args.length ; i++) {
			switch(args[i]) {
			case "-p":
			case "--port":
				port = Integer.valueOf(args[++i]);
				break;
			default:
				break;
			}
		}
		return new TCPSocketImplementation(port);
	}

	public void close() {
		try {
			serverSocket.close();
		} catch (Exception e) {}
	}
	
	public boolean isClosed() {
		return serverSocket.isClosed();
	}
	
}
