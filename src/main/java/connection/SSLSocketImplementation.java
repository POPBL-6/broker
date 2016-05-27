package connection;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SSLSocketImplementation implements SocketImplementation {
	
	private static final Logger logger = LogManager.getRootLogger();
	
	private ServerSocket serverSocket;
	
	String lastClientId;
	
	public SSLSocketImplementation(int port, String trustStore, String keyStore, String keyStorePassword) throws KeyStoreException, NoSuchAlgorithmException, CertificateException, FileNotFoundException, IOException, UnrecoverableKeyException, KeyManagementException {
		System.setProperty("javax.net.ssl.trustStore",trustStore);
		KeyStore keystore = KeyStore.getInstance("JKS");
	    keystore.load(new FileInputStream(keyStore), keyStorePassword.toCharArray());
	    
	    KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
	    kmf.init(keystore, keyStorePassword.toCharArray());
	    SSLContext sc = SSLContext.getInstance("TLSv1.2");
	    sc.init(kmf.getKeyManagers(), null, null);
		
		SSLServerSocketFactory ssf = sc.getServerSocketFactory();
		serverSocket = ssf.createServerSocket(port);
		logger.info("SSLServerSocket bound to port "+port);
	}

	public Socket accept() throws IOException {
		SSLSocket socket = (SSLSocket)serverSocket.accept();
	    socket.setEnabledCipherSuites(new String[] {"TLS_DHE_DSS_WITH_AES_128_CBC_SHA256"});
	    socket.setEnabledProtocols(new String[] {"TLSv1.2"});
	    socket.setEnableSessionCreation(true);
	    socket.setNeedClientAuth(true);
	    socket.startHandshake();
	    //TODO get the name from the certificate for lastClientId
	    lastClientId = socket.getInetAddress()+":"+socket.getPort();
	    return socket;
	}

	public String getLastClientId() {
		return lastClientId;
	}
	
	public static SocketImplementation getInstance(String[] args) throws Exception {
		int port = SocketConnection.DEFAULT_PORT;
		String trustStore = ".keystore";
		String keyStore = ".keystore";
		String keyStorePassword = "snowflake";
		for(int i = 0 ; i < args.length ; i++) {
			switch(args[i]) {
			case "-p":
			case "--port":
				port = Integer.valueOf(args[++i]);
				break;
			//SSL Specifics
			case "-t":
			case "--truststore":
				trustStore = args[++i];
				break;
			case "-k":
			case "--keystore":
				keyStore = args[++i];
				break;
			case "-kp":
			case "--keyPass":
				keyStorePassword = args[++i];
				break;
			default:
				break;
			}
		}
		return new SSLSocketImplementation(port,trustStore,keyStore,keyStorePassword);
	}

	public void close() {
		try {
			serverSocket.close();
		} catch (Exception e) {}
	}
	
	public boolean isClosed() {
		return serverSocket.isClosed();
	}
	
}
