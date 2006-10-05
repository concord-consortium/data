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
 * $Revision: 1.1 $
 * $Date: 2006-09-29 21:06:15 $
 * $Author: scytacki $
 *
 * Licence Information
 * Copyright 2004 The Concord Consortium 
*/
package org.concord.data.state;

import org.concord.data.Unit;
import org.concord.data.stream.PropertyDataProducer;
import org.concord.data.stream.WaveDataProducer;
import org.concord.framework.data.stream.DataChannelDescription;
import org.concord.framework.data.stream.DataProducer;
import org.concord.framework.data.stream.DataStreamDescription;
import org.concord.framework.otrunk.OTObject;
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
public class OTPropertyDataProducer extends OTDelegatingDataProducer
	implements Copyable
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
        
        public OTObject getTarget();
        
        public String getProperty();
	}
	
	private ResourceSchema resources;
	public OTPropertyDataProducer(ResourceSchema resources) 
	{
		super(resources);
		this.resources = resources;		
	}
	
    public void init()
    {
        float sampleTime = resources.getSampleTime();
        PropertyDataProducer dataProducer = new PropertyDataProducer();
        DataStreamDescription dataDescription = dataProducer.getDataDescription();
        dataDescription.setDt(sampleTime);      
        DataChannelDescription chDesc = dataDescription.getDtChannelDescription();
        chDesc.setUnit(Unit.getUnit(Unit.UNIT_CODE_S));
        chDesc.setName("time");

        chDesc = dataDescription.getChannelDescription(0);
        chDesc.setUnit(Unit.getUnit(Unit.UNIT_CODE_METER));
        chDesc.setName("distance");
        
        dataProducer.setTimeScale(resources.getTimeScale());
        
        dataProducer.setProperty(resources.getTarget(), 
        		resources.getProperty());
        setMyDataProducer(dataProducer);
    }
    
	public Object getCopy() {
		OTObjectService service = getOTObjectService();
		
		OTPropertyDataProducer generator = null;
		try {
			generator = (OTPropertyDataProducer)service.createObject(OTPropertyDataProducer.class);
			generator.setMyDataProducer((DataProducer)((WaveDataProducer)(getMyDataProducer())).getCopy());
			generator.resources = this.resources;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//generator.myProducer = this.myProducer;
		return generator;
	}
}