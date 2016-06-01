package broker;

import manager.ConnectionsManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import connection.SocketImplementation;
import connection.SocketImplementationFactory;

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
	 * for example: "SSLSocketImplementation -p 443 -t .keystore -k .keystore -kp snowflake".
	 */
	public static void main(String[] args) {
		BrokerMain b = new BrokerMain();
		b.start(args);
	}
	
	/**
	 * Instantiates a SocketImplementation and runs a ConnectionsManager with it.
	 * 
	 * @param args The configuration for the SocketImplementation, 
	 * for example: "SSLSocketImplementation -p 443 -t .keystore -k .keystore -kp snowflake".
	 */
	public void start(String[] args) {
		ConnectionsManager manager;
		SocketImplementation socketImplementation;
		try {
			if(args.length>0) {
				socketImplementation = SocketImplementationFactory.getSocketImplementation(args);
			}
			else {
				socketImplementation = SocketImplementationFactory.
						getSocketImplementation(SocketImplementationFactory.getConfigurationFromFile("broker.ini"));
			}
			manager = new ConnectionsManager(socketImplementation);
			logger.info("Broker successfully started");
			manager.run();
		} catch (Throwable e) {
			logger.fatal("Could not start Broker: "+e.getClass()+" "+e.getMessage());
		}
	}
	
}
