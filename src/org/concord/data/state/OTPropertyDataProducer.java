/**
 * 
 */
package org.concord.data.state;

import org.concord.framework.otrunk.OTObject;

public interface OTPropertyDataProducer extends OTDataProducer
{
    public final static float DEFAULT_sampleTime = 0.1f; 
	public float getSampleTime();
	public void setSampleTime(float time);

    /**
     * This allows you to speed up or slow down real time
     * so it is makes it easier to test some things. Scales
     * larger than 1 will slow time down like zooming in on
     * time.  Scales smaller than 1 will speed it up.
     */
    public final static float DEFAULT_timeScale = 1f;
    public float getTimeScale();
    public void setTimeScale(float scale);
    
    public OTObject getTarget();
    public void setTarget(OTObject object);
    
    public String getProperty();
    public void setProperty(String property);
}