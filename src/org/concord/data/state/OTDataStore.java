

/*
 *  Copyright (C) 2004  The Concord Consortium, Inc.,
 *  10 Concord Crossing, Concord, MA 01741
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
 */
/*
 * Last modification information:
 * $Revision: 1.4 $
 * $Date: 2005-02-22 21:07:25 $
 * $Author: eburke $
 *
 * Licence Information
 * Copyright 2004 The Concord Consortium 
*/
package org.concord.data.state;

import java.io.IOException;
import java.util.Vector;

import org.concord.data.Unit;
import org.concord.data.stream.DataStoreUtil;
import org.concord.framework.data.stream.DataChannelDescription;
import org.concord.framework.data.stream.DataStoreEvent;
import org.concord.framework.data.stream.DataStoreListener;
import org.concord.framework.data.stream.WritableDataStore;
import org.concord.framework.otrunk.DefaultOTObject;
import org.concord.framework.otrunk.OTObjectList;
import org.concord.framework.otrunk.OTResourceList;
import org.concord.framework.otrunk.OTResourceSchema;


/**
 * PfDataStore
 * Class name and description
 *
 * Date created: Nov 18, 2004
 *
 * @author scott<p>
 *
 */
public class OTDataStore extends DefaultOTObject
	implements WritableDataStore
{
	public static interface ResourceSchema extends OTResourceSchema
	{
		public static int DEFAULT_numberChannels = -1;
		public int getNumberChannels();
		public void setNumberChannels(int cols);
		
		public OTResourceList getValues();

		public OTObjectList getChannelDescriptions();
		
		public String getValuesString();
		public void setValuesString(String values);
	};
	
	protected ResourceSchema resources;
	protected Vector dataStoreListeners = new Vector();
	DataStoreEvent changeEvent = new DataStoreEvent(this, DataStoreEvent.DATA_CHANGED);
	
	public OTDataStore(ResourceSchema resources)
	{
		super(resources);
		this.resources = resources;
	}
	
	public void init()
	{
		String valueStr = resources.getValuesString();
		if(valueStr == null) return;
		
		int numChannels = resources.getNumberChannels();
				
		resources.setValuesString(null);
		try {
			DataStoreUtil.loadData(valueStr, this, false);
		} catch ( IOException e) {
			e.printStackTrace();
		}		
	}
	
	/* (non-Javadoc)
	 * @see org.concord.framework.data.stream.DataStore#clearValues()
	 */
	public void clearValues() 
	{
		resources.getValues().removeAll();
	}
	
	/* (non-Javadoc)
	 * @see org.concord.framework.data.stream.DataStore#getTotalNumChannels()
	 */
	public int getTotalNumChannels() 
	{
		int resNumChan = resources.getNumberChannels();
		if(resNumChan == -1) return 1;
		
		return resNumChan;
	}
	
	/* (non-Javadoc)
	 * @see org.concord.framework.data.stream.DataStore#getTotalNumSamples()
	 */
	public int getTotalNumSamples() 
	{
		int numChannels = getTotalNumChannels();
		
		return resources.getValues().size() / numChannels;
	}
	
	/* (non-Javadoc)
	 * @see org.concord.framework.data.stream.DataStore#getValueAt(int, int)
	 */
	public Object getValueAt(int numSample, int numChannel) 
	{
		int numChannels = getTotalNumChannels();
		
		if(numChannel >= numChannels) return null;
		
		int index = numSample * numChannels + numChannel;
		if(index >= resources.getValues().size()) {
			return null;
		}
		
		return resources.getValues().get(index);
	}
	
	/* (non-Javadoc)
	 * @see org.concord.framework.data.stream.WritableDataStore#setValueAt(int, int, java.lang.Object)
	 */
	public void setValueAt(int numSample, int numChannel, Object value) 
	{
		OTResourceList values = resources.getValues();
		int numChannels = getTotalNumChannels();
		
		if(numChannel >= numChannels) {
			// FIXME
			// increase the number of channels
			// if we have existing data then we need to insert a lot of nulls
			// or something to fill the space.
			numChannels = numChannel+1;
			resources.setNumberChannels(numChannels);
		}
		
		int index = numSample * numChannels + numChannel;
		if(index >= values.size()) {
			values.add(index, value);			
		} else {
			values.set(index, value);
		}
		
		for(int i=0; i<dataStoreListeners.size(); i++) {
			DataStoreListener l = (DataStoreListener)dataStoreListeners.get(i);
			l.dataChanged(changeEvent);			
		}
	}	

	/* (non-Javadoc)
	 * @see org.concord.framework.data.stream.WritableDataStore#removeValueAt(int)
	 */
	public void removeValueAt(int numSample) 
	{
		// FIXME this is not supported yet
	}
	
	/* (non-Javadoc)
	 * @see org.concord.framework.data.stream.WritableDataStore#setDataChannelDescription(int, org.concord.framework.data.stream.DataChannelDescription)
	 */
	public void setDataChannelDescription(int channelIndex,
			DataChannelDescription desc) 
	{
		
	}
	
	/* (non-Javadoc)
	 * @see org.concord.framework.data.stream.DataStore#getDataChannelDescription(int)
	 */
	public DataChannelDescription getDataChannelDescription(int numChannel) 
	{
		OTObjectList channelDescriptions = resources.getChannelDescriptions();
		if(numChannel >= channelDescriptions.size()) {
			return null;
		}
		
		OTDataChannelDescription otChDesc = 
			(OTDataChannelDescription)channelDescriptions.get(numChannel);
		
		DataChannelDescription chDesc = new DataChannelDescription();
		chDesc.setAbsoluteMax(otChDesc.getAbsoluteMax());
		chDesc.setAbsoluteMin(otChDesc.getAbsoluteMin());
		chDesc.setNumericData(true);
		chDesc.setName(otChDesc.getName());
		int precision = otChDesc.getPrecision();
		if(precision != Integer.MAX_VALUE) {
			chDesc.setPrecision(precision);
		}
		chDesc.setRecommendMax(otChDesc.getRecommendMax());
		chDesc.setRecommendMin(otChDesc.getRecommendMin());
		String unitStr = otChDesc.getUnit();
		Unit unit = Unit.findUnit(unitStr);
		chDesc.setUnit(unit);

		return chDesc;
	}
	
	/* (non-Javadoc)
	 * @see org.concord.framework.data.stream.DataStore#removeDataStoreListener(org.concord.framework.data.stream.DataStoreListener)
	 */
	public void removeDataStoreListener(DataStoreListener l) 
	{
		dataStoreListeners.remove(l);
	}
	
	/* (non-Javadoc)
	 * @see org.concord.framework.data.stream.DataStore#addDataStoreListener(org.concord.framework.data.stream.DataStoreListener)
	 */
	public void addDataStoreListener(DataStoreListener l) 
	{
		dataStoreListeners.add(l);
	}
	
}
