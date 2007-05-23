/**
 * 
 */
package org.concord.data.state;

/**
 * @author scott
 *
 */
public interface OTLinearProducerFilter
    extends OTDataProducerFilter
{
	public float getK0();
	public void setK0(float k0);
	
	public float getK1();
	public void setK1(float k1);
}
