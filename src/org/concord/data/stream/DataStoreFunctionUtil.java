
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
 * $Revision: 1.3 $
 * $Date: 2005-03-15 03:17:58 $
 * $Author: imoncada $
 *
 * Licence Information
 * Copyright 2004 The Concord Consortium 
*/
package org.concord.data.stream;

import org.concord.framework.data.stream.WritableDataStore;


/**
 * DataStoreFunctionUtil
 * Class name and description
 *
 * Date created: Mar 9, 2005
 *
 * @author imoncada<p>
 *
 */
public class DataStoreFunctionUtil
{
	protected WritableDataStore dataStore = null;
	protected int xChannel = 0;			//channel indicating the "x" in a function
	
	/**
	 * 
	 */
	public DataStoreFunctionUtil()
	{
	}
	
	/**
	 * Not used yet!!
	 * @param x
	 * @return
	 */
	public int insertSampleInOrder(float x)
	{
		// Not used yet!!
		 
		int index  = findSamplePositionValue(x);
		float value = getXValueAt(index);
		//If the value is not in the data store, then insert an empty sample
		if (value != x){
			dataStore.insertSampleAt(index);
		}
		//Return the sample index 
		return index;
	}
	
	/**
	 * Returns the correct index where the x value should be s
	 * @param v
	 * @return
	 */
	public int findSamplePositionValue(float x)
	{
		if (dataStore == null) return -1;
		
		//Assumes the data is ordered by the specified channel
		float value;
		Object obj;
		int i;
		for (i = 0; i < dataStore.getTotalNumSamples(); i++){
			obj = dataStore.getValueAt(i, xChannel);
			if (obj instanceof Float){
				value = ((Float)obj).floatValue();
				if (value >= x){
					break;
				}
			}
		}
		
		//System.out.println("get index "+i+" of value "+v+" in "+getFloatVectorStr((Vector)channelsValues.elementAt(0)));
		
		return i;
	}

	/**
	 * @param i
	 * @return
	 */
	public float getXValueAt(int sample)
	{
		if (dataStore == null) return Float.NaN;
		
		Object obj = dataStore.getValueAt(sample, xChannel);
		if (obj instanceof Float){
			return ((Float)obj).floatValue();
		}
		return Float.NaN;
	}
	/**
	 * @return Returns the xChannel.
	 */
	public int getXChannel()
	{
		return xChannel;
	}
	/**
	 * @param channel The xChannel to set.
	 */
	public void setXChannel(int channel)
	{
		xChannel = channel;
	}
	/**
	 * @return Returns the dataStore.
	 */
	public WritableDataStore getDataStore()
	{
		return dataStore;
	}
	/**
	 * @param dataStore The dataStore to set.
	 */
	public void setDataStore(WritableDataStore dataStore)
	{
		this.dataStore = dataStore;
	}
}
