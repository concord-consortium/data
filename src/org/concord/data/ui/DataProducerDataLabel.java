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
 * $Date: 2007-09-06 16:07:09 $
 * $Author: scytacki $
 *
 * Licence Information
 * Copyright 2004 The Concord Consortium 
*/
package org.concord.data.ui;

import org.concord.framework.data.stream.DataChannelDescription;
import org.concord.framework.data.stream.DataListener;
import org.concord.framework.data.stream.DataProducer;
import org.concord.framework.data.stream.DataStreamEvent;
import org.concord.framework.startable.StartableEvent;
import org.concord.framework.startable.StartableListener;
import org.concord.framework.startable.StartableEvent.StartableEventType;


/**
 * DataValueLabel
 * This is a JLabel can be used to display a single 
 * data value coming from a data producer.
 * It displays the latest value sent by a data 
 * producer on a specific channel. 
 *
 * Date created: Sep 16, 2004
 *
 * @author imoncada<p>
 *
 */
public class DataProducerDataLabel extends DataValueLabel
	implements DataListener, StartableListener
{
	/**
	 * Not intended to be serialized, added to remove compile warning.
	 */
	private static final long serialVersionUID = 1L;

	protected DataProducer dataProducer;
	protected int channel = 0;				//Default channel is 0
	
	/**
	 * Default constructor. Creates an empty label that doesn't listen to
	 * any data producer yet.
	 */
	public DataProducerDataLabel()
	{
		super();
	}
	
	/**
	 * Creates an empty label that will receive data from 
	 * the specified data producer. By default, 
	 * it will display the value of the first channel
	 */
	public DataProducerDataLabel(DataProducer dataProducer)
	{
		this();
		setDataProducer(dataProducer);
	}
	
	/**
	 * Creates an empty label that will receive data from 
	 * the specified data producer and will display the value
	 * of the specified channel
	 */
	public DataProducerDataLabel(DataProducer dataProducer, int channel)
	{
		this();
		setDataProducer(dataProducer, channel);
	}
	
	/**
	 * Sets the data producer of this data value label,
	 * specifying the channel that this label will display
	 * @param dataProducer	Object that will produce the data
	 * @param channel		Channel to display within the data 
	 * 						sent by the data producer
	 */
	public void setDataProducer(DataProducer dataProducer, int channel)
	{
		setDataProducer(dataProducer);
		setChannel(channel);
	}

	/**
	 * Sets the data producer of this data value label,
	 * without specifying the channel that this label will display
	 * (by default it will display the first channel received)
	 * @param dataProducer	Object that will produce the data
	 */
	private void setDataProducer(DataProducer dataProducer)
	{
		if (this.dataProducer != null){
			dataProducer.removeDataListener(this);
			dataProducer.removeStartableListener(this);
		}
		
		this.dataProducer = dataProducer;
		
		if (this.dataProducer != null){
			dataProducer.addDataListener(this);
			dataProducer.addStartableListener(this);
		}
	}

	/**
	 * Sets the channel that this label will display
	 * within the data sent by the data producer 
	 * @param channel		Channel to display within the data 
	 * 						sent by the data producer
	 */
	public void setChannel(int channel)
	{
		this.channel = channel;
	}
	
	/**
	 * Event received from the data producer when data is sent
	 * @see org.concord.framework.data.stream.DataListener#dataReceived(org.concord.framework.data.stream.DataStreamEvent)
	 */
	public void dataReceived(DataStreamEvent dataEvent)
	{
		int numberOfSamples = dataEvent.getNumSamples();
		int sampleOffset = dataProducer.getDataDescription().getDataOffset();
		int nextSampleOffset = dataProducer.getDataDescription().getNextSampleOffset();
		
		int i;
		
		i = sampleOffset + (numberOfSamples - 1)*nextSampleOffset;
		
		float value = dataEvent.data[i + channel];
		
		setValue(value);
	}
	
	/**
	 * @see org.concord.framework.data.stream.DataListener#dataStreamEvent(org.concord.framework.data.stream.DataStreamEvent)
	 */
	public void dataStreamEvent(DataStreamEvent dataEvent)
	{
	}
	
	public void startableEvent(StartableEvent event) {
		if(event.getType() == StartableEventType.RESET){
			clear();
		}
	}

	/**
	 * Returns the channel that this label will display
	 * @return channel to display within the data 
	 * 		sent by the data producer
	 */
	public int getChannel()
	{
		return channel;
	}
		
	/**
	 * Returns the data producer from which this label
	 * is receiving tha data.
	 * @return dataProducer sending the data
	 */
	public DataProducer getDataProducer()
	{
		return dataProducer;
	}

	@Override
	protected DataChannelDescription getChannelDescription() {
		return getDataProducer().getDataDescription().getChannelDescription(getChannel());
	}
}
