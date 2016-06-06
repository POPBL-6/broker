package tests.connectiontests;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import connection.SSLListener;
import connection.ListenerFactory;
import connection.TCPListener;

@RunWith(PowerMockRunner.class)
@PrepareForTest({SSLListener.class, TCPListener.class})
@PowerMockIgnore("javax.management.*")
public class TestListenerFactory extends PowerMock {

	@Test
	public void testSSLListenerCreation() throws Throwable {
		expectNew(SSLListener.class,1234,"trustStore","keyStore"
				,"keyStorePassword","protocol","cipher").andThrow(new Exception("End"));
		replay(SSLListener.class);
		try {
			ListenerFactory.getListener(new String[]{
					"SSLListener","-p","1234","-t","trustStore","-k","keyStore","-kp"
					,"keyStorePassword","-pr","protocol","-c","cipher"
			});
		} catch(Exception e){
			assertEquals("Unexpected Exception in testSSLListenerCreation","End",e.getMessage());
		}
		verify(SSLListener.class);
	}
	
	@Test
	public void testTCPListenerCreation() throws Throwable {
		expectNew(TCPListener.class,9875).andThrow(new Exception("End"));
		replay(TCPListener.class);
		try {
			ListenerFactory.getListener(new String[]{
					"TCPListener","-p","9875","-t","trustStore","-k","keyStore","-kp"
					,"keyStorePassword","-pr","protocol","-c","cipher"
			});
		} catch(Exception e){
			assertEquals("Unexpected Exception in testTCPListenerCreation","End",e.getMessage());
		}
		verify(TCPListener.class);
	}
	
	@Test
	public void testListenerCreationFromFile() throws Throwable {
		expectNew(SSLListener.class,5434,"broker.jks","broker.jks"
				,"snowflake","TLSv1.2","TLS_RSA_WITH_AES_128_CBC_SHA256").andThrow(new Exception("End"));
		replay(SSLListener.class);
		try {
			ListenerFactory.getListener(new String[]{
					"file","broker.ini"
			});
		} catch(Exception e){
			assertEquals("Unexpected Exception in testListenerCreationFromFile","End",e.getMessage());
		}
		verify(SSLListener.class);
	}
	
}
