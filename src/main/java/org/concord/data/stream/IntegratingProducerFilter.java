/**
 * 
 */
package org.concord.data.stream;

/**
 * @author scott
 *
 */
public class IntegratingProducerFilter extends DataProducerFilter
{
	boolean started = false;
	
	float sum = 0;
	
	boolean offsetSet = false;
	float offset = 0;
	
	/* (non-Javadoc)
	 * @see org.concord.data.stream.DataProducerFilter#filter(float)
	 */
	protected float filter(float value)
	{
		if(!started && !offsetSet){
			offset = value;
			started = true;
		} 
		
		sum += value - offset;
		return sum;
	}

	/* (non-Javadoc)
	 * @see org.concord.data.stream.DataProducerFilter#reset()
	 */
	public void reset()
	{
	    // TODO Auto-generated method stub
	    super.reset();
	    
	    sum = 0;
	    started = false;
	}

	public float getOffset()
    {
    	return offset;
    }

	public void setOffset(float offset)
    {
    	this.offset = offset;
    	offsetSet = true;
    }
	
	public boolean isOffsetSet()
	{
		return offsetSet;
	}
}
