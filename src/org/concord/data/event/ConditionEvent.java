package org.concord.data.event;

import java.util.EventObject;
import org.concord.data.ConditionValue;

public class ConditionEvent extends EventObject
{
    public ConditionEvent(Object object)
	{
		super(object);
    }
    
    public ConditionValue getConditionValue()
	{
		return (ConditionValue) (this.getSource() instanceof ConditionValue ? this.getSource() : null);
    }
}
