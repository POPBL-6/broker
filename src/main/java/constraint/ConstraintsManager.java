package constraint;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import data.MessagePublish;

/**
 * Manages all the MessagePublish constraints for the Broker.
 * Configuration should be loaded by calling 
 * loadFromFile() or loadFromFile(InputStream)
 * (The former reads from ConstraintsManager.DEFAULT_FILE).
 * 
 * @author Jon Ayerdi
 */
public class ConstraintsManager {
	
	public static final String DEFAULT_FILE = "constraints.ini";
	
	private static final Logger logger = LogManager.getLogger(ConstraintsManager.class);
	
	private Map<String,List<Constraint>> constraints;

	public ConstraintsManager() {
		constraints = Collections.synchronizedMap(new HashMap<String,List<Constraint>>());
	}
	
	/**
	 * Calls loadFromFile(InputStream) with the FileInputStream of
	 * the file ConstraintsManager.DEFAULT_FILE.
	 * 
	 * @throws IOException
	 * @throws Exception
	 */
	public void loadFromFile() throws IOException, Exception {
		loadFromFile(new FileInputStream(DEFAULT_FILE));
	}
	
	/**
	 * <p>Reads the constraints configuration from the provided InputStream
	 * and creates the appropriate Constraints, which will be later
	 * used when calling checkConstraints(MessagePublish,String).</p>
	 * <p>The format should be:</p>
	 * <pre>
	 * [TopicName]
	 * {
	 *   [constraint]=[value]
	 *   [constraint]=[value]
	 *   [...]
	 * }
	 * [TopicName]
	 * {
	 *   [...]
	 * }
	 * [...]
	 * </pre>
	 * 
	 * <p>[constraint]=[value] might be something like <i>length=5</i> or
	 * something like <i>class=java.lang.String</i></p>
	 * 
	 * @param inStream The InputStream from which to read the configuration.
	 * @throws IOException
	 * @throws Exception
	 */
	public void loadFromFile(InputStream inStream) throws IOException, Exception {
		String input;
		List<String> lines;
		Scanner in = new Scanner(inStream);
		while(in.hasNextLine()) {
			lines = new ArrayList<String>();
			do {
				input = in.nextLine();
				if(!input.trim().equals(""))
					lines.add(input);
			} while(in.hasNextLine() && !input.equals("}"));
			loadTopicContstraints(lines);
		}
		in.close();
	}
	
	/**
	 * Creates the constraints for a single topic according to the
	 * provided configuration. <i>The first line must be the name of the topic.</i>
	 * 
	 * @param lines The lines from the configuration for a particular topic.
	 * @throws ClassNotFoundException
	 */
	private void loadTopicContstraints(List<String> lines) throws ClassNotFoundException {
		String topic = lines.get(0);
		List<Constraint> topicConstraints = constraints.get(topic);
		if(topicConstraints == null) {
			topicConstraints = new ArrayList<Constraint>();
			constraints.put(topic, topicConstraints);
		}
		for(int i = 1 ; i < lines.size() ; i++) {
			String[] args = lines.get(i).split("[=]");
			switch(args[0].trim()) {
			case "length":
			case "LENGTH":
			case "Length":
				topicConstraints.add(new LengthConstraint(topic,Integer.valueOf(args[1].trim())));
				break;
			case "class":
			case "CLASS":
			case "Class":
				topicConstraints.add(new ClassConstraint(topic,args[1].trim()));
				break;
			default:
				break;
			}
		}
	}
	
	/**
	 * Checks whether a MessagePublish should be rejected or not
	 * according to the constraints loaded from in loadTopicContstraints(InputStream).
	 * 
	 * @param msg The received MessagePublish.
	 * @param sender String representing the sender of the MessagePublish.
	 * @return false if the MessagePublish should be rejected, true otherwise.
	 */
	public boolean checkConstraints(MessagePublish msg, String sender) {
		List<Constraint> topicConstraints = constraints.get(msg.getTopic());
		if(topicConstraints!=null) {
			for(Constraint constraint : topicConstraints) {
				if(!constraint.checkConstraint(msg)) {
					logger.warn("Message from "+sender+" dropped, constraint "+constraint+" broken");
					return false;
				}
			}
		}
		return true;
	}
	
}
