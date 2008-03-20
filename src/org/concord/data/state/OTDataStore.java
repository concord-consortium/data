package org.concord.data.state;

import org.concord.framework.otrunk.OTObjectInterface;
import org.concord.framework.otrunk.OTObjectList;
import org.concord.framework.otrunk.OTResourceList;

public interface OTDataStore extends OTObjectInterface {

	/**
	 * The numberChannels property refers to the number of channels actually
	 * stored in the values property.
	 * 
	 * If virtual channels is turned on and there is a dt then
	 * the first channel (channel 0) is not stored in values property.  In 
	 * which case should return 1 even though the 
	 * OTDataStoreRealObject.getTotalNumChannels will return 2.
	 * 
	 * This is for consistency so the only difference between a a data store with
	 * virtual channels and one without is the virtualChannels property.  Also this
	 * it is easier to write a parser for the values property.  The only thing the
	 * parser needs to know is the numberChannels.
	 * 
	 * @return
	 */
	public int getNumberChannels();
	public static int DEFAULT_numberChannels = -1;
	public void setNumberChannels(int cols);
	
	/**
     * If the dt is set then the first channel
     * description will be for the dt channel
     */
    public static float DEFAULT_dt = Float.NaN;
    public float getDt();
    public void setDt(float dt);
    
	public OTResourceList getValues();

	public OTObjectList getChannelDescriptions();
	
	public String getValuesString();
	public void setValuesString(String values);	
	
	public static boolean DEFAULT_virtualChannels = false;
	public boolean isVirtualChannels();
	public void setVirtualChannels(boolean flag);
	
	/**
	 * We can provide a name if this data store is going to be shared
	 */
	public String getName();
	public void setName(String name);
}
