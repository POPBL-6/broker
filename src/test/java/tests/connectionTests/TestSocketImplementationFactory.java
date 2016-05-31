package tests.connectionTests;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import connection.SocketImplementationFactory;

@RunWith(PowerMockRunner.class)
@PrepareForTest(SocketImplementationFactory.class)
@PowerMockIgnore("javax.management.*")
public class TestSocketImplementationFactory extends PowerMock {

	@Before
	public void init() {
		
	}
	
	@Test
	public void testBrokerSocketImplementationCreation() throws Throwable {
		
	}
	
	@After
	public void cleanup() {
		
	}
	
}
