package org.concord.data.state;

import org.concord.framework.otrunk.OTObjectList;

public interface OTTimerDataStoreDataProducer
    extends OTDataProducer
{
    @Deprecated
	public OTDataStore getDataStore();
    @Deprecated
	public void setDataStore(OTDataStore dataStore);
    
    public OTObjectList getDataStores();
	
	public final static float DEFAULT_sampleTime = 0.1f; 
	public float getSampleTime();
	public void setSampleTime(float time);
	
	public final static boolean DEFAULT_keepExistingXValues = true; 
    public boolean getKeepExistingXValues();
    public void setKeepExistingXValues(boolean keep);
    
    public final static boolean DEFAULT_startAtZero = true; 
    public boolean getStartAtZero();
    public void setStartAtZero(boolean keep);

    /**
     * This allows you to speed up or slow down real time
     * so it is makes it easier to test some things. Scales
     * larger than 1 will slow time down like zooming in on
     * time.  Scales smaller than 1 will speed it up.
     */
    public final static float DEFAULT_timeScale = 1f;
    public float getTimeScale();
    public void setTimeScale(float scale);
}
