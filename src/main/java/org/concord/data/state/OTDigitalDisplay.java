package org.concord.data.state;

import org.concord.framework.otrunk.OTObjectInterface;

public interface OTDigitalDisplay extends OTObjectInterface 
{
	public OTDataProducer getDataProducer();
	public OTDataStore getDataStore();
	
	public static int DEFAULT_fontSize = 25;
	public int getFontSize();
	public void setFontSize(int points);
}
