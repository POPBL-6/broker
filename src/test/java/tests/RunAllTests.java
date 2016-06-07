package tests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import tests.brokertests.BrokerPkgTestSuite;
import tests.connectiontests.ConnectionPkgTestSuite;
import tests.constrainttests.ConstraintPkgTestSuite;
import tests.managertests.ManagerPkgTestSuite;

@RunWith(Suite.class)
@SuiteClasses({BrokerPkgTestSuite.class, ConnectionPkgTestSuite.class, ConstraintPkgTestSuite.class, ManagerPkgTestSuite.class})
public class RunAllTests {

}