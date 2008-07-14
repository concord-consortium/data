/*
 * Last modification information:
 * $Revision: 1.1 $
 * $Date: 2007-06-29 20:26:11 $
 * $Author: imoncada $
 *
 * Licence Information
 * Copyright 2007 The Concord Consortium 
*/
package org.concord.data.state.filter;

import java.util.Vector;

import org.concord.data.state.DataStoreFilter;
import org.concord.framework.data.stream.DataChannelDescription;
import org.concord.framework.data.stream.DataStore;
import org.concord.framework.data.stream.DataStoreEvent;
import org.concord.framework.data.stream.DataStoreEventNotifier;
import org.concord.framework.data.stream.DataStoreListener;

/**
 * DefaultFilteredDataStore
 * 
 * This is a base class that can be used by a new data filter
 * as a default in-place data store to return as result.
 * By default, it just returns the same values as the input 
 * data store in the two x and y channels specified in its
 * filter description.
 * It only returns these values, always on channels 
 * 0 and 1 (it only has two channels in total, no matter how
 * many channels has the input data store). By default it has 
 * the same number of samples as the input data store.
 * In particular, this class is useful because it already
 * returns the x value when asked for channel 0 and it returns
 * the y value when asked for channel 1. It can then be easily
 * extended when something more advanced is needed.
 *
 * Date created: Mar 22, 2007
 *
 * @author Ingrid Moncada<p>
 *
 */

public class DefaultFilteredDataStore 
	implements DataStore, DataStoreEventNotifier
{
	protected DataStoreFilter dataFilter;
	
	protected Vector dataStoreListeners;
	
	public DefaultFilteredDataStore(DataStoreFilter df)
	{
		dataFilter = df;
		dataStoreListeners = new Vector();
	}	

	/**
	 * @see org.concord.framework.data.stream.DataStore#addDataStoreListener(org.concord.framework.data.stream.DataStoreListener)
	 */
	public void addDataStoreListener(DataStoreListener l)
	{
		dataStoreListeners.add(l);
	}

	/**
	 * @see org.concord.framework.data.stream.DataStore#clearValues()
	 */
	public void clearValues()
	{
		throw new UnsupportedOperationException("A filtered data store cannot be cleared.");
	}

	/**
	 * @see org.concord.framework.data.stream.DataStore#getDataChannelDescription(int)
	 */
	public DataChannelDescription getDataChannelDescription(int numChannel)
	{
		return dataFilter.getInputDataStore().getDataChannelDescription(numChannel);
	}

	/**
	 * @see org.concord.framework.data.stream.DataStore#getTotalNumChannels()
	 */
	public int getTotalNumChannels()
	{
		//Always two channels only
		return 2;
	}

	/**
	 * @see org.concord.framework.data.stream.DataStore#getTotalNumSamples()
	 */
	public int getTotalNumSamples()
	{
		return dataFilter.getInputDataStore().getTotalNumSamples();
	}

	/**
	 * @see org.concord.framework.data.stream.DataStore#getValueAt(int, int)
	 */
	public Object getValueAt(int numSample, int numChannel)
	{
		Object obj = null;
		
		if (numChannel == 0){
		//X value
			obj = dataFilter.getInputDataStore().getValueAt(numSample, 
					dataFilter.getFilterDescription().getChannelX());
		}
		else if (numChannel == 1){
		//Y value
			obj = dataFilter.getInputDataStore().getValueAt(numSample, 
					dataFilter.getFilterDescription().getChannelY());
		}
		
		//System.out.println("getvalue at "+numSample+","+numChannel+" "+realNumSample+" "+analysisDescription.getChannelX()+" "+analysisDescription.getChannelY()+" "+obj);
		return obj;
	}

	/**
	 * @see org.concord.framework.data.stream.DataStore#removeDataStoreListener(org.concord.framework.data.stream.DataStoreListener)
	 */
	public void removeDataStoreListener(DataStoreListener l)
	{
		dataStoreListeners.remove(l);
	}
	
	public void notifyDataStoreListeners(DataStoreEvent evt)
	{
		for (int i=0; i<dataStoreListeners.size(); i++){
			DataStoreListener l = (DataStoreListener)dataStoreListeners.elementAt(i);
			//System.out.println("notifying "+l);
			evt.setSource(this);
			if (evt.getType() == DataStoreEvent.DATA_ADDED){
				l.dataAdded(evt);
			}
			else if (evt.getType() == DataStoreEvent.DATA_CHANGED){
				l.dataChanged(evt);
			}
			else if (evt.getType() == DataStoreEvent.DATA_REMOVED){
				l.dataRemoved(evt);
			}
			else if (evt.getType() == DataStoreEvent.DATA_DESC_CHANGED){
				l.dataChannelDescChanged(evt);
			}
		}
	}
	
	public DataStore getInputDataStore()
	{
		return dataFilter.getInputDataStore();
	}
}
