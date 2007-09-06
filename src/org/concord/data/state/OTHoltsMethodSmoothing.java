package org.concord.data.state;

public interface OTHoltsMethodSmoothing extends OTDataProducerFilter 
{
	// this is for setting the levelSmoothingConstant
	// it is named k0 so this will work in the diy without modifications.
	public static float DEFAULT_k0 = 0.5f;
	public float getK0();
	public void setK0(float levelSmoothingConstant);

	// this is for setting the trendSmoothingConstant
	// it is named k1 so this will work in the diy without modifications.
	public static float DEFAULT_k1 = 0.5f;
	public float getK1();
	public void setK1(float trendSmoothingConstant);

}
