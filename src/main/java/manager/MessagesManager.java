package manager;

import java.util.List;
import java.util.Map;

import connection.Connection;
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
 */
public class MessagesManager extends Thread {

	static final Logger logger = LogManager.getLogger(MessagesManager.class);

	Map<Connection,List<String>> subscriptions;
	Connection connection;
	
	public MessagesManager(Connection connection, Map<Connection,List<String>> subscriptions) {
		this.connection = connection;
		this.subscriptions = subscriptions;
	}
	
	private void manageMessagePublication(MessagePublication message) {
		logger.warn("A message publication class message was received from a non broker peer");
	}
	
	private void manageMessagePublish(MessagePublish message) {
		MessagePublication out = new MessagePublication(message, connection.toString(), System.currentTimeMillis());
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

	private void manageMessageSubscribe(MessageSubscribe message) {
		String[] topics = message.getTopics();
		List<String> subscription;
		logger.info("MessageSubscribe received from "+connection.toString());
		if(topics!=null) {
			subscription = subscriptions.get(connection);
			for(int i = 0 ; i < topics.length ; i++) {
				if(!subscription.contains(topics[i]))
					subscription.add(topics[i]);
			}
		}
	}
	
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
	
	public void run() {
		try {
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
