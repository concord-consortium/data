/*
 * Last modification information:
 * $Revision: 1.1 $
 * $Date: 2005-05-19 17:03:12 $
 * $Author: scytacki $
 *
 * Licence Information
 * Copyright 2004 The Concord Consortium 
*/
package org.concord.data.state;

import org.concord.data.stream.WaveDataProducer;
import org.concord.framework.data.stream.DataListener;
import org.concord.framework.data.stream.DataProducer;
import org.concord.framework.data.stream.DataStreamDescription;
import org.concord.framework.otrunk.DefaultOTObject;
import org.concord.framework.otrunk.OTResourceSchema;


/**
 * PfWGenerator
 * Class name and description
 *
 * Date created: Oct 27, 2004
 *
 * @author scott<p>
 *
 */
public class OTWaveGenerator extends DefaultOTObject
	implements DataProducer
{
	public static interface ResourceSchema extends OTResourceSchema {
		public float getSampleTime();
		public void putSampleTime(float time);
	}
	
	private ResourceSchema resources;
	public OTWaveGenerator(ResourceSchema resources) 
	{
		super(resources);
		this.resources = resources;		
	}

	WaveDataProducer myProducer = new WaveDataProducer();
	
	/*
	public void init()
	{
		Float sampleTime = (Float)getResource(RES_SAMPLE_TIME);
		DataStreamDescription dataDescription = myProducer.getDataDescription();
		dataDescription.setDt(sampleTime.floatValue());		
	}
	*/
	
	/* (non-Javadoc)
	 * @see org.concord.framework.data.stream.DataProducer#addDataListener(org.concord.framework.data.stream.DataListener)
	 */
	public void addDataListener(DataListener listener)
	{
		myProducer.addDataListener(listener);
	}
	
	
	/* (non-Javadoc)
	 * @see org.concord.framework.data.stream.DataProducer#getDataDescription()
	 */
	public DataStreamDescription getDataDescription()
	{
		float sampleTime = resources.getSampleTime();
		DataStreamDescription dataDescription = myProducer.getDataDescription();
		dataDescription.setDt(sampleTime);		

		return myProducer.getDataDescription();
	}
	
	
	/* (non-Javadoc)
	 * @see org.concord.framework.data.stream.DataProducer#removeDataListener(org.concord.framework.data.stream.DataListener)
	 */
	public void removeDataListener(DataListener listener)
	{
		myProducer.removeDataListener(listener);		
	}
	
	/* (non-Javadoc)
	 * @see org.concord.framework.data.DataFlow#reset()
	 */
	public void reset()
	{
		myProducer.reset();
	}
	
	
	/* (non-Javadoc)
	 * @see org.concord.framework.data.DataFlow#start()
	 */
	public void start()
	{
		myProducer.start();
	}
	
	/* (non-Javadoc)
	 * @see org.concord.framework.data.DataFlow#stop()
	 */
	public void stop()
	{
		myProducer.stop();
	}
	

}
