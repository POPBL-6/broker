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
import connection.Listener;
import connection.SocketConnection;
import constraint.ConstraintsManager;

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
	private Listener listener;
	private ConstraintsManager constraintsManager;
	
	/**
	 * Creates a ConnectionsManager that will accept connections.
	 * 
	 * @param listener The Listener that will be used to establish new connections.
	 */
	public ConnectionsManager(Listener listener) throws IOException, Exception {
		subscriptions = Collections.synchronizedMap(new HashMap<Connection,List<String>>());
		this.listener = listener;
		try {
			constraintsManager = new ConstraintsManager();
			constraintsManager.loadFromFile();
		} catch(Exception e) {
			throw new IllegalArgumentException(e.getClass().getName()+" "+e.getMessage()+" in constraints.ini");
		}
	}
	
	/**
	 * Entry point for a ConnectionsManager Thread, accepts new connections with
	 * the provided Listener and spawns a MessagesManager for each of them.
	 * 
	 */
	public void run() {
		Socket socket;
		SocketConnection connection;
		while(!listener.isClosed()) {
			try {
				socket = listener.accept();
				connection = new SocketConnection();
				connection.setConnectionId(listener.getLastClientId());
				connection.init(socket, CONNECTION_BUFFER_SIZE);
				subscriptions.put(connection, new ArrayList<String>());
				new MessagesManager(connection,subscriptions,constraintsManager).start();
				logger.info("New connection from " + socket.getInetAddress() + " received");
			} catch(Exception e) {
				logger.error("Connection error: "+e.getClass()+" "+e.getMessage());
			}
		}
	}
	
	/**
	 * Closes all connections, therefore terminating their corresponding MessagesManager, as
	 * well as the Listener provided in the constructor, thus terminating this Thread.
	 * This ConnectionsManager will be left permanently unusable after performing this operation.
	 */
	public void close() {
		listener.close();
		Connection[] connections = subscriptions.keySet().toArray(new Connection[0]);
		for(int i = 0 ; i < connections.length ; i++) {
			subscriptions.remove(connections[i]);
			connections[i].close();
		}
		interrupt();
	}
	
}
