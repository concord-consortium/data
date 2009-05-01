package org.concord.data.ui;

import org.concord.data.state.OTStartable;
import org.concord.framework.otrunk.OTObjectInterface;

public interface OTStartableToolBar extends OTObjectInterface 
{
	public OTStartable getStartable();
}
