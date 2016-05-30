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
 * It extends Thread so that it can be run asynchronously if needed.
 * 
 * @author Jon Ayerdi
 */
public class ConnectionsManager extends Thread {

	private static final Logger logger = LogManager.getLogger(ConnectionsManager.class);
	
	public static final int CONNECTION_BUFFER_SIZE = 10;
	public static final int DIFFUSION_QUEUE_SIZE = 10;
	
	private Map<Connection,List<String>> subscriptions;
	private SocketImplementation socketImplementation;
	
	/**
	 * Creates a ConnectionsManager that will accept connections.
	 * 
	 * @param socketImplementation The SocketImplementation that will be used to establish new connections.
	 */
	public ConnectionsManager(SocketImplementation socketImplementation) throws IOException {
		subscriptions = Collections.synchronizedMap(new HashMap<Connection,List<String>>());
		this.socketImplementation = socketImplementation;
	}
	
	/**
	 * Entry point for a ConnectionsManager Thread, accepts new connections with
	 * the provided SocketImplementation and spawns a MessagesManager for each of them.
	 * 
	 */
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
	
	/**
	 * Closes all connections, therefore terminating their corresponding MessagesManager, as
	 * well as the SocketImplementation provided in the constructor, thus terminating this Thread.
	 * This ConnectionsManager will be left permanently unusable after performing this operation.
	 */
	public void close() {
		socketImplementation.close();
		Connection[] connections = subscriptions.keySet().toArray(new Connection[0]);
		for(int i = 0 ; i < connections.length ; i++) {
			subscriptions.remove(connections[i]);
			connections[i].close();
		}
	}
	
}
