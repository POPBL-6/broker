package main;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import connection.Connection;
import data.Message;
import data.MessagePublication;
import data.MessagePublish;
import data.MessageSubscribe;
import data.MessageUnsubscribe;

public class MessagesManager extends Thread {

	Map<Connection,List<String>> subscriptions;
	Connection connection;
	
	public MessagesManager(Connection connection, Map<Connection,List<String>> subscriptions) {
		this.connection = connection;
		this.subscriptions = subscriptions;
	}
	
	private void manageMessagePublication(MessagePublication message) {
		//TODO: Log? No deberia ocurrir
	}
	
	private void manageMessagePublish(MessagePublish message) {
		MessagePublication out = new MessagePublication(message, connection.toString(), System.currentTimeMillis());
		Connection[] connections = subscriptions.keySet().toArray(new Connection[0]);
		for(int i = 0 ; i < connections.length ; i++) {
			List<String> topics = subscriptions.get(connections[i]);
			if(topics!=null && topics.contains(out.getTopic())) {
				try {
					connections[i].writeMessage(out);
				} catch(Exception e) {
					//La conexion se ha cerrado antes
				}
			}
		}
	}

	private void manageMessageSubscribe(MessageSubscribe message) {
		String[] topics = message.getTopics();
		List<String> subscription;
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
				//TODO: Log message received
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
		}
		try {
			connection.close();
		} catch (IOException e) {}
		subscriptions.remove(connection);
		//TODO: Log disconection
	}
	
}
