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

import java.awt.Point;
import java.util.Vector;

import org.concord.data.MathUtil;
import org.concord.data.state.AbstractDataStoreFilter;
import org.concord.data.state.DataStoreFilter;
import org.concord.data.state.DefaultDataStoreFilterDescription;
import org.concord.data.stream.PointsDataStore;
import org.concord.framework.data.stream.DataStore;

/**
 * A start to a filter that could be used to parse a dataset
 * and extract "interesting" points, such as points of inflection,
 * maxima, minima and sharp changes in slope.
 * 
 * @author sfentress
 *
 */
public class DataInterestingFilter extends AbstractDataStoreFilter
{
	
	/**
	 * 
	 */
	public DataInterestingFilter()
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
		
		outputDataStore.clearValues();
		DefaultDataStoreFilterDescription slopeFilterDesc = new DefaultDataStoreFilterDescription("slope");
		try {
	        DataStoreFilter slopeFilter = DataStoreFilterFactory.getNewFilter(slopeFilterDesc);
	        slopeFilter.setInputDataStore(inputDataStore);
	        DataStore slopeDataStore = slopeFilter.getResultDataStore();
	        float lastSlope = Float.MIN_VALUE;
	        for (int i = 0; i < slopeDataStore.getTotalNumSamples(); i++) {
	            boolean interesting = false;
	        	if (i == 0){
	        		interesting = true;
	        	} else if (i == slopeDataStore.getTotalNumSamples()-1){
	        		interesting = true;
	        	} else {
	        		float slope = ((Float)slopeDataStore.getValueAt(i, 2)).floatValue();
	        		if (lastSlope != Float.MIN_VALUE && slope * lastSlope <= 0){
	        			interesting = true;
	        		}
	        		lastSlope = slope;
	        	}
	        	if (interesting){
	        		float x = ((Float)slopeDataStore.getValueAt(i, 0)).floatValue();
	        		float y = ((Float)slopeDataStore.getValueAt(i, 1)).floatValue();
	        		((PointsDataStore)outputDataStore).addPoint(x, y);
	        	}
	            
            }
        } catch (InstantiationException e) {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
        } catch (IllegalAccessException e) {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
        }
	}
	
}
