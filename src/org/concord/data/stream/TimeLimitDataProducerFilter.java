package org.concord.data.stream;

import java.util.Timer;
import java.util.TimerTask;

/**
 * This data filter is designed to stop a data producer after a set
 * amount of time.
 * 
 * After the time is up, it will stop the data producer and send a
 * DATA_STOPPED event, which should be handled by any controller
 * on the other end (e.g. to re-set start/stop buttons).
 * 
 * @author sfentress
 *
 */
public class TimeLimitDataProducerFilter extends DataProducerFilter
{
	private int timeLimit;
	private TimerTask timeLimitTask;
	private Timer timer;
	private int totalTime = 0;
	private float dt = 0;
	private boolean usingDt = false;
	
	/**
	 * @param timeLimit Time before data producer is stopped after a
	 * start event, in ms.
	 */
	public void setTimeLimit(int timeLimit){
		this.timeLimit = timeLimit;
		resetTimer();
		dt = source.getDataDescription().getDt();
		if (dt > 0){
			usingDt = true;
		}
	}
	
	public int getTimeLimit(){
		return timeLimit;
	}
	
	private void resetTimer(){
		if (usingDt){
			totalTime = 0;
		} else {
			timer = new Timer();
    		if (timeLimitTask != null) {
    			timeLimitTask.cancel();
    			timer.purge();
    		}
    		timeLimitTask = new TimerTask() {
    
    			public void run()
    			{
    				stopDataProducer();
    			}
    		};
		}
	}
	
	private void startTimer(){
		if (!usingDt){
			timer.schedule(timeLimitTask, timeLimit);
		}
	}
	
	private void stopDataProducer(){
		stop();
	}
	
	/* (non-Javadoc)
     * @see org.concord.framework.data.DataFlow#start()
     */
    public void start()
    {
    	resetTimer();
    	startTimer();
    	super.start();
    }
    
    public void stop()
    {
    	resetTimer();
    	super.stop();
    }
    
    /* (non-Javadoc)
     * @see org.concord.framework.data.DataFlow#reset()
     */
    public void reset()
    {
    	resetTimer();
    	super.reset();
    }
    
    protected float filter(int sampleStartOffset, float [] values)
    {
    	int producerChannel = getTranslatedSourceChannel();
    	float value = values[sampleStartOffset + producerChannel];
    	if (usingDt){
    		totalTime += (int) (dt * 1000f);
    		if (totalTime > timeLimit){
    			stopDataProducer();
    		}
    	}
    	return value;    	
    }	
}
