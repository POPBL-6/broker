package tests.brokertests;

import static org.easymock.EasyMock.expect;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import broker.BrokerMain;
import connection.SocketImplementationFactory;

@RunWith(PowerMockRunner.class)
@PrepareForTest(SocketImplementationFactory.class)
@PowerMockIgnore("javax.management.*")
public class TestBrokerMain extends PowerMock {

	@Before
	public void init() {
		mockStaticStrict(SocketImplementationFactory.class);
	}
	
	@Test
	public void testBrokerSocketImplementationCreation() throws Throwable {
		String[] config = new String[]{"SSLSocketImplementation", "-p", "8888"
				, "-t", "5897", "-k", "1234", "-kp", "asdf"};
		String[] config2 = new String[]{"TCPSocketImplementation"};
		expect(SocketImplementationFactory.getSocketImplementation(config)).andReturn(null);
		expect(SocketImplementationFactory.getConfigurationFromFile("broker.ini")).andReturn(config2);
		expect(SocketImplementationFactory.getSocketImplementation(config2)).andReturn(null);
		replay(SocketImplementationFactory.class);
		BrokerMain.main(config);
		BrokerMain.main(new String[]{});
		verify(SocketImplementationFactory.class);
	}
	
}
