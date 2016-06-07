package tests.constrainttests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({TestClassConstraint.class,TestLengthConstraint.class,TestConstraintsManager.class})
public class ConstraintPkgTestSuite {

}
