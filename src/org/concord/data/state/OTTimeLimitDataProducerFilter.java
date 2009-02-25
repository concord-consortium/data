package org.concord.data.state;

public interface OTTimeLimitDataProducerFilter extends OTDataProducerFilter
{
	// max time that dp will run, in ms
	public int getTimeLimit();
	public void setTimeLimit(float timeLimit);
}
