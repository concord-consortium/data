/**
 * 
 */
package org.concord.data.stream;

/**
 * @author scott
 *
 */
public class LinearProducerFilter extends DataProducerFilter
{
	float k0;
	float k1;
	
	/* (non-Javadoc)
	 * @see org.concord.data.stream.DataProducerFilter#filter(float)
	 */
	protected float filter(float value)
	{
		return k0 + k1*value;
	}

	public float getK0()
    {
    	return k0;
    }

	public void setK0(float k0)
    {
    	this.k0 = k0;
    }

	public float getK1()
    {
    	return k1;
    }

	public void setK1(float k1)
    {
    	this.k1 = k1;
    }

}
