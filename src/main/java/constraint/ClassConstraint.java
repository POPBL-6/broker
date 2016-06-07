package constraint;

import data.MessagePublish;

@SuppressWarnings("rawtypes")
public class ClassConstraint extends Constraint {
	
	private Class clazz;
	
	public ClassConstraint(String topic, String className) throws ClassNotFoundException {
		this(topic,Class.forName(className));
	}
	
	public ClassConstraint(String topic, Class clazz) {
		this.topic = topic;
		this.clazz = clazz;
	}

	public boolean checkConstraint(MessagePublish msg) {
		try {
			return clazz.isInstance(msg.getDataObject());
		} catch(Exception e) {}
		return false;
	}
	
	public String toString() {
		return topic+": class="+clazz.getName();
	}

}
