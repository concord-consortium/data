/**
 * 
 */
package org.concord.data.state;

/**
 * @author scott
 *
 */
public interface OTDataProducerFilter
    extends OTDataProducer
{
	public OTDataProducer getSource();
	public void setSource(OTDataProducer source);
	
	public static int DEFAULT_sourceChannel = 0;
	public int getSourceChannel();
	public void setSourceChannel(int channel);
	
	public OTDataChannelDescription getChannelDescription();
	public void setChannelDescription(OTDataChannelDescription desc);	
}
