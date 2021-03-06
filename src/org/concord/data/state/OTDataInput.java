package org.concord.data.state;

import org.concord.framework.otrunk.OTObject;
import org.concord.framework.otrunk.OTObjectInterface;
import org.concord.framework.otrunk.OTResourceMap;

/**
 * This class is used to add a data producer to a model (or anything else) with
 * the inclusion of named variables or channels of a specified target which will
 * accept the input data. It is currently used by OTModelerPage and
 * OTNLogoModel. If the target is not specified, then the input variable is
 * assumed to be associated to the entire model (QX).
 * 
 * @author sfentress
 * 
 */
public interface OTDataInput extends OTObjectInterface {

	public OTDataProducer getDataProducer();

	public void setDataProducer(OTDataProducer producer);

	// optional (QX)
	public OTObject getTarget();

	public void setTarget(OTObject target);

	// use channelVariableMap instead
	@Deprecated
	public String getInputVariable();
	@Deprecated
	public void setInputVariable(String inputVariable);

	// use channelVariableMap instead
	@Deprecated
	public int getInputChannel();
	@Deprecated
	public void setInputChannel(int inputChannel);

	public static int DEFAULT_inputChannel = 0;
	
	public OTResourceMap getChannelVariableMap();
	public OTResourceMap getChannelFunctionMap();
}
