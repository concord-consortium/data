/*
 * Last modification information:
 * $Revision: 1.5 $
 * $Date: 2007-06-29 20:26:11 $
 * $Author: imoncada $
 *
 * Licence Information
 * Copyright 2007 The Concord Consortium 
*/
package org.concord.data.state.filter;

import org.concord.data.state.AbstractDataStoreFilter;
import org.concord.data.state.DataStoreFilter;
import org.concord.data.state.DataStoreFilterDescription;
import org.concord.data.state.DataStoreFilterPropertyEvent;

/**
 * DataRangeFilter
 * This is a default data filter that just filters the data
 * by the range specified in the analysis description.
 * 
 * Right now it only supports one range.
 *
 * Other data filter can extend this class if they want to support the range.
 * Then they can use outputDataStore as their input data store and ignore
 * the range
 *
 * Date created: Mar 18, 2007
 *
 * @author Ingrid Moncada<p>
 *
 */
public class DataRangeFilter extends AbstractDataStoreFilter
{
	
	public DataRangeFilter()
	{
		super();
		outputDataStore = new MyFilteredDataStore(this);
	}
	
	/**
	 * @see org.concord.data.state.DataStoreFilter#setFilterDescription(org.concord.data.state.DataStoreFilterDescription)
	 */
	public void setFilterDescription(DataStoreFilterDescription desc)
		throws IllegalArgumentException
	{
		this.filterDescription = desc;
		calculateRange();
		
		super.setFilterDescription(desc);
	}
	
	/**
	 * 
	 * @throws IllegalArgumentException
	 */
	protected void calculateRange()
		throws IllegalArgumentException, UnsupportedOperationException
	{		
		String strRange = null;
		
		strRange = filterDescription.getProperty("range");
		
		if (strRange == null || strRange.equals("")){
			setEmptyRange();
			return;
		}
		
		String values[] = strRange.split(",");
		
		//Right now it doesn't support more than one range
		if (values.length > 2){
			throw new UnsupportedOperationException("Multiple ranges not supported yet");			
		}
		
		//System.out.println("calculate range "+strRange+" "+this);
		
		ranges.removeAllElements();
		
		if (values.length == 0){
			setEmptyRange();
			return;
		}
		
		try{
			
			float m, n;
			DataRange range = null;
			int i=0;
			for (i=0; (i+1) < values.length; i+=2){
				String strVal;
				
				strVal = values[i];
				if (strVal.equals("")){
					m = Float.NaN;
				}
				else{
					m = Float.parseFloat(strVal);
				}
				
				strVal = values[i+1];				
				if (strVal.equals("")){
					n = Float.NaN;
				}
				else{
					n = Float.parseFloat(strVal);
				}
				range = new DataRange(m, n);
				ranges.add(range);
			}
			if ((i+1) == values.length){
				m = Float.parseFloat(values[i]);
				range = new DataRange(m, Float.NaN);
				ranges.add(range);
			}
			
		}
		catch (NumberFormatException ex) {
			throw new IllegalArgumentException("Range must be a number. "+ex.getMessage());
		}
	}
	
	/**
	 * 
	 */
	private void setEmptyRange()
	{
		ranges.removeAllElements();
		
		DataRange range = new DataRange(Float.NaN, Float.NaN);
		ranges.add(range);
	}

	/**
	 * 
	 */
	protected void calculateResults()
		throws IllegalArgumentException
	{
		//System.out.println("calculate results "+this);
		
		if (inputDataStore == null){
			throw new IllegalArgumentException("No input data store specified.");
		}
		
		((MyFilteredDataStore)outputDataStore).calculate();		
	}

	public void propertyChange(DataStoreFilterPropertyEvent evt)
	{
		calculateRange();
		super.propertyChange(evt);
	}
	
	class MyFilteredDataStore extends DefaultFilteredDataStore
	{
		protected int firstSample = 0;
		protected int lastSample = 0;
		
		public MyFilteredDataStore(DataStoreFilter df)
		{
			super(df);
		}
		
		/**
		 * 
		 */
		public void calculate()
		{
			//Calculate range of values given the range
			
			if (ranges.size() == 0){
				firstSample = 0;
				lastSample = inputDataStore.getTotalNumSamples()-1;
				return;
			}
			
			DataRange range = (DataRange)ranges.elementAt(0);
			if (range == null){
				firstSample = 0;
				lastSample = inputDataStore.getTotalNumSamples()-1;
				return;
			}
			
			//Look for min value
			firstSample = 0;
			if (!Float.isNaN(range.minValue)){
				for (firstSample = 0; firstSample < inputDataStore.getTotalNumSamples(); firstSample++){
					Object objX = inputDataStore.getValueAt(firstSample, filterDescription.getChannelX());
					float x = ((Float)objX).floatValue();
					
					if (x >= range.minValue) {
						break;
					}
				}
			}
			
			lastSample = inputDataStore.getTotalNumSamples()-1;
			if (!Float.isNaN(range.maxValue)){
				for (int i=firstSample; i<inputDataStore.getTotalNumSamples(); i++){
					Object objX = inputDataStore.getValueAt(i, filterDescription.getChannelX());
					float x = ((Float)objX).floatValue();
					
					if (x > range.maxValue) {
						lastSample = i-1;
						break;
					}
				}
			}
			//System.out.println(" from "+ range.minValue + " to " + range.maxValue);
			//System.out.println("  first sample is "+firstSample);
			//System.out.println("  last sample is "+lastSample);
		}

		/**
		 * @see org.concord.framework.data.stream.DataStore#getTotalNumSamples()
		 */
		public int getTotalNumSamples()
		{
			return lastSample - firstSample + 1;
		}

		/**
		 * @see org.concord.framework.data.stream.DataStore#getValueAt(int, int)
		 */
		public Object getValueAt(int numSample, int numChannel)
		{
			int realNumSample = numSample + firstSample;
			return super.getValueAt(realNumSample, numChannel);
		}
	}
}
