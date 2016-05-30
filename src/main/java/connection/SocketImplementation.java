package connection;

import java.io.IOException;
import java.net.Socket;

/**
 * Interface for ServerSockets that are able to accept connections.
 * 
 * @author Jon Ayerdi
 */
public interface SocketImplementation {
	/**
	 * Accepts an incoming connection and returns the Socket.
	 * 
	 * @return The Socket corresponding to the newly established connection.
	 * @throws IOException
	 */
	public Socket accept() throws IOException;
	/**
	 * Returns a String that identifies the last accept()-ed client.
	 * 
	 * @return The String representing the last client's ID (senderId for Messages).
	 */
	public String getLastClientId();
	/**
	 * Closes the underlying ServerSocket, making this object unusable for later.
	 */
	public void close();
	/**
	 * Returns the closed state of the underlying ServerSocket.
	 * 
	 * @return true if the socket has been closed .
	 */
	public boolean isClosed();
}
