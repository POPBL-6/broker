package tests.constrainttests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import org.easymock.EasyMockSupport;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import constraint.Constraint;
import constraint.ConstraintsManager;
import data.MessagePublish;

public class TestConstraintsManager extends EasyMockSupport {

	private ConstraintsManager manager;
	private MessagePublish msg1, msg2, msg3;
	private ByteArrayInputStream inStream;
	
	@Before
	public void init() throws ClassNotFoundException, IOException {
		manager = new ConstraintsManager();
		msg1 = new MessagePublish("topic1","Hello");
		msg2 = new MessagePublish("topic1",5);
		msg3 = new MessagePublish("topic3",null);
		String config = ""
				+ "topic1\n"
				+ "{\n"
				+ "class=java.lang.String\n"
				+ "}\n"
				+ "topic2\n"
				+ "{\n"
				+ "length=3\n"
				+ "garbage=whatever\n"
				+ "}\n"
				+ "topic1\n"
				+ "{\n"
				+ "moreGarbage=whatever\n"
				+ "}";
		inStream = new ByteArrayInputStream(config.getBytes());
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testLoadFromFile() throws Exception {
		manager.loadFromFile(inStream);
		Field field = ConstraintsManager.class.getDeclaredField("constraints");
		field.setAccessible(true);
		Map<String,List<Constraint>> constraints = (Map<String, List<Constraint>>) field.get(manager);
		assertNotEquals(constraints.get("topic1"),null);
		assertNotEquals(constraints.get("topic2"),null);
		assertEquals(constraints.get("topic3"),null);
	}
	
	@Test
	public void testLoadFromFileDefault() throws IOException, Exception {
		ConstraintsManager managerMock = new ConstraintsManager() {
			public void loadFromFile(InputStream inStream) throws IOException, Exception {
				assertTrue(inStream instanceof FileInputStream);
			}
		};
		managerMock.loadFromFile();
	}
	
	@Test
	public void testCheckConstraintsTrue() throws Exception {
		manager.loadFromFile(inStream);
		assertTrue(manager.checkConstraints(msg1, "sender"));
	}
	
	@Test
	public void testCheckConstraintsFalse() throws Exception {
		manager.loadFromFile(inStream);
		assertFalse(manager.checkConstraints(msg2, "sender"));
	}
	
	@Test
	public void testCheckConstraintsNoConstraints() throws Exception {
		manager.loadFromFile(inStream);
		assertTrue(manager.checkConstraints(msg3, "sender"));
	}
	
	@After
	public void cleanup() {
		manager = null;
		msg1 = null;
		msg2 = null;
		msg3 = null;
		inStream = null;
	}
	
}
