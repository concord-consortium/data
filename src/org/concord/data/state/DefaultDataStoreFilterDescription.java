/*
 * Last modification information:
 * $Revision: 1.3 $
 * $Date: 2007-07-22 03:56:23 $
 * $Author: imoncada $
 *
 * Licence Information
 * Copyright 2007 The Concord Consortium 
*/
package org.concord.data.state;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

/**
 * DefaultDataDescription
 * Class name and description
 *
 * Date created: May 20, 2007
 *
 * @author Ingrid Moncada<p>
 *
 */
public class DefaultDataStoreFilterDescription
	implements DataStoreFilterDescription
{
	protected int channelX;
	protected int channelY;
	protected String operation;
	protected String label;
	
	protected Hashtable properties;
	
	protected Vector listeners;
	
	public DefaultDataStoreFilterDescription(String operation)
	{
		this(operation, operation);
	}
	
	public DefaultDataStoreFilterDescription(String operation, String label)
	{
		this.label = label;
		this.operation = operation;
		properties = new Hashtable();
		listeners = new Vector();
		channelX = 0;
		channelY = 1;
	}
	
	/**
	 * @see org.concord.data.state.DataStoreFilterDescription#getChannelX()
	 */
	public int getChannelX()
	{
		return channelX;
	}

	/**
	 * @see org.concord.data.state.DataStoreFilterDescription#getChannelY()
	 */
	public int getChannelY()
	{
		return channelY;
	}

	/**
	 * @see org.concord.data.state.DataStoreFilterDescription#getOperation()
	 */
	public String getOperation()
	{
		return operation;
	}

	/**
	 * @see org.concord.data.state.DataStoreFilterDescription#getProperty(java.lang.String)
	 */
	public String getProperty(String name)
	{
		if (name.equals("channelX")) return Integer.toString(getChannelX());
		if (name.equals("channelY")) return Integer.toString(getChannelY());
		return (String)properties.get(name);
	}

	/**
	 * @see org.concord.data.state.DataStoreFilterDescription#setProperty(java.lang.String)
	 */
	public void setProperty(String name, String value)
	{
		if (name == null || value == null){
			throw new IllegalArgumentException("Property name and value cannot be null.");
		}
		
		String prevStr = null;
		if (name.equals("channelX")) {
			setChannelX(Integer.valueOf(value).intValue());
		}
		else if (name.equals("channelY")) {
			setChannelY(Integer.valueOf(value).intValue());
		}
		else {
			prevStr = (String)properties.put(name, value);
		}
		notifyPropertyChange(name, value, prevStr);
	}

	public Enumeration getPropertyNames()
	{
		return properties.keys();
	}
	
	/**
	 * @param name
	 * @param value
	 * @param prevStr
	 */
	protected void notifyPropertyChange(String name, String value, String prevStr)
	{
		DataStoreFilterPropertyEvent event;
		
		if (prevStr == null){
			event = new DataStoreFilterPropertyEvent(this, name, DataStoreFilterPropertyEvent.PROPERTY_ADDED);
		}
		else{
			event = new DataStoreFilterPropertyEvent(this, name, DataStoreFilterPropertyEvent.PROPERTY_CHANGED);			
		}
		
		int n = listeners.size();
		for (int i = 0; i < n; i++){
			DataStoreFilterPropertyListener listener = (DataStoreFilterPropertyListener)listeners.elementAt(i);
			listener.propertyChange(event);
		}
	}

	public void setChannelX(int channelX)
	{
		this.channelX = channelX;
	}

	
	public void setChannelY(int channelY)
	{
		this.channelY = channelY;
	}

	public String toString()
	{
		return label;
	}

	/**
	 * @see org.concord.data.state.DataStoreFilterDescription#addPropertyListener(org.concord.data.state.DataStoreFilterPropertyListener)
	 */
	public void addPropertyListener(DataStoreFilterPropertyListener listener)
	{
		listeners.add(listener);
	}

	/**
	 * @see org.concord.data.state.DataStoreFilterDescription#removePropertyListener(org.concord.data.state.DataStoreFilterPropertyListener)
	 */
	public void removePropertyListener(DataStoreFilterPropertyListener listener)
	{
		listeners.remove(listener);
	}
}
