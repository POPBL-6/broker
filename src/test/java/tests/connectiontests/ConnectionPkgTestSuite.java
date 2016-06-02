package tests.connectiontests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({TestListenerFactory.class, TestSSLListener.class, TestTCPListener.class})
public class ConnectionPkgTestSuite {

}
