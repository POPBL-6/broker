package tests.constrainttests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import constraint.ClassConstraint;
import constraint.Constraint;
import data.MessagePublish;

public class TestClassConstraint {

	private Constraint constraint1, constraint2;
	private MessagePublish msg1, msg2, msg3;
	
	@Before
	public void init() throws ClassNotFoundException, IOException {
		constraint1 = new ClassConstraint("topic1","java.lang.String");
		constraint2 = new ClassConstraint("topic2",Integer.class);
		msg1 = new MessagePublish("topic1","Hello");
		msg2 = new MessagePublish("topic2",5);
		msg3 = new MessagePublish("topic3",null);
	}
	
	@Test
	public void testCheckConstraintTrue() throws IOException {
		assertTrue(constraint1.checkConstraint(msg1));
		assertTrue(constraint2.checkConstraint(msg2));
	}
	
	@Test
	public void testCheckConstraintFalse() throws IOException {
		assertFalse(constraint1.checkConstraint(msg2));
		assertFalse(constraint2.checkConstraint(msg1));
	}
	
	@Test
	public void testCheckConstraintFalseWithNull() {
		assertFalse(constraint1.checkConstraint(msg3));
		assertFalse(constraint2.checkConstraint(msg3));
	}
	
	@Test
	public void testToString() {
		assertEquals("Unexpected toString() result","topic1: class=java.lang.String",constraint1.toString());
		assertEquals("Unexpected toString() result","topic2: class=java.lang.Integer",constraint2.toString());
	}
	
	@After
	public void cleanup() {
		constraint1 = null;
		constraint2 = null;
	}
	
}
