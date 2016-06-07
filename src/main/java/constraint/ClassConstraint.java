package constraint;

import data.MessagePublish;

/**
 * Constraint that ensures that the data from the MessagePublish
 * is a serialized instance of the specified class
 * 
 * @author Jon Ayerdi
 */
public class ClassConstraint extends Constraint {
	
	@SuppressWarnings("rawtypes")
	private Class clazz;
	
	/**
	 * 
	 * @param topic The topic to which this constraint applies
	 * @param className Name of the class of which the MessagePublish must have an instance.
	 * @throws ClassNotFoundException If the class represented by the provided className cannot be found.
	 */
	public ClassConstraint(String topic, String className) throws ClassNotFoundException {
		this(topic,Class.forName(className));
	}
	
	
	/**
	 * 
	 * @param topic The topic to which this constraint applies
	 * @param clazz The class of which the MessagePublish must have an instance.
	 */
	@SuppressWarnings("rawtypes")
	public ClassConstraint(String topic, Class clazz) {
		this.topic = topic;
		this.clazz = clazz;
	}

	/**
	 * Checks whether a MessagePublish should be rejected or not
	 * according to this particular Constraint.
	 * 
	 * @param msg The received MessagePublish
	 * @return false if the MessagePublish should be rejected, true otherwise.
	 */
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
