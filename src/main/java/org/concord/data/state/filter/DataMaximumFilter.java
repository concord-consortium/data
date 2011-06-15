/*
 * Last modification information:
 * $Revision: 1.5 $
 * $Date: 2007-06-21 20:58:13 $
 * $Author: imoncada $
 *
 * Licence Information
 * Copyright 2007 The Concord Consortium 
*/
package org.concord.data.state.filter;

import org.concord.data.MathUtil;
import org.concord.data.state.AbstractDataStoreFilter;
import org.concord.data.stream.PointsDataStore;

/**
 * DataMaximumFilter
 * Class name and description
 *
 * Date created: Mar 21, 2007
 *
 * @author Ingrid Moncada<p>
 *
 */
public class DataMaximumFilter extends AbstractDataStoreFilter
{
	
	/**
	 * 
	 */
	public DataMaximumFilter()
	{
		super();
		outputDataStore = new PointsDataStore();
	}

	/**
	 * @see org.concord.data.state.AbstractDataStoreFilter#calculateResults()
	 */
	protected void calculateResults()
		throws IllegalArgumentException
	{
		//super.calculateResults();
		
		outputDataStore.clearValues();
		
		float maxValue = Float.NEGATIVE_INFINITY;
		//Analyze the input data store and get the maximum
		int n = inputDataStore.getTotalNumSamples();
		int yChannel = filterDescription.getChannelY();
		for (int i=0; i<n; i++){
			Object yValObj = inputDataStore.getValueAt(i, yChannel);
			if (!(yValObj instanceof Float)) continue;
			float yVal = ((Float)yValObj).floatValue();
			if (yVal > maxValue){
				//We found a higher value -> delete our previous candidates and remember the new one
				outputDataStore.clearValues();
				maxValue = yVal;
			}
			//Careful comparing floats
			if (MathUtil.equals(yVal, maxValue)){
				//The current value equals the maximum: 
				//either it was just found (went into the if above)
				//or it's just another point with the same y value
				//Either way, we need to add values to our result
				Object xValObj = inputDataStore.getValueAt(i, filterDescription.getChannelX());
				float xVal = ((Float)xValObj).floatValue();
				((PointsDataStore)outputDataStore).addPoint(xVal, yVal);				
			}
		}
		//System.out.println("Maximum value was "+maxValue);
	}
	
}
