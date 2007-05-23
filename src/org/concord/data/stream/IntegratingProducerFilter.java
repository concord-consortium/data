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
	float sum = 0;
	float offset = 0;
	
	/* (non-Javadoc)
	 * @see org.concord.data.stream.DataProducerFilter#filter(float)
	 */
	protected float filter(float value)
	{
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
	    
	}

	public float getOffset()
    {
    	return offset;
    }

	public void setOffset(float offset)
    {
    	this.offset = offset;
    }
}
