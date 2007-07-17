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
 * $Date: 2007-07-17 19:47:54 $
 * $Author: scytacki $
 *
 * Licence Information
 * Copyright 2004 The Concord Consortium 
*/
package org.concord.data.state;

import java.io.IOException;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.Vector;

import org.concord.data.Unit;
import org.concord.data.stream.DataStoreUtil;
import org.concord.data.stream.ProducerDataStore;
import org.concord.framework.data.stream.DataChannelDescription;
import org.concord.framework.data.stream.DataStoreEvent;
import org.concord.framework.data.stream.DataStoreListener;
import org.concord.framework.data.stream.DataStreamDescription;
import org.concord.framework.data.stream.WritableArrayDataStore;
import org.concord.framework.otrunk.OTChangeEvent;
import org.concord.framework.otrunk.OTChangeListener;
import org.concord.framework.otrunk.OTObjectList;
import org.concord.framework.otrunk.OTObjectService;
import org.concord.framework.otrunk.OTResourceList;


/**
 * OTDataStore
 * Class name and description
 *
 * Date created: Nov 18, 2004
 *
 * @author scytacki<p>
 *
 */
public class OTDataStoreRealObject extends ProducerDataStore
	implements WritableArrayDataStore
{	
	protected OTDataStore otDataStore;
	DataStoreEvent changeEvent = new DataStoreEvent(this, DataStoreEvent.DATA_CHANGED);
	DataStoreEvent removeEvent = new DataStoreEvent(this, DataStoreEvent.DATA_REMOVED);
		
	private OTChangeListener myListener = new OTChangeListener(){

		public void stateChanged(OTChangeEvent e) {
			if("values".equals(e.getProperty())){
			    String op = e.getOperation();
			    if(OTChangeEvent.OP_ADD == op){
			    	notifyDataAdded();
			    } else if(OTChangeEvent.OP_CHANGE == op){
			    	notifyDataChanged();
			    } else if(OTChangeEvent.OP_REMOVE == op ||
			    		OTChangeEvent.OP_REMOVE_ALL == op){
			    	notifyDataRemoved();
			    }
			} else {
				// any other changes are being ignored right now
				
			}			
		}
		
	};
	
	public OTDataStoreRealObject()
	{
		System.err.println("creating OTDataStoreRealObject: " + this);
	}
	
	/**
	 * 
	 */
	public void setOTDataStore(OTDataStore otDataStore)
	{
		this.otDataStore = otDataStore;
		
		// We need to listen to the otDataStore so when it changes we can 
		// throw a datastore change or remove event
		otDataStore.addOTChangeListener(myListener);

		String valueStr = otDataStore.getValuesString();
		if(valueStr == null) return;
						
		otDataStore.setValuesString(null);
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
		otDataStore.getValues().removeAll();
		
		// this will happen when otDataStore notifies our listener that the data was removed
		// notifyDataRemoved();
	}
	
	/* (non-Javadoc)
	 * @see org.concord.framework.data.stream.DataStore#getTotalNumChannels()
	 */
	public int getTotalNumChannels() 
	{
		int resNumChan = otDataStore.getNumberChannels();
		if(resNumChan == -1) return 1;
		
		return resNumChan;
	}
	
	/* (non-Javadoc)
	 * @see org.concord.framework.data.stream.DataStore#getTotalNumSamples()
	 */
	public synchronized int getTotalNumSamples() 
	{
		int numChannels = getTotalNumChannels();
		
		OTResourceList values = otDataStore.getValues();
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
			return new Float(numSample * getDt());
		}
		
		int numChannels = getTotalNumChannels();
		
		if(numChannel >= numChannels) return null;
		
		int index = numSample * numChannels + numChannel;
		if(index >= otDataStore.getValues().size()) {
			return null;
		}
		
		return otDataStore.getValues().get(index);
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
		OTResourceList values = otDataStore.getValues();
		int numChannels = getTotalNumChannels();
		
		if(numChannel >= numChannels) {
			// FIXME
			// increase the number of channels
			// if we have existing data then we need to insert a lot of nulls
			// or something to fill the space.
			numChannels = numChannel+1;
			otDataStore.setNumberChannels(numChannels);
		}
		
		int index = numSample * numChannels + numChannel;
		if(index >= values.size()) {
			values.add(index, value);			
		} else {
			values.set(index, value);
		}
		
		/*
		 * this is taken care of by our listener on the otDataStore
		if(doNotify) {
		    notifyDataChanged();
		}
		*/		
	}	

	
	public void setValues(int numbChannels,float []values)
	{
        otDataStore.setDoNotifyChangeListeners(false);

	    for(int i=0;i<values.length;i++) {
	        int channelNumber = i%numbChannels;
	        setValueAt(i/numbChannels, channelNumber, 
	                new Float(values[i]), false);
	    }
	    
	    
        otDataStore.setDoNotifyChangeListeners(true);

        notifyOTValuesChange();
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
		synchronized(otDataStore) {
	        otDataStore.setDoNotifyChangeListeners(false);

			int numChannels = getNumberOfProducerChannels();
			int firstSample = getTotalNumSamples();

		    for(int i=0; i<numberOfSamples; i++) {
		        for(int j=0; j<numChannels; j++) {
		            Float value = new Float(values[offset+(i*localNextSampleOffset)+j]);
		            setValueAt(firstSample + i, j, value, false);
		        }
		    }
		    
	        otDataStore.setDoNotifyChangeListeners(true);
		}
		
		notifyOTValuesChange();
	}

	protected void notifyOTValuesChange()
	{
		otDataStore.notifyOTChange("values", OTChangeEvent.OP_CHANGE, null);		
	}
	
	protected void notifyOTValuesRemove()
	{
		otDataStore.notifyOTChange("values", OTChangeEvent.OP_REMOVE, null);		
	}

	/**
	 * @see org.concord.framework.data.stream.WritableDataStore#removeSampleAt(int)
	 */
	public void removeSampleAt(int numSample) 
	{
		OTResourceList values = otDataStore.getValues();
		int numChannels = getTotalNumChannels();
		
		int index = numSample * numChannels;

        otDataStore.setDoNotifyChangeListeners(false);

        for(int i=0; i<numChannels; i++) {
		    values.remove(index);
		}

        otDataStore.setDoNotifyChangeListeners(true);
        
        notifyOTValuesRemove();        
	}
	
	/**
	 * @see org.concord.framework.data.stream.WritableDataStore#insertSampleAt(int)
	 */
	public void insertSampleAt(int numSample)
	{
		OTResourceList values = otDataStore.getValues();
		int numChannels = getTotalNumChannels();
		
		int index = numSample * numChannels;

        otDataStore.setDoNotifyChangeListeners(false);

        for(int i=0; i<numChannels; i++) {
		    values.add(index, null);
		}

        otDataStore.setDoNotifyChangeListeners(true);
        
        notifyOTValuesChange();
	}	
	
	/* (non-Javadoc)
	 * @see org.concord.framework.data.stream.WritableDataStore#setDataChannelDescription(int, org.concord.framework.data.stream.DataChannelDescription)
	 */
	public void setDataChannelDescription(int channelIndex,
			DataChannelDescription desc) 
	{
		// FIXME this is not supported yet
		//throw new UnsupportedOperationException("org.concord.framework.data.stream.WritableDataStore.setDataChannelDescription not supported yet");
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
			if (dataStreamDesc == null) return null;
			
			//Special case: using dt as the channel -1
			if (numChannel == -1){
				return dataStreamDesc.getDtChannelDescription();
			}
			
			return dataStreamDesc.getChannelDescription(numChannel);
	    }
	    
	    // If we are using dt as a channel, then ot channel description at 0 is
	    // is the DT channel description.  And the ot channel description at 1 is
	    // the first real channel description.
	    // The dt channel is requested by passing a numChannel of -1
	    if(isUseDtAsChannel()){
	    	numChannel++;
	    }

	    OTObjectList channelDescriptions = otDataStore.getChannelDescriptions();
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
	
	protected OTDataChannelDescription createOTDataChannelDescription(DataChannelDescription dCDesc) 
		throws Exception
	{
		OTObjectService objService = otDataStore.getOTObjectService();

		OTDataChannelDescription otDCDesc = 
			(OTDataChannelDescription) objService.createObject(OTDataChannelDescription.class);

		otDCDesc.setName(dCDesc.getName());
		if(dCDesc.getUnit() != null){
			otDCDesc.setUnit(dCDesc.getUnit().getDimension());
		}

		float absMax = dCDesc.getAbsoluteMax();
		if(!Float.isNaN(absMax)){
			otDCDesc.setAbsoluteMax(absMax);
		}
		float absMin = dCDesc.getAbsoluteMin();
		if(!Float.isNaN(absMin)){
			otDCDesc.setAbsoluteMin(absMin);
		}
		float recMax = dCDesc.getRecommendMax();
		if(!Float.isNaN(recMax)){
			otDCDesc.setRecommendMax(recMax);
		}
		float recMin = dCDesc.getRecommendMin();
		if(!Float.isNaN(recMin)){
			otDCDesc.setRecommendMin(recMin);					
		}

		if(dCDesc.isUsePrecision()){
			otDCDesc.setPrecision(dCDesc.getPrecision());
		}

		return otDCDesc;
	}
	
	protected void updateDataDescription(DataStreamDescription desc) 
	{
		super.updateDataDescription(desc);
		
		// Save all the dataChannelDescriptions
		if(desc == null){
			return;
		}
		
		try {
			otDataStore.getChannelDescriptions().removeAll();
						
			if(isUseDtAsChannel()){
				// if we are using the dt as a channel then the first element in the channelDescriptions list
				// is the channel description of the dt
				DataChannelDescription dCDesc = desc.getDtChannelDescription();
				OTDataChannelDescription otDCDesc = createOTDataChannelDescription(dCDesc);
				otDataStore.getChannelDescriptions().add(otDCDesc);				
			}
			
			for(int i=0; i<desc.getChannelsPerSample(); i++){
				DataChannelDescription dCDesc = desc.getChannelDescription(i);
				OTDataChannelDescription otDCDesc = createOTDataChannelDescription(dCDesc);
				otDataStore.getChannelDescriptions().add(otDCDesc);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/* (non-Javadoc)
     * @see org.concord.framework.data.stream.WritableArrayDataStore#setDt(float)
     */
    public void setDt(float dt)
    {
        otDataStore.setDt(dt);
    }
	
    /**
     * This returns the dt of the datastore.  If there is no 
     * dt it returns Float.NaN
     */
    public float getDt()
    {
        return otDataStore.getDt();
    }
    
    /**
     * @return Returns the useDtAsChannel.
     */
    public boolean isUseDtAsChannel()
    {
        return !Float.isNaN(otDataStore.getDt());
    }
    
    /**
     * @param useDtAsChannel The useDtAsChannel to set.
     */
    public void setUseDtAsChannel(boolean useDtAsChannel)
    {
        if(!useDtAsChannel) {
            setDt(Float.NaN);
        } else {
            if(Float.isNaN(getDt())) {
                System.err.println("Warning: trying to use dt as a channel without a valid value");
            }
        }
    }
    

    /* (non-Javadoc)
     * @see org.concord.framework.data.stream.WritableArrayDataStore#setValues(int, float[], int, int, int)
     */
    public void setValues(int numChannels, float[] values, int offset,
            int numSamples, int nextSampleOffset)
    {
        otDataStore.setDoNotifyChangeListeners(false);

        for(int i=0; i<numSamples*nextSampleOffset; 
            i+=nextSampleOffset) {
            for(int j=0;j<numChannels;j++) {
                Float fValue = new Float(values[offset+i+j]);
                setValueAt(i/nextSampleOffset, j, fValue, false);
            }
        }

        otDataStore.setDoNotifyChangeListeners(true);
        
        notifyOTValuesChange();
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
	    Vector listenersClone = (Vector) dataStoreListeners.clone();
	    
	    for(int i=0; i<listenersClone.size(); i++){
	    	WeakReference listenerRef = (WeakReference) listenersClone.get(i);
	    	if(listenerRef.get() == l){
	    		dataStoreListeners.remove(listenerRef);
	    	}
	    }
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
		DataStoreEvent evt = new DataStoreEvent(this, DataStoreEvent.DATA_REMOVED);
		DataStoreListener l;
		
		// Clone our listeners so they can remove them selves from the list 
		// without the vector up.
		Vector listenersClone = (Vector) dataStoreListeners.clone();
		
		for (int i=0; i<listenersClone.size(); i++){
		    Reference ref = (Reference)listenersClone.elementAt(i);
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
