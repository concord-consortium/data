package org.concord.data.state;

import org.concord.framework.otrunk.OTObjectInterface;
import org.concord.framework.otrunk.OTObjectList;
import org.concord.framework.otrunk.OTResourceList;

public interface OTDataStore extends OTObjectInterface {
	public static int DEFAULT_numberChannels = -1;
	public int getNumberChannels();
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
}
