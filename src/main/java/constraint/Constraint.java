package constraint;

import data.MessagePublish;

public abstract class Constraint {
	
	protected String topic;

	public abstract boolean checkConstraint(MessagePublish msg);
	
}
