package manager;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import connection.Connection;
import connection.SocketConnection;
import connection.SocketImplementation;

/**
 * Class in charge of accepting TCP connections and spawning a MessagesManager for each client.
 */
public class ConnectionsManager extends Thread {

	private static final Logger logger = LogManager.getLogger(ConnectionsManager.class);
	
	public static final int CONNECTION_BUFFER_SIZE = 10;
	public static final int DIFFUSION_QUEUE_SIZE = 10;
	
	Map<Connection,List<String>> subscriptions;
	SocketImplementation socketImplementation;
	
	public ConnectionsManager(SocketImplementation socketImplementation) throws IOException {
		subscriptions = Collections.synchronizedMap(new HashMap<Connection,List<String>>());
		this.socketImplementation = socketImplementation;
	}
	
	public void run() {
		Socket socket;
		SocketConnection connection;
		while(!socketImplementation.isClosed()) {
			try {
				socket = socketImplementation.accept();
				connection = new SocketConnection();
				connection.setConnectionId(socketImplementation.getLastClientId());
				connection.init(socket, CONNECTION_BUFFER_SIZE);
				subscriptions.put(connection, new ArrayList<String>());
				new MessagesManager(connection,subscriptions).start();
				logger.info("New connection from " + socket.getInetAddress() + " received");
			} catch(Exception e) {
				logger.error("Connection error: "+e.getClass()+" "+e.getMessage());
			}
		}
	}
	
	public void close() {
		socketImplementation.close();
		Connection[] connections = subscriptions.keySet().toArray(new Connection[0]);
		for(int i = 0 ; i < connections.length ; i++) {
			subscriptions.remove(connections[i]);
			connections[i].close();
		}
	}
	
}
