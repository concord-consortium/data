/**
 * 
 */
package org.concord.data.state;

/**
 * @author scott
 *
 */
public interface OTIntegratingProducerFilter
    extends OTDataProducerFilter
{
	public final static float DEFAULT_offset = 0f;
	public float getOffset();
	public void setOffset(float offset);
}
