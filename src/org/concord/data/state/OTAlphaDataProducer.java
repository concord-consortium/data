package org.concord.data.state;


public interface OTAlphaDataProducer extends OTDataProducer
{
	int DEFAULT_numChannels = 2;
	int getNumChannels();
	void setNumChannels(int numChannels);
	
	float DEFAULT_sum = 1;
	float getSum();
	void setSum(float sum);
	
	int DEFAULT_step = 0;
	int getStep();
	void setStep(int step);
}
