package connection;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Listener for plain TCP connections.
 * 
 * @author Jon Ayerdi
 */
public class TCPListener implements Listener {
	
	private static final Logger logger = LogManager.getRootLogger();

	private ServerSocket serverSocket;
	
	private String lastClientId;
	
	/**
	 * Creates a new TCPListener object.
	 * 
	 * @param port
	 * @throws IOException
	 */
	public TCPListener(int port) throws IOException {
		serverSocket = new ServerSocket(port);
		lastClientId = "None";
		logger.info("TCPListener bound to port "+port);
	}
	
	/**
	 * Accepts an incoming connection and returns the Socket.
	 * 
	 * @return The Socket corresponding to the newly established connection.
	 * @throws IOException
	 */
	public Socket accept() throws IOException {
		Socket socket = serverSocket.accept();
	    lastClientId = socket.getInetAddress()+":"+socket.getPort();
	    return socket;
	}

	/**
	 * Returns a String that identifies the last accept()-ed client.
	 * 
	 * @return The String representing the last client's ID: IP:Port.
	 */
	public String getLastClientId() {
		return lastClientId;
	}
	
	/**
	 * Creates a TCPListener from the configuration. Example:
	 * "TCPListener -p 443"
	 * 
	 * @param args The configuration.
	 * @return
	 * @throws Exception
	 */
	public static Listener getInstance(String[] args) throws Exception {
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
		return new TCPListener(port);
	}

	/**
	 * Closes the underlying ServerSocket, making this object unusable for later.
	 */
	public void close() {
		try {
			serverSocket.close();
		} catch (Exception e) {}
	}
	
	/**
	 * Returns the closed state of the underlying ServerSocket.
	 * 
	 * @return true if the socket has been closed .
	 */
	public boolean isClosed() {
		return serverSocket.isClosed();
	}
	
}
