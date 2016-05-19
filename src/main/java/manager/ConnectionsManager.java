package manager;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import connection.Connection;
import connection.SocketConnection;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Class in charge of accepting TCP connections and spawning a MessagesManager for each client.
 */
public class ConnectionsManager extends Thread {

	static final Logger logger = LogManager.getLogger(ConnectionsManager.class);
	
	public static final int CONNECTION_BUFFER_SIZE = 10;
	public static final int DIFFUSION_QUEUE_SIZE = 10;
	
	Map<Connection,List<String>> subscriptions;
	ServerSocket serverSocket;
	
	public ConnectionsManager(int port) throws IOException {
		subscriptions = Collections.synchronizedMap(new HashMap<Connection,List<String>>());
		serverSocket = new ServerSocket(port);
		logger.info("Server socket bound to port " + port);
	}
	
	public void run() {
		Socket socket;
		SocketConnection connection;
		try {
			while(!serverSocket.isClosed()) {
				socket = serverSocket.accept();
				connection = new SocketConnection();
				connection.init(socket, CONNECTION_BUFFER_SIZE);
				subscriptions.put(connection, new ArrayList<String>());
				new MessagesManager(connection,subscriptions).start();
				logger.info("New connection from " + socket.getInetAddress() + " received");
			}
		} catch(IOException e) {
			logger.error("Socket error when binding port", e);
		}
	}
	
	public void close() {
		try {
			serverSocket.close();
		} catch (IOException e) {}
		Connection[] connections = subscriptions.keySet().toArray(new Connection[0]);
		for(int i = 0 ; i < connections.length ; i++) {
			subscriptions.remove(connections[i]);
			connections[i].close();
		}
	}
	
}
