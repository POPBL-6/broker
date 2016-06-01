package tests.connectiontests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({TestSocketImplementationFactory.class, TestSSLSocketImplementation.class, TestTCPSocketImplementation.class})
public class ConnectionPkgTestSuite {

}
