package broker;

import manager.ConnectionsManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import connection.Listener;
import connection.ListenerFactory;

/**
 * Main class of the Middleware Broker,
 * in charge of loading the configuration.
 * 
 * @author Jon Ayerdi
 */
public class BrokerMain {

	private static final Logger logger = LogManager.getRootLogger();
	
	/**
	 * Entry point of the Broker application.
	 * 
	 * @param args The configuration for the SocketImplementation, 
	 * for example: "SSLListener -p 443 -t .keystore -k .keystore -kp snowflake".
	 */
	public static void main(String[] args) {
		BrokerMain b = new BrokerMain();
		b.start(args);
	}
	
	/**
	 * Instantiates a SocketImplementation and runs a ConnectionsManager with it.
	 * 
	 * @param args The configuration for the SocketImplementation, 
	 * for example: "SSLListener -p 443 -t .keystore -k .keystore -kp snowflake".
	 */
	public void start(String[] args) {
		ConnectionsManager manager;
		Listener listener;
		try {
			if(args.length>0) {
				listener = ListenerFactory.getListener(args);
			}
			else {
				listener = ListenerFactory.
						getListener(ListenerFactory.getConfigurationFromFile("broker.ini"));
			}
			manager = new ConnectionsManager(listener);
			logger.info("Broker successfully started");
			manager.run();
		} catch (Throwable e) {
			logger.fatal("Could not start Broker: "+e.getClass().getName()+" "+e.getMessage());
		}
	}
	
}
