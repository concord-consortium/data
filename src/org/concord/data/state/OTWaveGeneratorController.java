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
 * $Revision: 1.2 $
 * $Date: 2007-09-24 18:21:22 $
 * $Author: scytacki $
 *
 * Licence Information
 * Copyright 2004 The Concord Consortium 
*/
package org.concord.data.state;

import org.concord.data.Unit;
import org.concord.data.stream.WaveDataProducer;
import org.concord.framework.data.stream.DataChannelDescription;
import org.concord.framework.data.stream.DataStreamDescription;
import org.concord.framework.otrunk.DefaultOTController;
import org.concord.framework.otrunk.OTObject;


/**
 * PfWGenerator
 * Class name and description
 *
 * Date created: Oct 27, 2004
 *
 * @author scott<p>
 *
 */
public class OTWaveGeneratorController extends DefaultOTController
{
	public static Class[] realObjectClasses = {WaveDataProducer.class};	
	public static Class otObjectClass = OTWaveGenerator.class;

	/* (non-Javadoc)
     * @see org.concord.framework.otrunk.OTController#loadRealObject(java.lang.Object)
     */
    public void loadRealObject(Object realObject)
    {
    	OTWaveGenerator otWave = (OTWaveGenerator) otObject;
    	WaveDataProducer waveProducer = (WaveDataProducer) realObject;
    	
        DataStreamDescription dataDescription = waveProducer.getDataDescription();
        dataDescription.setDt(otWave.getSampleTime());      
        DataChannelDescription chDesc = dataDescription.getDtChannelDescription();
        chDesc.setUnit(Unit.getUnit(Unit.UNIT_CODE_S));
        chDesc.setName("time");

        chDesc = dataDescription.getChannelDescription(0);
        chDesc.setUnit(Unit.getUnit(Unit.UNIT_CODE_METER));
        chDesc.setName("distance");
        
        waveProducer.setTimeScale(otWave.getTimeScale());	    
    }

	/* (non-Javadoc)
     * @see org.concord.framework.otrunk.OTController#registerRealObject(java.lang.Object)
     */
    public void registerRealObject(Object realObject)
    {
    	// Don't need to do anything here
    }

	/* (non-Javadoc)
     * @see org.concord.framework.otrunk.OTController#saveRealObject(java.lang.Object)
     */
    public void saveRealObject(Object realObject)
    {
    	OTWaveGenerator otWave = (OTWaveGenerator) otObject;
    	WaveDataProducer waveProducer = (WaveDataProducer) realObject;

    	DataStreamDescription dataDescription = waveProducer.getDataDescription();
    	
    	otWave.setSampleTime(dataDescription.getDt());
    	
    	otWave.setTimeScale(waveProducer.getTimeScale());
    }
    
    /**
     * Allow our read object to be shared, so it can be listened to by multiple graphs at the same
     * time.
     * 
     * @see org.concord.framework.otrunk.DefaultOTController#isRealObjectSharable(org.concord.framework.otrunk.OTObject, java.lang.Object)
     */
    public boolean isRealObjectSharable(OTObject otObject, Object realObject) 
    {
    	return true;
    }
}
