/*
 * Created on Jan 26, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.concord.data.ui;

import javax.swing.JLabel;
import javax.swing.JTextField;

import org.concord.framework.data.stream.DataChannelDescription;
import org.concord.framework.data.stream.DataStore;
import org.concord.framework.data.stream.DataStoreEvent;
import org.concord.framework.data.stream.DataStoreListener;

/**
 * @author scott
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class DataStoreLabel extends JTextField
	implements DataStoreListener
{
	DataStore dataStore = null;
	int channel = 0;
	
	public DataStoreLabel(DataStore dataStore, int channel)
	{
		this.dataStore = dataStore;
		this.channel = channel;
		dataStore.addDataStoreListener(this);
		setEditable(false);
		updateValue();
	}
	
	/* (non-Javadoc)
	 * @see org.concord.framework.data.stream.DataStoreListener#dataAdded(org.concord.framework.data.stream.DataStoreEvent)
	 */
	public void dataAdded(DataStoreEvent evt) 
	{
		updateValue();
	}
	
	/* (non-Javadoc)
	 * @see org.concord.framework.data.stream.DataStoreListener#dataChanged(org.concord.framework.data.stream.DataStoreEvent)
	 */
	public void dataChanged(DataStoreEvent evt) 
	{
		updateValue();
	}
	
	/* (non-Javadoc)
	 * @see org.concord.framework.data.stream.DataStoreListener#dataChannelDescChanged(org.concord.framework.data.stream.DataStoreEvent)
	 */
	public void dataChannelDescChanged(DataStoreEvent evt) 
	{
	}
	
	/* (non-Javadoc)
	 * @see org.concord.framework.data.stream.DataStoreListener#dataRemoved(org.concord.framework.data.stream.DataStoreEvent)
	 */
	public void dataRemoved(DataStoreEvent evt) 
	{
	}
	
	private void updateValue()
	{
		int numSamples = dataStore.getTotalNumSamples();
		if(numSamples == 0) return;
		
		Float lastValue = 
			(Float)dataStore.getValueAt(numSamples-1, channel);

		setValue(lastValue.floatValue());
	}
	
	/**
	 * Sets the value of the label. 
	 * Pays attention to the properties of the channel that is being displayed
	 * (channel description), specially the precision
	 * @param val	float value to display
	 */
	private void setValue(float val)
	{
		DataChannelDescription channelDesc = 
			dataStore.getDataChannelDescription(channel);
		
		//If the channel has a description, display the value with the correct precision
		if (channelDesc != null){
			if (channelDesc.isUsePrecision()){
				double precision = Math.pow(10, channelDesc.getPrecision());
				val = (float)(Math.floor(((precision) * val) + 0.5) / precision);
			}
		}
		setText(Float.toString(val));
	}
}
