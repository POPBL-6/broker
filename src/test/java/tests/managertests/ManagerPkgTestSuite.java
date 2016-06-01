package tests.managertests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({TestConnectionsManager.class, TestMessagesManager.class})
public class ManagerPkgTestSuite {

}
