package tests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import tests.brokerTests.BrokerPkgTestSuite;
import tests.connectionTests.ConnectionPkgTestSuite;
import tests.managerTests.ManagerPkgTestSuite;

@RunWith(Suite.class)
@SuiteClasses({BrokerPkgTestSuite.class, ConnectionPkgTestSuite.class, ManagerPkgTestSuite.class})
public class RunAllTests {

}