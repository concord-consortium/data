package org.concord.data.ui;

import org.concord.data.state.OTDataProducer;
import org.concord.framework.otrunk.OTObjectInterface;

public interface OTDataFlowControlToolBar extends OTObjectInterface
{
	public OTDataProducer getDataProducer();
	public void setDataProducer(OTDataProducer dataProducer);
	
	public boolean getAllowQuickRestart();
	public void setAllowQuickRestart(boolean allowQuickRestart);
	public static boolean DEFAULT_allowQuickRestart = false;
}
