package constraint;

import data.MessagePublish;

/**
 * Constraint that ensures that the data from the MessagePublish
 * has a particular length.
 * 
 * @author Jon Ayerdi
 */
public class LengthConstraint extends Constraint {
	
	private int length;
	
	/**
	 * 
	 * @param topic The topic to which this constraint applies
	 * @param length The desired length of the data held by the MessagePublish.
	 */
	public LengthConstraint(String topic, int length) {
		this.topic = topic;
		this.length = length;
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
			return msg.getData().length==length;
		} catch(Exception e) {}
		return false;
	}

	public String toString() {
		return topic+": length="+length;
	}
	
}
