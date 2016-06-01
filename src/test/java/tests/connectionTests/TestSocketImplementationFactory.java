package tests.connectiontests;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import connection.SSLSocketImplementation;
import connection.SocketImplementationFactory;
import connection.TCPSocketImplementation;

@RunWith(PowerMockRunner.class)
@PrepareForTest({SSLSocketImplementation.class, TCPSocketImplementation.class})
@PowerMockIgnore("javax.management.*")
public class TestSocketImplementationFactory extends PowerMock {

	@Test
	public void testSSLSocketImplementationCreation() throws Throwable {
		expectNew(SSLSocketImplementation.class,1234,"trustStore","keyStore"
				,"keyStorePassword","protocol","cipher").andThrow(new Exception("End"));
		replay(SSLSocketImplementation.class);
		try {
			SocketImplementationFactory.getSocketImplementation(new String[]{
					"SSLSocketImplementation","-p","1234","-t","trustStore","-k","keyStore","-kp"
					,"keyStorePassword","-pr","protocol","-c","cipher"
			});
		} catch(Exception e){
			assertEquals("Unexpected Exception in testSSLSocketImplementationCreation","End",e.getMessage());
		}
		verify(SSLSocketImplementation.class);
	}
	
	@Test
	public void testTCPSocketImplementationCreation() throws Throwable {
		expectNew(TCPSocketImplementation.class,9875).andThrow(new Exception("End"));
		replay(TCPSocketImplementation.class);
		try {
			SocketImplementationFactory.getSocketImplementation(new String[]{
					"TCPSocketImplementation","-p","9875","-t","trustStore","-k","keyStore","-kp"
					,"keyStorePassword","-pr","protocol","-c","cipher"
			});
		} catch(Exception e){
			assertEquals("Unexpected Exception in testTCPSocketImplementationCreation","End",e.getMessage());
		}
		verify(TCPSocketImplementation.class);
	}
	
	@Test
	public void testSocketImplementationCreationFromFile() throws Throwable {
		expectNew(SSLSocketImplementation.class,443,".keystore",".keystore"
				,"snowflake","TLSv1.2","TLS_DHE_DSS_WITH_AES_128_CBC_SHA256").andThrow(new Exception("End"));
		replay(SSLSocketImplementation.class);
		try {
			SocketImplementationFactory.getSocketImplementation(new String[]{
					"file","broker.ini"
			});
		} catch(Exception e){
			assertEquals("Unexpected Exception in testSSLSocketImplementationCreation","End",e.getMessage());
		}
		verify(SSLSocketImplementation.class);
	}
	
}
