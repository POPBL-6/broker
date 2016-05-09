package main;

import java.io.IOException;

public class BrokerMain {
	
	public static final int DEFAULT_PORT = 6969;
	
	ConnectionsManager manager;
	int port = DEFAULT_PORT;

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
		}
	}
	
}
