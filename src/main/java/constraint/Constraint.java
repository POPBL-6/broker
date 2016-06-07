package constraint;

import data.MessagePublish;

/**
 * Abstract class for constraints that define the rules for the
 * Broker to accept or reject a MessagePublish from clients.
 * 
 * @author Jon Ayerdi
 */
public abstract class Constraint {
	
	protected String topic;

	/**
	 * Checks whether a MessagePublish should be rejected or not
	 * according to this particular Constraint.
	 * 
	 * @param msg The received MessagePublish
	 * @return false if the MessagePublish should be rejected, true otherwise.
	 */
	public abstract boolean checkConstraint(MessagePublish msg);
	
}
