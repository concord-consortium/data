/*
 * Last modification information:
 * $Revision: 1.5 $
 * $Date: 2007-06-29 22:03:38 $
 * $Author: imoncada $
 *
 * Licence Information
 * Copyright 2007 The Concord Consortium 
*/
package org.concord.data.state.filter;

import org.concord.data.state.AbstractDataStoreFilter;
import org.concord.data.state.DataStoreFilter;
import org.concord.framework.data.stream.DataChannelDescription;


/**
 * DataSlopeFilter
 * 
 * This filters takes the data from 2 channels of a data store
 * (interpreted as x and y) and calculates the slope in each point, 
 * based on the points before and after. 
 * The result data store has 3 channels: the first two are identical 
 * as the input data store (x, y), and the third one corresponds to 
 * the slope at that point. (x, y, dy/dx). 
 * By default it assumes that, in the input data store, the x value 
 * is on channel 0 and the y value is on channel 1, but that can be 
 * adjusted on the data filter description.
 *
 * Date created: Mar 22, 2007
 *
 * @author Ingrid Moncada<p>
 *
 */
public class DataSlopeFilter extends AbstractDataStoreFilter
{

	/**
	 * 
	 */
	public DataSlopeFilter()
	{
		super();
		outputDataStore = new MyFilteredDataStore(this);
	}

	/**
	 * @see org.concord.data.state.AbstractDataStoreFilter#calculateResults()
	 */
	protected void calculateResults()
		throws IllegalArgumentException
	{
		//There's nothing to pre-calculate, since the output is calculate on the fly
	}
	
	class MyFilteredDataStore extends DefaultFilteredDataStore
	{
		protected DataChannelDescription slopeDataChannelDescription;
		protected int slopeChannel = 2;
		
		public MyFilteredDataStore(DataStoreFilter df)
		{
			super(df);
			slopeDataChannelDescription = new DataChannelDescription("dy/dx");
		}

		/**
		 * @see org.concord.framework.data.stream.DataStore#getTotalNumChannels()
		 */
		public int getTotalNumChannels()
		{
			//This data store has 3 channels: x, y and dy/dx
			return 3;
		}
		
		/**
		 * @see org.concord.framework.data.stream.DataStore#getTotalNumSamples()
		 */
		public int getTotalNumSamples()
		{
			/* 
			 * Because this filter uses the sample before and the sample after the current sample to calculate
			 * the slope, until we get 3 samples, we can't provide any useful data. Even then, we can only
			 * provide useful data for one sample less than the actual number of raw samples.
			 * 
			 * It is required to do it this way if you want to display a filtered dataStore while it is still being
			 * written into.
			 * 
			 * Drawbacks:
			 *  - If you have less than 3 samples, you won't get any samples.
			 *  - You won't get the original data for the last sample of the source dataStore.
			 */
			int samples = dataFilter.getInputDataStore().getTotalNumSamples();
			if (samples < 3) {
				return 0;
			}
			return samples - 1;
		}

		/**
		 * @see org.concord.framework.data.stream.DataStore#getValueAt(int, int)
		 */
		public Object getValueAt(int numSample, int numChannel)
		{
			Object obj = null;
			if (numChannel == slopeChannel){
			//Slope channel
				Float pointBefore = null;
				Float pointAfter = null;
				if (numSample > 0 && numSample < getTotalNumSamples()){
					pointBefore = (Float)inputDataStore.getValueAt(numSample-1, filterDescription.getChannelY());
					pointAfter = (Float)inputDataStore.getValueAt(numSample+1, filterDescription.getChannelY());
					
					if (pointBefore == null || pointAfter == null) return null;
					
					float yValBefore = pointBefore.floatValue();
					float yValAfter = pointAfter.floatValue();

					pointBefore = (Float)inputDataStore.getValueAt(numSample-1, filterDescription.getChannelX());
					pointAfter = (Float)inputDataStore.getValueAt(numSample+1, filterDescription.getChannelX());
					
					if (pointBefore == null || pointAfter == null) return null;
					
					float xValBefore = pointBefore.floatValue();
					float xValAfter = pointAfter.floatValue();
					
					float slope = (yValAfter - yValBefore) / (xValAfter - xValBefore);
					
					obj = new Float(slope);
				}
				return obj;
			}
			else{
				//The parent will return the correct x and y values for channels 0 and 1, null for the rest
				return super.getValueAt(numSample, numChannel);
			}
		}

		public DataChannelDescription getDataChannelDescription(int numChannel)
		{
			if (numChannel <0 || numChannel >= getTotalNumChannels()) return null;
			if (numChannel == slopeChannel){
				return slopeDataChannelDescription;
			}
			return super.getDataChannelDescription(numChannel);
		}
		
		
	}

}
