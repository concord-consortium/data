/**
 * 
 */
package org.concord.data.state;

/**
 * @author scott
 *
 */
public interface OTDifferentiatingProducerFilter
    extends OTDataProducerFilter
{
	public final static int DEFAULT_independentChannel = 0;
	public int getIndependentChannel();
	public void setIndependentChannel(int channel);    	
}
