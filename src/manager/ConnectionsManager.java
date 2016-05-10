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

public class ConnectionsManager extends Thread {
	
	public static final int CONNECTION_BUFFER_SIZE = 10;
	public static final int DIFFUSION_QUEUE_SIZE = 10;
	
	Map<Connection,List<String>> subscriptions;
	ServerSocket serverSocket;
	
	public ConnectionsManager(int port) throws IOException {
		subscriptions = Collections.synchronizedMap(new HashMap<Connection,List<String>>());
		serverSocket = new ServerSocket(port);
		//TODO: Log servSocket bound to port
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
				//TODO: Log new connection
			}
		} catch(IOException e) {
			//TODO Log
			e.printStackTrace();
		}
	}
	
	public void close() {
		try {
			serverSocket.close();
		} catch (IOException e) {}
		Connection[] connections = subscriptions.keySet().toArray(new Connection[0]);
		for(int i = 0 ; i < connections.length ; i++) {
			try {
				subscriptions.remove(connections[i]);
				connections[i].close();
			} catch (IOException e) {}
		}
	}
	
}
