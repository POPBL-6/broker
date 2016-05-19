package brokerMain;

import java.io.IOException;


import connection.SocketConnection;
import manager.ConnectionsManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BrokerMain {

	static final Logger logger = LogManager.getRootLogger();

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
			logger.fatal("Could not bind to port", e);
		}
		logger.info("Broker successfully started at port " + port);
		manager.run();
	}
	
}
