/*
 * Last modification information:
 * $Revision: 1.2 $
 * $Date: 2007-06-29 20:26:11 $
 * $Author: imoncada $
 *
 * Licence Information
 * Copyright 2007 The Concord Consortium 
*/
package org.concord.data.state.filter;

import org.concord.data.MathUtil;
import org.concord.data.state.AbstractDataStoreFilter;
import org.concord.framework.data.stream.DataStore;
import org.concord.framework.data.stream.DataStoreEvent;
import org.concord.framework.data.stream.DataStoreEventNotifier;

/**
 * InPlaceDataFilter
 *
 * This is a convenience abstract class that can be used to create a simple filter
 * that filters values "in place" (when asked for it).
 * The only method that needs to be implemented is getYValue()
 * To create a new filter, extend this class.
 * The method getYValue() receives by parameter the x and y values before filtering.
 * To implement this method, just return the desired "filtered" y value
 *
 * Date created: May 20, 2007
 *
 * @author Ingrid Moncada<p>
 *
 */
public abstract class InPlaceDataStoreFilter extends AbstractDataStoreFilter
{
	/**
	 * Default constructor
	 */
	public InPlaceDataStoreFilter()
	{
		super();
		outputDataStore = new MyFilteredDataStore(this);
	}
	
	/**
	 * This is the only method that is required to be implemented.
	 * It receives the sample number and the x,y values of the input
	 * data store (before filtering). Returns the filtered y value. 
	 * 
	 * @param numSample
	 * @param xValue
	 * @param inputYValue
	 * @return
	 */
	public abstract float getYValue(int numSample, float xValue, float inputYValue);
	
	/**
	 * If the filter wants to filter the x value as well, it can override this method
	 * By default, it returns the same x value as the input data store.
	 * 
	 * @param numSample
	 * @param inputValue
	 * @return
	 */
	public float getXValue(int numSample, float inputValue)
	{
		return inputValue;
	}
	
	/**
	 * This method can also be overriden in case the filtered values need (or can)  
	 * be calculated at once. It will be called once, every time any value in the input 
	 * data store changes.
	 * By default, it does nothing.
	 * 
	 * @see org.concord.data.state.AbstractDataStoreFilter#calculateResults()
	 */
	protected void calculateResults()
		throws IllegalArgumentException
	{
		//Does nothing by default
		//There's nothing to pre-calculate, 
		//since the output is calculate on the fly
	}
	
	/**
	 * This is a convenience method that can be used to obtain the x value
	 * of the input data store on a specific sample.
	 * Do not override
	 */
	public float getInputXValue(int numSample)
	{
		int xChannel = getFilterDescription().getChannelX();
		return getInputValue(numSample, xChannel);
	}
	
	/**
	 * This is a convenience method that can be used to obtain the y value
	 * of the input data store on a specific sample 
	 * Do not override
	 */
	public float getInputYValue(int numSample)
	{
		int yChannel = getFilterDescription().getChannelY();
		return getInputValue(numSample, yChannel);
	}

	/**
	 * This is a convenience method that can be used to obtain the value
	 * of the input data store on a specific sample and a specific channel
	 * Do not override
	 */
	public float getInputValue(int numSample, int numChannel)
	{
		Object obj = inputDataStore.getValueAt(numSample, numChannel);
		if (obj instanceof Float){
			Float objValue = (Float)obj;
			return objValue.floatValue();
		}
		return Float.NaN;
	}
	
	/**
	 * This is a convenience method that can be used to obtain the y value
	 * of the input data store given an x value.
	 * It looks for all the x values in the input data store looking for the
	 * desired one to return its y value. If it's not found, it returns NaN.
	 * Do not override
	 */
	public float getInputYValue(float xValue)
	{
		int xChannel = getFilterDescription().getChannelX();
		for (int i=0; i<inputDataStore.getTotalNumSamples(); i++){
			float value = getInputValue(i, xChannel);
			if (MathUtil.equals(value, xValue)){
				//Once we find an x that equals the x value passed by parameter, we consider it found
				//we don't care if there are more than one x with the same value, we just take the first
				return getInputYValue(i); 
			}
		}
		return Float.NaN;
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

	class MyFilteredDataStore extends DefaultFilteredDataStore
	{

		public MyFilteredDataStore(InPlaceDataStoreFilter df)
		{
			super(df);
		}
		
		/**
		 * @see org.concord.framework.data.stream.DataStore#getValueAt(int, int)
		 */
		public Object getValueAt(int numSample, int numChannel)
		{			
			InPlaceDataStoreFilter df = (InPlaceDataStoreFilter)dataFilter;
			Object obj = inputDataStore.getValueAt(numSample, numChannel);
			if (obj instanceof Float){
				float inputValue = ((Float)obj).floatValue();
				
				//System.out.println("get value at "+numSample+" "+numChannel+" input is "+inputValue);
				
				float value = inputValue;
				int xChannel = dataFilter.getFilterDescription().getChannelX();
				if (numChannel == xChannel){
					
					//Ask our in-place data filter about which value should we return for x
					value = df.getXValue(numSample, inputValue);				
					//
				}
				else if (numChannel == dataFilter.getFilterDescription().getChannelY()){
					
					float xValue = Float.NaN;
					if (numChannel != xChannel){
						Object xObj = getValueAt(numSample, xChannel);
						if (xObj instanceof Float){
							xValue = ((Float)xObj).floatValue();
						}
					}
					else{
						xValue = inputValue;
					}
					
					//Ask our in-place data filter about which value should we return for y
					//System.out.println("x value (channel "+xChannel +") of sample "+numSample+" is "+xValue+". Input value is "+inputValue);
					value = df.getYValue(numSample, xValue, inputValue);
					//
				}
				//System.out.println("value is "+value);
				return new Float(value);
			}
			return obj;
		}
	}
}
