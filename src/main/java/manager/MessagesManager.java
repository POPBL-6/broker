package manager;

import java.util.List;
import java.util.Map;

import connection.Connection;
import constraint.ConstraintsManager;
import data.Message;
import data.MessagePublication;
import data.MessagePublish;
import data.MessageSubscribe;
import data.MessageUnsubscribe;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Class in charge of managing a single Connection.
 * It reads Message objects form the assigned Connection and acts accordingly.
 * 
 * @author Jon Ayerdi
 */
public class MessagesManager extends Thread {

	private static final Logger logger = LogManager.getLogger(MessagesManager.class);

	private Map<Connection,List<String>> subscriptions;
	private Connection connection;
	private ConstraintsManager constraintsManager;
	
	/**
	 * Manages incoming Messages from the provided Connection.
	 * 
	 * @param connection The Connection used to communicate with the other end.
	 * @param subscriptions The Map containing the subscriptions for each Connection of this Broker.
	 * @param constraintsManager The ConstraintsManager that decides whether a MessagePublish is accepted or not.
	 */
	public MessagesManager(Connection connection, Map<Connection,List<String>> subscriptions, ConstraintsManager constraintsManager) {
		this.connection = connection;
		this.subscriptions = subscriptions;
		this.constraintsManager = constraintsManager;
	}
	
	/**
	 * Determines what to do when a MessagePublication is received.
	 * 
	 * @param message
	 */
	private void manageMessagePublication(MessagePublication message) {
		logger.warn("A message publication class message was received from a non broker peer: "
				+ message.getSender() + " Topic: " + message.getTopic());
	}
	
	/**
	 * Determines what to do when a MessagePublish is received.
	 * 
	 * @param message
	 */
	private void manageMessagePublish(MessagePublish message) {
		if(constraintsManager.checkConstraints(message, connection.getConnectionId())) {
			MessagePublication out = new MessagePublication(message, connection.getConnectionId(), System.currentTimeMillis());
			Connection[] connections = subscriptions.keySet().toArray(new Connection[0]);
			logger.info("MessagePublish received from "+connection.toString()+", distributing");
			for(int i = 0 ; i < connections.length ; i++) {
				List<String> topics = subscriptions.get(connections[i]);
				if(topics!=null && topics.contains(out.getTopic())) {
					try {
						connections[i].writeMessage(out);
					} catch(Exception e) {
						logger.error("The connection was not successfully closed before", e);
					}
				}
			}
		}
	}

	/**
	 * Determines what to do when a MessageSubscribe is received.
	 * 
	 * @param message
	 */
	private void manageMessageSubscribe(MessageSubscribe message) {
		String[] topics = message.getTopics();
		List<String> subscription;
		logger.info("MessageSubscribe received from "+connection.toString());
		if(topics!=null) {
			subscription = subscriptions.get(connection);
			for(int i = 0 ; i < topics.length ; i++) {
				if(!subscription.contains(topics[i])) {
					subscription.add(topics[i]);
				}
			}
		}
	}
	
	/**
	 * Determines what to do when a MessageUnsubscribe is received.
	 * 
	 * @param message
	 */
	private void manageMessageUnsubscribe(MessageUnsubscribe message) {
		String[] topics = message.getTopics();
		List<String> subscription;
		logger.info("MessageUnsubscribe received from "+connection.toString());
		if(topics!=null) {
			subscription = subscriptions.get(connection);
			for(int i = 0 ; i < topics.length ; i++) {
					subscription.remove(topics[i]);
			}
		}
	}
	
	/**
	 * The entry point for this MessagesManager Thread. Constantly reads Messages
	 * from the provided Connection and acts accordingly.
	 */
	public void run() {
		try {
			connection.setThreadToInterrupt(this);
			while(!connection.isClosed()) {
				Message message = connection.readMessage();
				logger.info("Message received by the messages manager");
				switch(message.getMessageType()) {
				case Message.MESSAGE_PUBLICATION:
					manageMessagePublication((MessagePublication) message);
					break;
				case Message.MESSAGE_PUBLISH:
					manageMessagePublish((MessagePublish) message);		
					break;
				case Message.MESSAGE_SUBSCRIBE:
					manageMessageSubscribe((MessageSubscribe) message);
					break;
				case Message.MESSAGE_UNSUBSCRIBE:
					manageMessageUnsubscribe((MessageUnsubscribe) message);
					break;
				default:
					break;
				}
			}
		} catch (InterruptedException e) {
			//Interrupted
		} catch (NullPointerException e) {
			//Interrupted
		}
		connection.close();
		subscriptions.remove(connection);
		logger.info("Messages manager has been disconnected");
	}
	
}
