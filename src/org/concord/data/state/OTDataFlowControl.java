package org.concord.data.state;

import org.concord.framework.data.DataFlow;
import org.concord.framework.otrunk.OTObject;
import org.concord.framework.otrunk.OTObjectList;


public interface OTDataFlowControl extends OTObject 
{
	public OTObjectList getDataFlows();
}
