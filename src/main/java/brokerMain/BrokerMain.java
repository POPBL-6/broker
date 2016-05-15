package brokerMain;

import java.io.IOException;

import connection.SocketConnection;
import manager.ConnectionsManager;

public class BrokerMain {
	
	ConnectionsManager manager;
	int port = SocketConnection.DEFAULT_PORT;

	public static void main(String[] args) {
		BrokerMain b = new BrokerMain();
		b.start(args);
	}
	
	public void start(String[] args) {
		for(int i = 0 ; i < args.length ; i++) {
			switch(args[i]) {
			case "-p":
			case "--port":
				port = Integer.valueOf(args[++i]);
				break;
			}
		}
		try {
			manager = new ConnectionsManager(port);
		} catch (IOException e) {
			// TODO: Log unable to bind port
			e.printStackTrace();
		}
		//TODO: Log
		System.out.println("Starting Broker: port="+port);
		manager.run();
	}
	
}
