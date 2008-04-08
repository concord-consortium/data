package org.concord.data.state;

import org.concord.framework.otrunk.OTObjectInterface;

/**
 * This class is used to add a data producer to a model (or anything else) with the
 * inclusion of named variables or channels which will accept the input data. 
 * It is currently used by OTModelerPage and OTNLogoModel.
 * 
 * @author sfentress
 *
 */
public interface OTDataInput extends OTObjectInterface
{
	public OTDataProducer getDataProducer();
	public void setDataProducer(OTDataProducer producer);
	
	//optional
	public String getInputVariable();
	public void setInputVariable(String inputVariable);
	
	//optional
	public int getInputChannel();
	public void setInputChannel(int inputChannel);
	public static int DEFAULT_inputChannel = 0;
}
