

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
 * $Revision: 1.15 $
 * $Date: 2005-04-19 15:45:47 $
 * $Author: scytacki $
 *
 * Licence Information
 * Copyright 2004 The Concord Consortium 
*/
package org.concord.data.state;

import java.io.IOException;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

import org.concord.data.Unit;
import org.concord.data.stream.DataStoreUtil;
import org.concord.framework.data.stream.DataChannelDescription;
import org.concord.framework.data.stream.DataStoreEvent;
import org.concord.framework.data.stream.DataStoreListener;
import org.concord.framework.data.stream.ProducerDataStore;
import org.concord.framework.data.stream.WritableArrayDataStore;
import org.concord.framework.data.stream.WritableDataStore;
import org.concord.framework.otrunk.OTID;
import org.concord.framework.otrunk.OTObject;
import org.concord.framework.otrunk.OTObjectList;
import org.concord.framework.otrunk.OTResourceList;
import org.concord.framework.otrunk.OTResourceSchema;


/**
 * OTDataStore
 * Class name and description
 *
 * Date created: Nov 18, 2004
 *
 * @author scytacki<p>
 *
 */
public class OTDataStore extends ProducerDataStore
	implements WritableArrayDataStore, OTObject
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
	DataStoreEvent changeEvent = new DataStoreEvent(this, DataStoreEvent.DATA_CHANGED);
	DataStoreEvent removeEvent = new DataStoreEvent(this, DataStoreEvent.DATA_REMOVED);
	
	public OTDataStore(ResourceSchema resources)
	{
	    this.resources = resources;
	}
	
	/* (non-Javadoc)
     * @see org.concord.framework.otrunk.OTObject#getGlobalId()
     */
    public OTID getGlobalId()
    {
        return resources.getGlobalId();
    }

    /* (non-Javadoc)
     * @see org.concord.framework.otrunk.OTObject#getName()
     */
    public String getName()
    {
        return resources.getName();
    }

    public void setName(String name)
    {
        resources.setName(name);
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
		
		notifyDataRemoved();
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
	public synchronized int getTotalNumSamples() 
	{
		int numChannels = getTotalNumChannels();
		
		OTResourceList values = resources.getValues();
		int size = values.size();
		return size / numChannels;
	}
	
	/* (non-Javadoc)
	 * @see org.concord.framework.data.stream.DataStore#getValueAt(int, int)
	 */
	public synchronized Object getValueAt(int numSample, int numChannel) 
	{
		//Special case: when dt is a channel, it's the channel -1
		if (numChannel == -1){
			return new Float(numSample * dt);
		}
		
		int numChannels = getTotalNumChannels();
		
		if(numChannel >= numChannels) return null;
		
		int index = numSample * numChannels + numChannel;
		if(index >= resources.getValues().size()) {
			return null;
		}
		
		return resources.getValues().get(index);
	}

	public void setValueAt(int numSample, int numChannel, Object value) 
	{
	    setValueAt(numSample, numChannel, value, true);
	}
	
	/* (non-Javadoc)
	 * @see org.concord.framework.data.stream.WritableDataStore#setValueAt(int, int, java.lang.Object)
	 */
	public void setValueAt(int numSample, int numChannel, Object value, boolean doNotify) 
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
		
		if(doNotify) {
		    notifyDataChanged();
		}		
	}	

	
	public void setValues(int numbChannels,float []values)
	{
	    for(int i=0;i<values.length;i++) {
	        int channelNumber = i%numbChannels;
	        setValueAt(i/numbChannels, channelNumber, 
	                new Float(values[i]), false);
	    }
	    
	    notifyDataChanged();
	}
	
	/**
	 * Adds a value to the channel indicated
	 * If the channel doesn't exist, it doesn't do anything
	 *
	 * @param numChannel	channel number, starting from 0, >0
	 * @param value			value to add
	 */
	protected void addValue(int numSample, int numChannel, Object value)
	{
	    setValueAt(numSample, numChannel, value);	    
	}
		
	protected void addSamples(float [] values, int offset, 
	        int numberOfSamples, int localNextSampleOffset)
	{
		synchronized(this) {
			int numChannels = getNumberOfProducerChannels();
			int firstSample = getTotalNumSamples();

		    for(int i=0; i<numberOfSamples; i++) {
		        for(int j=0; j<numChannels; j++) {
		            Float value = new Float(values[offset+(i*localNextSampleOffset)+j]);
		            setValueAt(firstSample + i, j, value, false);
		        }
		    }
		}
		
	    // notifyDataAdded();	
	    notifyDataChanged();
	}
	
	/**
	 * @see org.concord.framework.data.stream.WritableDataStore#removeSampleAt(int)
	 */
	public void removeSampleAt(int numSample) 
	{
		OTResourceList values = resources.getValues();
		int numChannels = getTotalNumChannels();
		
		int index = numSample * numChannels;

		for(int i=0; i<numChannels; i++) {
		    values.remove(index);
		}

		notifyDataRemoved();
	}
	
	/**
	 * @see org.concord.framework.data.stream.WritableDataStore#insertSampleAt(int)
	 */
	public void insertSampleAt(int numSample)
	{
		OTResourceList values = resources.getValues();
		int numChannels = getTotalNumChannels();
		
		int index = numSample * numChannels;

		for(int i=0; i<numChannels; i++) {
		    values.add(index, null);
		}

		notifyDataChanged();
	}	
	
	/* (non-Javadoc)
	 * @see org.concord.framework.data.stream.WritableDataStore#setDataChannelDescription(int, org.concord.framework.data.stream.DataChannelDescription)
	 */
	public void setDataChannelDescription(int channelIndex,
			DataChannelDescription desc) 
	{
		// FIXME this is not supported yet
		throw new UnsupportedOperationException("org.concord.framework.data.stream.WritableDataStore.setDataChannelDescription not supported yet");
	}
	
	/* (non-Javadoc)
	 * @see org.concord.framework.data.stream.DataStore#getDataChannelDescription(int)
	 */
	public DataChannelDescription getDataChannelDescription(int numChannel) 
	{
	    if(getDataProducer() != null) {
	        // need to make sure that these values are saved
	        // so if this data store is disconnected from the 
	        // the data producer it will preserve this info
	        return super.getDataChannelDescription(numChannel);
	    }
	    
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
     * @see org.concord.framework.data.stream.WritableArrayDataStore#setDt(float)
     */
    public void setDt(float dt)
    {
        // FIXME
        throw new RuntimeException("unimplemented");
    }
	
    /* (non-Javadoc)
     * @see org.concord.framework.data.stream.WritableArrayDataStore#setValues(int, float[], int, int, int)
     */
    public void setValues(int numChannels, float[] values, int offset,
            int numSamples, int nextSampleOffset)
    {
        // FIXME
        throw new RuntimeException("unimplemented");
    }
    
	/**
	 * @see org.concord.framework.data.stream.DataStore#addDataStoreListener(org.concord.framework.data.stream.DataStoreListener)
	 */
	public void addDataStoreListener(DataStoreListener l)
	{
	    WeakReference ref = new WeakReference(l);
		if (!dataStoreListeners.contains(ref)){
			dataStoreListeners.add(ref);
		}
	}

	/**
	 * @see org.concord.framework.data.stream.DataStore#removeDataStoreListener(org.concord.framework.data.stream.DataStoreListener)
	 */
	public void removeDataStoreListener(DataStoreListener l)
	{
	    WeakReference ref = new WeakReference(l);
		dataStoreListeners.remove(ref);		
	}

	protected void notifyDataAdded()
	{
		DataStoreEvent evt = new DataStoreEvent(this, DataStoreEvent.DATA_ADDED);
		DataStoreListener l;
		for (int i=0; i<dataStoreListeners.size(); i++){
		    Reference ref = (Reference)dataStoreListeners.elementAt(i);
			l = (DataStoreListener)ref.get();
			
			// ignore references that have been gc'd
			if(l == null) continue;

			l.dataAdded(evt);
		}
	}
	
	protected void notifyDataRemoved()
	{
		DataStoreEvent evt = new DataStoreEvent(this, DataStoreEvent.DATA_ADDED);
		DataStoreListener l;
		for (int i=0; i<dataStoreListeners.size(); i++){
		    Reference ref = (Reference)dataStoreListeners.elementAt(i);
			l = (DataStoreListener)ref.get();
			
			// ignore references that have been gc'd
			if(l == null) continue;

			l.dataRemoved(evt);
		}
	}
	
	protected void notifyDataChanged()
	{
		DataStoreEvent evt = new DataStoreEvent(this, DataStoreEvent.DATA_ADDED);
		DataStoreListener l;
		for (int i=0; i<dataStoreListeners.size(); i++){
		    Reference ref = (Reference)dataStoreListeners.elementAt(i);
			l = (DataStoreListener)ref.get();
			
			// ignore references that have been gc'd
			if(l == null) continue;

			l.dataChanged(evt);
		}
	}
	
	protected void notifyChannelDescChanged()
	{
		DataStoreEvent evt = new DataStoreEvent(this, DataStoreEvent.DATA_DESC_CHANGED);
		DataStoreListener l;
		for (int i=0; i<dataStoreListeners.size(); i++){
		    Reference ref = (Reference)dataStoreListeners.elementAt(i);
			l = (DataStoreListener)ref.get();
			
			// ignore references that have been gc'd
			if(l == null) continue;

			l.dataChannelDescChanged(evt);
		}
	}

}
