package org.concord.data.state;

import org.concord.framework.otrunk.OTObjectInterface;

public interface OTWave extends OTObjectInterface 
{
	public float getWavelength();
	public void setWavelength(float seconds);
	
	public float getAmplitude();
	public void setAmplitude(float amplitude);
}
