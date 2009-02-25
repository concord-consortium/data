package org.concord.data.state;

public interface OTTimeLimitDataProducerFilter extends OTDataProducerFilter
{
	// max time that dp will run, in ms
	public int getTimeLimit();
	public void setTimeLimit(int timeLimit);
	public static int DEFAULT_timeLimit = 0;
}
