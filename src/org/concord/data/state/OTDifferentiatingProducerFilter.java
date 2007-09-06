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
	
	/// These properties are so this filter can be used in the DIY without modification
	
	/**
	 * This sets the type of smoothing to be used by the filter
	 */
	public final static int OPTION_k0_No_Smoothing = 0;
	public final static int OPTION_k0_Holts_Method_Smoothing = 1;
	public final static int DEFAULT_k0=OPTION_k0_No_Smoothing;
	public int getK0();
	
	/**
	 * This value will be passed to the smoothing filter as its k0
	 * @return
	 */
	public final static float DEFAULT_k1=Float.NaN;
	public float getK1();
	
	/**
	 * This value will be passed to the smoothing filter as its k1
	 * @return
	 */
	public final static float DEFAULT_k2=Float.NaN;
	public float getK2();
	
	/**
	 * This value will be passed to the smoothing filter as its k2
	 * @return
	 */
	public final static float DEFAULT_k3=Float.NaN;
	public float getK3();
	
}
