package connection;

import java.io.IOException;
import java.net.Socket;

public interface SocketImplementation {
	public Socket accept() throws IOException;
	public String getLastClientId();
	public void close();
	public boolean isClosed();
}
