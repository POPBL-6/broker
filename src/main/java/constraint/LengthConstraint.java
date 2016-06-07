package constraint;

import data.MessagePublish;

public class LengthConstraint extends Constraint {
	
	private int length;
	
	public LengthConstraint(String topic, int length) {
		this.topic = topic;
		this.length = length;
	}

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
