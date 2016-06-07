package tests.constrainttests;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import constraint.Constraint;
import constraint.LengthConstraint;
import data.MessagePublish;

public class TestLengthConstraint {

	private Constraint constraint;
	
	@Before
	public void init() {
		constraint = new LengthConstraint("topic",5);
	}
	
	@Test
	public void testCheckConstraintTrue() {
		MessagePublish msg = new MessagePublish("topic",new byte[]{1,2,3,4,5});
		assertTrue(constraint.checkConstraint(msg));
	}
	
	@Test
	public void testCheckConstraintFalse() {
		MessagePublish msg = new MessagePublish("topic",new byte[]{1,2,3,4,5,6});
		assertFalse(constraint.checkConstraint(msg));
	}
	
	@Test
	public void testCheckConstraintFalseWithNull() {
		MessagePublish msg = new MessagePublish("topic",null);
		assertFalse(constraint.checkConstraint(msg));
	}
	
	@Test
	public void testToString() {
		assertEquals("Unexpected toString() result","topic: length=5",constraint.toString());
	}
	
	@After
	public void cleanup() {
		constraint = null;
	}
	
}
