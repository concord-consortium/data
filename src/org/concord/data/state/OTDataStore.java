

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
 * $Revision: 1.1 $
 * $Date: 2005-01-27 16:43:11 $
 * $Author: scytacki $
 *
 * Licence Information
 * Copyright 2004 The Concord Consortium 
*/
package org.concord.data.state;

import java.util.Vector;

import org.concord.framework.data.stream.DataChannelDescription;
import org.concord.framework.data.stream.DataStoreEvent;
import org.concord.framework.data.stream.DataStoreListener;
import org.concord.framework.data.stream.WritableDataStore;
import org.concord.framework.otrunk.DefaultOTObject;
import org.concord.framework.otrunk.OTObject;
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
		public OTResourceList getValues();		
	};
	
	protected ResourceSchema resources;
	protected Vector dataStoreListeners = new Vector();
	DataStoreEvent changeEvent = new DataStoreEvent(this, DataStoreEvent.DATA_CHANGED);
	
	public OTDataStore(ResourceSchema resources)
	{
		super(resources);
		this.resources = resources;
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
		return 1;
	}
	
	/* (non-Javadoc)
	 * @see org.concord.framework.data.stream.DataStore#getTotalNumSamples()
	 */
	public int getTotalNumSamples() 
	{
		return resources.getValues().size();
	}
	
	/* (non-Javadoc)
	 * @see org.concord.framework.data.stream.DataStore#getValueAt(int, int)
	 */
	public Object getValueAt(int numSample, int numChannel) 
	{
		return resources.getValues().get(numSample);
	}
	
	/* (non-Javadoc)
	 * @see org.concord.framework.data.stream.WritableDataStore#setValueAt(int, int, java.lang.Object)
	 */
	public void setValueAt(int numSample, int numChannel, Object value) 
	{
		OTResourceList values = resources.getValues();
		if(values.size() < numSample+1) {
			values.add(numSample, value);			
		} else {
			values.set(numSample, value);
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
		return null;
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
