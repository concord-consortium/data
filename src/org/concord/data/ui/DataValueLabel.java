/*
 * Last modification information:
 * $Revision: 1.1 $
 * $Date: 2004-09-22 15:52:09 $
 * $Author: imoncada $
 *
 * Licence Information
 * Copyright 2004 The Concord Consortium 
*/
package org.concord.data.ui;

import javax.swing.JLabel;

import org.concord.framework.data.stream.DataChannelDescription;
import org.concord.framework.data.stream.DataListener;
import org.concord.framework.data.stream.DataProducer;
import org.concord.framework.data.stream.DataStreamEvent;


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
public class DataValueLabel extends JLabel
	implements DataListener
{
	protected DataProducer dataProducer;
	protected int channel = 0;				//Default channel is 0
	
	/**
	 * Default constructor. Creates an empty label that doesn't listen to
	 * any data producer yet.
	 */
	public DataValueLabel()
	{
		super();
	}
	
	/**
	 * Creates an empty label that will receive data from 
	 * the specified data producer. By default, 
	 * it will display the value of the first channel
	 */
	public DataValueLabel(DataProducer dataProducer)
	{
		this();
		setDataProducer(dataProducer);
	}
	
	/**
	 * Creates an empty label that will receive data from 
	 * the specified data producer and will display the value
	 * of the specified channel
	 */
	public DataValueLabel(DataProducer dataProducer, int channel)
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
		}
		
		this.dataProducer = dataProducer;
		
		if (this.dataProducer != null){
			dataProducer.addDataListener(this);
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
	 * Sets the value of the label. 
	 * Pays attention to the properties of the channel that is being displayed
	 * (channel description), specially the precision
	 * @param val	float value to display
	 */
	private void setValue(float val)
	{
		DataChannelDescription channelDesc = dataProducer.getDataDescription().getChannelDescription(channel);
		
		//If the channel has a description, display the value with the correct precision
		if (channelDesc != null){
			double precision = Math.pow(10, channelDesc.getPrecision());
			val = (float)(Math.floor(((precision) * val) + 0.5) / precision);
		}
		setText(Float.toString(val));
	}

	/**
	 * @see org.concord.framework.data.stream.DataListener#dataStreamEvent(org.concord.framework.data.stream.DataStreamEvent)
	 */
	public void dataStreamEvent(DataStreamEvent dataEvent)
	{
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
}