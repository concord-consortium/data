/*
 *  Copyright (C) 2004  The Concord Consortium, Inc.,
 *  10 Concord Crossing, Concord, MA 01742
 *
 *  Web Site: http://www.concord.org
 *  Email: info@concord.org
 *
 *  This library is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public
 *  License as published by the Free Software Foundation; either
 *  version 2.1 of the License, or (at your option) any later version.
 *
 *  This library is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *  Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public
 *  License along with this library; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 * END LICENSE */

/*
 * Last modification information:
 * $Revision: 1.8 $
 * $Date: 2005-09-23 19:57:49 $
 * $Author: swang $
 *
 * Licence Information
 * Copyright 2004 The Concord Consortium 
*/
package org.concord.data.state;

import org.concord.data.Unit;
import org.concord.data.stream.WaveDataProducer;
import org.concord.framework.data.stream.DataChannelDescription;
import org.concord.framework.data.stream.DataListener;
import org.concord.framework.data.stream.DataProducer;
import org.concord.framework.data.stream.DataStreamDescription;
import org.concord.framework.data.stream.DefaultDataProducer;
import org.concord.framework.otrunk.DefaultOTObject;
import org.concord.framework.otrunk.OTObjectService;
import org.concord.framework.otrunk.OTResourceSchema;
import org.concord.framework.util.Copyable;


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
	implements DataProducer, Copyable
{
	public static interface ResourceSchema extends OTResourceSchema {
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
	
    public void init()
    {
        float sampleTime = resources.getSampleTime();
        DataStreamDescription dataDescription = myProducer.getDataDescription();
        dataDescription.setDt(sampleTime);      
        DataChannelDescription chDesc = dataDescription.getDtChannelDescription();
        chDesc.setUnit(Unit.getUnit(Unit.UNIT_CODE_S));
        chDesc.setName("time");

        chDesc = dataDescription.getChannelDescription(0);
        chDesc.setUnit(Unit.getUnit(Unit.UNIT_CODE_METER));
        chDesc.setName("distance");
        
        myProducer.setTimeScale(resources.getTimeScale());
    }
    
	public Object getCopy() {
		OTObjectService service = getOTObjectService();
		
		OTWaveGenerator generator = null;
		try {
			generator = (OTWaveGenerator)service.createObject(OTWaveGenerator.class);
			generator.myProducer = (WaveDataProducer)this.myProducer.getCopy();
			generator.resources = this.resources;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//generator.myProducer = this.myProducer;
		return generator;
	}
}
