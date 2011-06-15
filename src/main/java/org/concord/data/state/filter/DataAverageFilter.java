/*
 * Last modification information:
 * $Revision: 1.2 $
 * $Date: 2007-06-21 20:58:13 $
 * $Author: imoncada $
 *
 * Licence Information
 * Copyright 2007 The Concord Consortium 
*/
package org.concord.data.state.filter;

import org.concord.framework.data.stream.DataStoreEvent;


/**
 * DataAverageFilter
 * Class name and description
 *
 * Date created: May 13, 2007
 *
 * @author Ingrid Moncada<p>
 *
 */
public class DataAverageFilter extends InPlaceDataStoreFilter
{
	public static final String PROPERTY_TYPE = "type";
	public static final String PROPERTY_LOWER_RANGE = "upper";
	public static final String PROPERTY_UPPER_RANGE = "lower";
	
	public static final String TYPE_CUMULATIVE = "cumulative";
	public static final String TYPE_LOCAL = "local";
	public static final String TYPE_ABSOLUTE = "absolute";
	public static final String DEFAULT_TYPE = TYPE_CUMULATIVE;
	
	private float[] totals;		// store of totals calculation
	
	public DataAverageFilter()
	{
		super();
	}
	
	/**
	 * @see org.concord.data.state.filter.InPlaceDataStoreFilter#getYValue(int, float, float)
	 */
	public float getYValue(int numSample, float xValue, float inputYValue)
	{
		if (totals == null){
			totals = new float[inputDataStore.getTotalNumSamples()];
		}
		//Average
		int minSample = 0;
		int maxSample = 0;
		String type = getType();
		if (type.equals(TYPE_CUMULATIVE)){
			minSample = 0;
			maxSample = numSample;
		}
		else if (type.equals(TYPE_LOCAL)){
			minSample = numSample - Math.round(getLowerRange());
			maxSample = numSample + Math.round(getUpperRange());
		}
		else if (type.equals(TYPE_ABSOLUTE)){
			minSample = 0;
			maxSample = inputDataStore.getTotalNumSamples();
		}
		return getYAverage(minSample, maxSample, numSample);
		//
		
	}

	/**
	 * Returns the average of all the y values between the given 
	 * start and end sample 
	 * @param startSample
	 * @param endSample
	 * @return
	 */
	protected float getYAverage(int startSample, int endSample, int numSample)
	{
		float total = 0;
		
		if (startSample < 0) startSample = 0;
		
		int limitSamples = inputDataStore.getTotalNumSamples();
		if (endSample >= limitSamples) endSample = limitSamples - 1;
		
		int numSamples = endSample - startSample + 1;
		if (numSamples <= 0) return 0;
		
		// if we use previously calculated values, we can
		// greatly speed up this calculation (we only ever need 1-2 lookups,
		// instead of looking up each value in the range)
		if (numSample > 0){
			String type = getType();
			if (type.equals(TYPE_CUMULATIVE)){
				total = totals[numSample -1] + getInputYValue(endSample);
			} else if (type.equals(TYPE_LOCAL)){
				if (numSample - getLowerRange() <= 0){
					total = totals[numSample - 1] + getInputYValue(endSample);
				} else if (numSample + getUpperRange() >= limitSamples) {
					total = totals[numSample - 1] - getInputYValue(startSample -1);
				} else {
					total = totals[numSample - 1] + getInputYValue(endSample) - getInputYValue(startSample -1);
				}
			} else if (type.equals(TYPE_ABSOLUTE)){
				total = totals[numSample -1];
			}
		} else {
    		for (int i = startSample; i <= endSample; i++){
    			float value = getInputYValue(i);
    			total = total + value;
    		}
		}
		totals[numSample] = total;
		
		float average = total / numSamples;
		return average;
	}

	public String getType()
	{
		String type = filterDescription.getProperty(PROPERTY_TYPE);
		if (type == null) return DEFAULT_TYPE;
		return type;
	}
	
	public float getLowerRange()
	{
		if (!getType().equals(TYPE_LOCAL)) return Float.NaN;
		String range = filterDescription.getProperty(PROPERTY_LOWER_RANGE);
		if (range == null) return 0;
		try{
			return Float.parseFloat(range);
		}
		catch(NumberFormatException ex){
			return 0;
		}
	}
	
	public float getUpperRange()
	{
		if (!getType().equals(TYPE_LOCAL)) return Float.NaN;
		String range = filterDescription.getProperty(PROPERTY_UPPER_RANGE);
		if (range == null) return 0;
		try{
			return Float.parseFloat(range);
		}
		catch(NumberFormatException ex){
			return 0;
		}
	}
	
	//If data changes, we need to re-do all calculations, so totals
	//array must be wiped
	private void clearTotals(){
		totals = null;
	}
	
	/**
	 * @see org.concord.framework.data.stream.DataStoreListener#dataAdded(org.concord.framework.data.stream.DataStoreEvent)
	 */
	public void dataAdded(DataStoreEvent evt)
	{
		super.dataAdded(evt);
		clearTotals();
	}

	/**
	 * @see org.concord.framework.data.stream.DataStoreListener#dataChanged(org.concord.framework.data.stream.DataStoreEvent)
	 */
	public void dataChanged(DataStoreEvent evt)
	{
		super.dataChanged(evt);
		clearTotals();
	}

	/**
	 * @see org.concord.framework.data.stream.DataStoreListener#dataRemoved(org.concord.framework.data.stream.DataStoreEvent)
	 */
	public void dataRemoved(DataStoreEvent evt)
	{
		super.dataRemoved(evt);
		clearTotals();
	}
}
