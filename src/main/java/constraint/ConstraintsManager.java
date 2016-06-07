package constraint;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import manager.ConnectionsManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import data.MessagePublish;

public class ConstraintsManager {
	
	public static final String DEFAULT_FILE = "constraints.ini";
	
	private static final Logger logger = LogManager.getLogger(ConnectionsManager.class);
	
	private Map<String,List<Constraint>> constraints;

	public ConstraintsManager() {
		constraints = Collections.synchronizedMap(new HashMap<String,List<Constraint>>());
	}
	
	public void loadFromFile() throws IOException, Exception {
		loadFromFile(DEFAULT_FILE);
	}
	
	public void loadFromFile(String file) throws IOException, Exception {
		String input;
		List<String> lines;
		Scanner in = new Scanner(new FileInputStream(file));
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
