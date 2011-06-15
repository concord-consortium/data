package org.concord.data.stream.smoothing;

import org.concord.data.stream.DataProducerFilter;

public class HoltsMethodSmoothing extends DataProducerFilter 
{
	boolean filteredValueValid = false;
	float previousValue = Float.NaN;
	
	float levelSmoothingConstant = 0.75f;
	float trendSmoothingConstant = 0.75f;
	
	float trendValue = 0f;
	
	protected float filter(int sampleStartOffset, float[] values) 
	{		
		float currentValue = values[sampleStartOffset + getTranslatedSourceChannel()];
		
		if(Float.isNaN(previousValue)){
			previousValue = currentValue;
			return Float.NaN;
		}

		filteredValueValid = true;
		
		float newValue = levelSmoothingConstant * currentValue + 
			(1 - levelSmoothingConstant)*(previousValue + trendValue);
		
		trendValue = trendSmoothingConstant*(newValue - previousValue) + 
			(1 - trendSmoothingConstant)*trendValue;

		previousValue = newValue;
		
		return newValue;
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
	    trendValue = 0f;
	}

	public float getLevelSmoothingConstant() {
		return levelSmoothingConstant;
	}

	public void setLevelSmoothingConstant(float levelSmoothingConstant) {
		this.levelSmoothingConstant = levelSmoothingConstant;
	}

	public float getTrendSmoothingConstant() {
		return trendSmoothingConstant;
	}

	public void setTrendSmoothingConstant(float trendSmoothingConstant) {
		this.trendSmoothingConstant = trendSmoothingConstant;
	}
	
	
}
