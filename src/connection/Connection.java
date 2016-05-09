package connection;

import java.io.IOException;

import data.Message;

public interface Connection {
	
	Message readMessage() throws InterruptedException;
	void writeMessage(Message message) throws InterruptedException;
	void close() throws IOException;
	boolean isClosed();
	
}
