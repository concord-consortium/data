

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
 * $Revision: 1.2 $
 * $Date: 2005-08-05 16:13:40 $
 * $Author: scytacki $
 *
 * Licence Information
 * Copyright 2004 The Concord Consortium 
*/
package org.concord.data.stream;

import java.util.Vector;

import org.concord.framework.data.stream.AbstractDataStore;
import org.concord.framework.data.stream.DataChannelDescription;
import org.concord.framework.data.stream.DataListener;
import org.concord.framework.data.stream.DataProducer;
import org.concord.framework.data.stream.DataStore;
import org.concord.framework.data.stream.DataStreamDescription;
import org.concord.framework.data.stream.DataStreamEvent;
import org.concord.framework.data.stream.DeltaDataStore;


/**
 * ProducerDataStore
 * Class name and description
 *
 * Date created: Oct 24, 2004
 *
 * @author imoncada<p>
 *
 */
public class ProducerDataStore extends AbstractDataStore 
	implements DeltaDataStore
{
	protected DataProducer dataProducer;
	
	private int numberOfProducerChannels = 0;
	private int nextSampleOffset = 1;
	protected float dt = 1;
	private boolean useDtAsChannel = true;
		
	protected DataStreamDescription dataStreamDesc;
	
	private DataListener dataListener;

    private boolean useVirtualChannels;
	
	public ProducerDataStore()
	{
		super();
		
		/**
		 * This looks ugly, but it has a purpose.  The methods are in 
		 * an inner class so this class does not implement the DataListener
		 * interface itself.  That should make it easier for a user of this
		 * class to just look at the interfaces to see how to use this class.  
		 * 
		 * It also prevents someone from taking an instance of this class and
		 * adding it as a listener to multiple DataProducers.
		 */	
		dataListener = new DataListener() {
		    /* (non-Javadoc)
		     * @see org.concord.framework.data.stream.DataListener#dataReceived(org.concord.framework.data.stream.DataStreamEvent)
		     */
		    public void dataReceived(DataStreamEvent dataEvent)
		    {
		        ProducerDataStore.this.dataReceived(dataEvent);
		        
		    }
		    /* (non-Javadoc)
		     * @see org.concord.framework.data.stream.DataListener#dataStreamEvent(org.concord.framework.data.stream.DataStreamEvent)
		     */
		    public void dataStreamEvent(DataStreamEvent dataEvent)
		    {
		        ProducerDataStore.this.dataStreamEvent(dataEvent);
		    }
		};

	}
	
	/**
	 * 
	 */
	public ProducerDataStore(DataProducer dataProducer)
	{
		this();
		setDataProducer(dataProducer);
	}

    /** 
     * Set this to true if you want the channel numbers presented
     * to the outside to always start at 0.  
     * This is to abstract the case where the dataproducer has a dt.
     * In that case the dt (usually x) channel of the datastore is -1
     * 
     * So if useVirtualChannels is true then channel 0 will really 
     * be -1 of the datastore if it has a dt or 0 if it doesn't.
     * The other channels will be shifted too.
     * 
     * This is useful when a single datagraphable can have different
     * datastores plugged into it.  Some of which have dts and some
     * don't.  
     */ 
	public void setUseVirtualChannels(boolean flag)
	{
	    useVirtualChannels = flag;
	}
	
	public boolean useVirtualChannels()
	{
	    return useVirtualChannels;
	}

	public boolean channelsNeedAdjusting()
	{
	    return useVirtualChannels() && isUseDtAsChannel();
	}
	
	/**
	 * @return Returns the dataProducer.
	 */
	public DataProducer getDataProducer()
	{
		return dataProducer;
	}
	
	/**
	 * Sets the data producer of this data store
	 * @param dataProducer The dataProducer to set.
	 */
	public void setDataProducer(DataProducer dataProducer)
	{
		if (this.dataProducer != null){
			this.dataProducer.removeDataListener(dataListener);
		}
		this.dataProducer = dataProducer;
		if (this.dataProducer != null){
			updateDataDescription(this.dataProducer.getDataDescription());
			this.dataProducer.addDataListener(dataListener);
		}
	}

	
	
	/**
	 * @see org.concord.framework.data.stream.DataStore#getValueAt(int, int)
	 */
	public Object getValueAt(int numSample, int numChannel)
	{
	    if(channelsNeedAdjusting()) {
	        numChannel--;
	    }
	    
		//Special case: when dt is a channel, it's the channel -1
		if (numChannel == -1){
			return new Float(numSample * getDt());
		}
		
		return super.getValueAt(numSample, numChannel);
	}
	
	/**
	 * @see org.concord.framework.data.stream.DataStore#getTotalNumChannels()
	 */
	public int getTotalNumChannels()
	{
		return super.getTotalNumChannels();
		//if (useDtAsChannel){
		//	nChannels++;
		//}
	}
	
	/**
     * @see org.concord.framework.data.stream.DataListener#dataReceived(org.concord.framework.data.stream.DataStreamEvent)
     */
    protected void dataReceived(DataStreamEvent dataEvent)
    {
        float [] data = dataEvent.getData();
        int numberOfSamples = dataEvent.getNumSamples();
        int eventSampleIndex;
        Float value;
        int storeSampleIndex = getTotalNumSamples();		
        eventSampleIndex =  dataEvent.getDataDescription().getDataOffset();
        if(data == null) {
            System.err.println("null data in dataReceived");
            System.err.println("  num samples: " + numberOfSamples);
            return;
        }
        
        addSamples(data, eventSampleIndex, numberOfSamples, nextSampleOffset);		
    }
    
    /**
     * @see org.concord.framework.data.stream.DataListener#dataStreamEvent(org.concord.framework.data.stream.DataStreamEvent)
     */
    protected void dataStreamEvent(DataStreamEvent dataEvent)
    {
        updateDataDescription(dataEvent.getDataDescription());
        notifyChannelDescChanged();
    }
	
	protected synchronized void addSamples(float [] values, int offset, 
	        int numberOfSamples, int localNextSampleOffset)
	{
		int eventSampleIndex = offset;
		int storeSampleIndex = getTotalNumSamples();		

	    for(int i=0; i<numberOfSamples; i++)
		{
		    synchronized (this){
		        for(int j=0; j<numberOfProducerChannels; j++)
		        {
		            Float value = new Float(values[eventSampleIndex + j]);
		            
		            //This is not a WritableDataStore, so this is not valid anymore:
		            //setValueAt(i + iSamples, j, value);
		            addValue(storeSampleIndex, j, value);
		        }
		        eventSampleIndex+= localNextSampleOffset;
		        storeSampleIndex++;
		    }
		}

		notifyDataAdded();	    
	}
	
	/* (non-Javadoc)
     * @see org.concord.framework.data.stream.AbstractDataStore#getTotalNumSamples()
     */
    public synchronized int getTotalNumSamples()
    {
		//Returns the maximum between the number of samples in each channel
		int t = 0;
		for (int i=0; i < channelsValues.size(); i++){
			Vector channel = (Vector)channelsValues.elementAt(i);
			if (channel.size() > t){
				t = channel.size();
			} else if(channel.size() < t) {
			    System.err.println("got inconsistant call getTotalNumSamples");
			}
		}
		return t;
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
		if (numChannel < 0) return;	
		if (numChannel >= channelsValues.size()) return;

		//Locate the channel
		Vector channel = (Vector)channelsValues.elementAt(numChannel);
		
		//Add the value to the channel
		channel.addElement(value);		
	}
	
	protected int getNumberOfProducerChannels()
	{
	    return numberOfProducerChannels;
	}
	
	protected void updateDataDescription(DataStreamDescription desc)
	{
		dataStreamDesc = desc;
		if(desc == null) {
		    System.err.println("null data description");
		    return;
		}
		nextSampleOffset = desc.getNextSampleOffset();
        if(desc.getDataType() == DataStreamDescription.DATA_SEQUENCE) {
            setDt(desc.getDt());
            setUseDtAsChannel(true);
        } else {
            setUseDtAsChannel(false);
        }
		numberOfProducerChannels = desc.getChannelsPerSample();

		//Make sure the values vector has enough channels
		while (numberOfProducerChannels > channelsValues.size()){
			//Add empty vectors until all channels have space
			channelsValues.addElement(new Vector());
		}
	}
	
	
	/**
	 * @see org.concord.framework.data.stream.DataStore#getDataChannelDescription(int)
	 */
	public DataChannelDescription getDataChannelDescription(int numChannel)
	{
		DataChannelDescription channelDesc;
		if (dataStreamDesc == null) return null;
		
		if(channelsNeedAdjusting()) {
		    numChannel--;
		}
		
		//Special case: using dt as the channel -1
		if (numChannel == -1){
			return dataStreamDesc.getDtChannelDescription();
		}
		
		channelDesc = dataStreamDesc.getChannelDescription(numChannel);
		if (channelDesc == null){
			channelDesc = super.getDataChannelDescription(numChannel);
		}
		return channelDesc;
	}
	
	/**
	 * @return Returns the useDtAsChannel.
	 */
	public boolean isUseDtAsChannel()
	{
		return useDtAsChannel;
	}
	
	/**
	 * @param useDtAsChannel The useDtAsChannel to set.
	 */
	protected void setUseDtAsChannel(boolean useDtAsChannel)
	{
		this.useDtAsChannel = useDtAsChannel;
	}
	
	public float getDt()
	{
	    return dt;
	}
    
    public void setDt(float dt)
    {
        this.dt = dt;
    }
}
