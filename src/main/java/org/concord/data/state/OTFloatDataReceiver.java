package org.concord.data.state;

import org.concord.framework.otrunk.wrapper.OTFloat;

public interface OTFloatDataReceiver extends OTFloat
{
	public OTDataProducer getDataProducer();
	public void setDataProducer(OTDataProducer dataProducer);
}
