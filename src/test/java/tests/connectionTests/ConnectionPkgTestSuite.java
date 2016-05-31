package tests.connectionTests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import connection.SSLSocketImplementation;
import connection.TCPSocketImplementation;

@RunWith(Suite.class)
@SuiteClasses({TestSocketImplementationFactory.class, SSLSocketImplementation.class, TCPSocketImplementation.class})
public class ConnectionPkgTestSuite {

}
