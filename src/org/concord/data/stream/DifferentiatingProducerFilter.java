/**
 * 
 */
package org.concord.data.stream;

import org.concord.framework.data.stream.DataStreamDescription;

/**
 * FIXME this is just a copy of the Integrating filter right now.  It needs to be updated
 * to take the derivative of the data.
 * 
 * @author scott
 *
 */
public class DifferentiatingProducerFilter extends DataProducerFilter
{
	/**
	 * Assume the independentChannel is channel 0 if no one sets it.
	 */
	protected int independentChannel = 0;
	
	boolean started = false;
	boolean filteredValueValid = false;
	float previousValue = Float.NaN;
	float previousIndependentValue = Float.NaN;	
	
	protected float filter(int sampleStartOffset, float[] values) 
	{
		if(!started){
			started = true;
		} 
		
		float currentValue = values[sampleStartOffset + getTranslatedSourceChannel()];
		
		if(Float.isNaN(previousValue)){
			previousValue = currentValue;
			DataStreamDescription dataDesc = getSource().getDataDescription();
	        if(dataDesc.getDataType() ==
	            DataStreamDescription.DATA_SERIES) {
	        	previousIndependentValue = getIndependentValue(sampleStartOffset, values);
	        }
			return Float.NaN;
		}

		filteredValueValid = true;
		
		float deltaY = currentValue - previousValue;	
		float deltaX = getIndependentDifference(sampleStartOffset, values);

		previousValue = currentValue;
        if(dataDesc.getDataType() ==
            DataStreamDescription.DATA_SERIES) {
        	previousIndependentValue = getIndependentValue(sampleStartOffset, values);
        }
		
		
		return deltaY/deltaX;
	}

	public int getIndependentChannel() 
	{
		return independentChannel;
	}

	public void setIndependentChannel(int channel)
	{
		independentChannel = channel;
	}
	
	protected float getIndependentValue(int sampleStartOffset, float[] values)
	{
		return values[sampleStartOffset + getIndependentChannel()];	
	}
	
	protected boolean isFilteredValueValid() 
	{
		
		// TODO Auto-generated method stub
		return super.isFilteredValueValid();
	}
	
	/* (non-Javadoc)
	 * @see org.concord.data.stream.DataProducerFilter#reset()
	 */
	public void reset()
	{
	    // TODO Auto-generated method stub
	    super.reset();
	    
	    filteredValueValid = false;
	    started = false;
	}
	
	protected float getIndependentDifference(int sampleStartOffset, float[] values)
	{
		DataStreamDescription dataDesc = getSource().getDataDescription();
        if(dataDesc.getDataType() ==
            DataStreamDescription.DATA_SEQUENCE) {
            // has a dt
        	return dataDesc.getDt();
        } else {        	
        	return getIndependentValue(sampleStartOffset, values) - previousIndependentValue;
        }

	}
}
