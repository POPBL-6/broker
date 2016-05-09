package connection;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import utils.ArrayUtils;
import data.Message;

public class SocketConnection implements Connection {
	
	private Socket socket;
	private BlockingQueue<Message> messagesIn, messagesOut;
	private Thread readingThread, writingThread;
	
	public void init(Socket socket, int bufferSize) {
		this.socket = socket;
		messagesIn = new ArrayBlockingQueue<Message>(bufferSize);
		messagesOut = new ArrayBlockingQueue<Message>(bufferSize);
		readingThread = new Thread() {
			public void run() {
				readingTask();
			}
		};
		writingThread = new Thread() {
			public void run() {
				sendingTask();
			}
		};
		readingThread.start();
		writingThread.start();
	}
	
	public void readingTask() {
		try {
			InputStream in = socket.getInputStream();
			int messageLength;
			int read;
			byte[] messageBytes;
			try {
				while(!socket.isClosed()) {
					messageLength = 0;
					read = 0;
					for(int i = 0 ; i < Integer.SIZE ; i++) {
						messageLength += in.read()<<(Byte.SIZE*i);
					}
					messageBytes = new byte[messageLength];
					while(read < messageLength) {
						in.read(messageBytes, read, messageLength-read);
					}
					//TODO: Log mensaje recibido
					try {
						messagesIn.put(Message.fromByteArray(messageBytes));
					} catch(NullPointerException nullException) {
						//TODO: Log bad Message
					}
				}
			} catch(SocketException sockException) {
				//Socket was closed
			} catch(InterruptedException interruptException) {
				//BlockingQueue interrupted
			}
		}  catch(IOException ioException) {
			//TODO: Log
			try {
				close();
			} catch (IOException e) {}
		}
	}
	
	public void sendingTask() {
		try {
			OutputStream out = socket.getOutputStream();
			byte[] send;
			byte[] sendLength = new byte[Integer.SIZE];
			try {
				while(!socket.isClosed()) {
					send = messagesOut.take().toByteArray();
					for(int i = 0 ; i < Integer.SIZE ; i++) {
						sendLength[i] = (byte)(send.length>>(Byte.SIZE*i));
					}
					send = ArrayUtils.concat(sendLength,send);
					out.write(send);
				}
			} catch(SocketException sockException) {
				//Socket was closed
			} catch(InterruptedException interruptException) {
				//BlockingQueue interrupted
			}
		}  catch(IOException ioException) {
			//TODO: Log
			try {
				close();
			} catch (IOException e) {}
		}
	}

	public Message readMessage() throws InterruptedException {
		return messagesIn.take();
	}

	public void writeMessage(Message message) throws InterruptedException {
		messagesOut.put(message);
	}

	public void close() throws IOException {
		socket.close();
		socket = null;
		messagesIn = null;
		messagesOut = null;
	}

	public boolean isClosed() {
		return socket==null;
	}
	
	public String toString() {
		if(socket!=null) {
			return socket.getInetAddress()+":"+socket.getPort();
		}
		else return "ConnectionClosed";
	}
	
}
