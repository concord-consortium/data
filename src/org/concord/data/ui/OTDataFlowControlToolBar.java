package org.concord.data.ui;

import org.concord.data.state.OTDataFlow;
import org.concord.framework.otrunk.OTObjectInterface;

public interface OTDataFlowControlToolBar extends OTObjectInterface
{
	public OTDataFlow getDataProducer();
	public void setDataProducer(OTDataFlow dataProducer);
	
	public boolean getAllowQuickRestart();
	public void setAllowQuickRestart(boolean allowQuickRestart);
	public static boolean DEFAULT_allowQuickRestart = false;
}
