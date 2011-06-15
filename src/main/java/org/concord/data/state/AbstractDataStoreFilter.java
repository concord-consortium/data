/*
 * Last modification information:
 * $Revision: 1.4 $
 * $Date: 2007-06-29 22:03:38 $
 * $Author: imoncada $
 *
 * Licence Information
 * Copyright 2007 The Concord Consortium 
*/
package org.concord.data.state;

import java.util.Vector;

import org.concord.data.state.filter.DataRange;
import org.concord.framework.data.stream.DataStore;
import org.concord.framework.data.stream.DataStoreEvent;
import org.concord.framework.data.stream.DataStoreEventNotifier;
import org.concord.framework.data.stream.DataStoreListener;


/**
 * AbstractDataFilter
 *
 * Date created: Mar 18, 2007
 *
 * @author Ingrid Moncada<p>
 *
 */
public abstract class AbstractDataStoreFilter
	implements DataStoreFilter, DataStoreListener, DataStoreFilterPropertyListener
{
	protected DataStore inputDataStore;
	protected DataStoreFilterDescription filterDescription;
		
	protected Vector<DataRange> ranges;
	
	protected DataStore outputDataStore;

	/**
	 * Does the calculations to filter the data
	 */
	protected abstract void calculateResults()
		throws IllegalArgumentException;

	/**
	 * 
	 *
	 */
	public AbstractDataStoreFilter()
	{
		ranges = new Vector<DataRange>();
	}
	
	/**
	 * @see org.concord.data.state.DataStoreFilter#geInputDataStore()
	 */
	public DataStore getInputDataStore()
	{
		return inputDataStore;
	}

	/**
	 * @see org.concord.data.state.DataStoreFilter#getFilterDescription()
	 */
	public DataStoreFilterDescription getFilterDescription()
	{
		return filterDescription;
	}

	/**
	 * @see org.concord.data.state.DataStoreFilter#setFilterDescription(org.concord.data.state.DataStoreFilterDescription)
	 */
	public void setFilterDescription(DataStoreFilterDescription desc)
		throws IllegalArgumentException
	{
		if (this.filterDescription != null){
			this.filterDescription.removePropertyListener(this);
		}
		this.filterDescription = desc;
		if (this.filterDescription != null){
			this.filterDescription.addPropertyListener(this);
		}
		
		if (inputDataStore != null){
			calculateResults();
		}
	}

	/**
	 * @see org.concord.data.state.DataStoreFilter#setInputDataStore(org.concord.framework.data.stream.DataStore)
	 */
	public void setInputDataStore(DataStore ds)
	{
		this.inputDataStore = ds;
		
		if (filterDescription != null){
			calculateResults();
		}
		inputDataStore.addDataStoreListener(this);
	}

	/**
	 * @see org.concord.data.state.DataStoreFilter#getResultDataStore()
	 */
	public DataStore getResultDataStore()
	{
		return outputDataStore;
	}

	/**
	 * @see org.concord.framework.data.stream.DataStoreListener#dataAdded(org.concord.framework.data.stream.DataStoreEvent)
	 */
	public void dataAdded(DataStoreEvent evt)
	{
		calculateResults();
		if (outputDataStore instanceof DataStoreEventNotifier){
			((DataStoreEventNotifier)outputDataStore).notifyDataStoreListeners(evt);
		}
	}

	/**
	 * @see org.concord.framework.data.stream.DataStoreListener#dataChanged(org.concord.framework.data.stream.DataStoreEvent)
	 */
	public void dataChanged(DataStoreEvent evt)
	{
		calculateResults();
		if (outputDataStore instanceof DataStoreEventNotifier){
			((DataStoreEventNotifier)outputDataStore).notifyDataStoreListeners(evt);
		}
	}

	/**
	 * @see org.concord.framework.data.stream.DataStoreListener#dataChannelDescChanged(org.concord.framework.data.stream.DataStoreEvent)
	 */
	public void dataChannelDescChanged(DataStoreEvent evt)
	{
		if (outputDataStore instanceof DataStoreEventNotifier){
			((DataStoreEventNotifier)outputDataStore).notifyDataStoreListeners(evt);
		}
	}

	/**
	 * @see org.concord.framework.data.stream.DataStoreListener#dataRemoved(org.concord.framework.data.stream.DataStoreEvent)
	 */
	public void dataRemoved(DataStoreEvent evt)
	{
		calculateResults();
		if (outputDataStore instanceof DataStoreEventNotifier){
			((DataStoreEventNotifier)outputDataStore).notifyDataStoreListeners(evt);
		}
	}

	/**
	 * 
	 */
	protected void notifyDataChange()
	{
		DataStoreEvent evt = new DataStoreEvent(null, DataStoreEvent.DATA_CHANGED);
		DataStore resultsDS = getResultDataStore();
		if (resultsDS instanceof DataStoreEventNotifier){
			((DataStoreEventNotifier)resultsDS).notifyDataStoreListeners(evt);
		}
	}
	
	/**
	 * @see org.concord.data.state.DataStoreFilterPropertyListener
	 */
	public void propertyChange(DataStoreFilterPropertyEvent evt)
	{
		System.out.println("property changed: "+evt + "\n" + this);
		calculateResults();
		
		//We don't care what kind of change it was, we notify it as data changed
		notifyDataChange();
	}
}
