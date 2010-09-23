/**
 * 
 */
package org.concord.data.stream;

import java.util.logging.Logger;

import org.concord.framework.data.stream.DataChannelDescription;
import org.concord.framework.data.stream.DataListener;
import org.concord.framework.data.stream.DataProducer;
import org.concord.framework.data.stream.DataStreamDescription;
import org.concord.framework.data.stream.DataStreamEvent;
import org.concord.framework.data.stream.DefaultDataProducer;
import org.concord.framework.startable.StartableEvent;
import org.concord.framework.startable.StartableListener;

/**
 * @author scott
 *
 */
public abstract class DataProducerFilter extends DefaultDataProducer
{
	private static final Logger logger = Logger
			.getLogger(DataProducerFilter.class.getCanonicalName());
	
    protected DataProducer source;
	private DataListener dataListener;
	private int sourceChannel;
	protected int currentSample;

	private StartableListener startableListener;
    
	public DataProducerFilter(){
		/*
		 * Send the data listening to this inner class so
		 * it hides that from users of this class.
		 */
		this.dataListener = new DataListener() {
			public void dataReceived(DataStreamEvent dataEvent)
			{
				DataProducerFilter.this.dataReceived(dataEvent);
			}  

			public void dataStreamEvent(DataStreamEvent dataEvent)
			{
				updateDescription();

				notifyDataStreamEvent(dataEvent.getType());
			}
		};
		
		this.startableListener = new StartableListener() {

			public void startableEvent(StartableEvent event) {
				StartableEvent clone = event.clone();
				clone.setStartable(DataProducerFilter.this);
				notifyStartableListeners(clone);				
			}
			
		};
		
		// make sure values has enough space for our data
		// this array is the same one used in dataEvent.data
		values = new float [100];
		dataEvent.data = values;
		currentSample = 0;
	}

	/* (non-Javadoc)
     * @see org.concord.framework.data.stream.DataConsumer#addDataProducer(org.concord.framework.data.stream.DataProducer)
     */
    public void setSource(DataProducer source)
    {
        if(this.source != null) {
            this.source.removeDataListener(dataListener);
            this.source.removeStartableListener(startableListener);
        }
        this.source = source;

        if(source == null) {
            return;
        }
        
        updateDescription();
        this.source.addDataListener(dataListener);
        this.source.addStartableListener(startableListener);
    }

    protected void updateDescription()
    {
    	DataStreamDescription srcDesc = source.getDataDescription();
    	dataDesc.setDt(srcDesc.getDt());
    	dataDesc.setChannelsPerSample(srcDesc.getChannelsPerSample());
    	dataDesc.setDataType(srcDesc.getDataType());
    	dataDesc.setDataOffset(srcDesc.getDataOffset());
    	dataDesc.setNextSampleOffset(srcDesc.getNextSampleOffset());
    	
        for (int i = 0; i < srcDesc.getChannelsPerSample(); i++) {
            DataChannelDescription channelDesc = srcDesc.getChannelDescription(i);
            dataDesc.setChannelDescription(channelDesc, i);
        }
    }
    
    public DataProducer getSource()
    {
    	return source;
    }
    
    /* (non-Javadoc)
     * @see org.concord.framework.data.DataFlow#reset()
     */
    @Override
    public void reset()
    {
    	// we count on the source to send the appropriate events so we don't need 
    	// to call the super method
        if(source != null) {
            source.reset();
        }
        
        currentSample = 0;
    }

    /* (non-Javadoc)
     * @see org.concord.framework.data.DataFlow#stop()
     */
    @Override
    public void stop()
    {
    	// we count on the source to send the appropriate events so we don't need 
    	// to call the super method
        if(source != null) {
        	logger.finer("stopping "+source);
            source.stop();
        }
    }

    /* (non-Javadoc)
     * @see org.concord.framework.data.DataFlow#start()
     */
    @Override
    public void start()
    {
    	// we count on the source to send the appropriate events so we don't need 
    	// to call the super method
        if(source != null) {
            source.start();
        }
    }

    protected void dataReceived(DataStreamEvent dataEvent)
    {
        int producerChannel = getTranslatedSourceChannel();
        int numSamples = dataEvent.getNumSamples();
        int offset = dataEvent.getDataDescription().getDataOffset();
        int stride = dataEvent.getDataDescription().getNextSampleOffset();
        int channelsPerSample = dataEvent.getDataDescription().getChannelsPerSample();
        
        int filteredSamples = 0;
        int filteredOffset = offset;
        
        // do the actual filtering
        for(int i=0; i<numSamples; i++){
        	
        	// initialize the other channels
        	for(int j=0; j<channelsPerSample; j++){
        		dataEvent.data[filteredOffset+j] = dataEvent.data[offset+j];
        	}
        	
        	float filteredValue = filter(offset, dataEvent.data); 
        	        	
        	offset += stride;

        	if(!isFilteredValueValid()){
        		// the value is not valid so we should not increase the currentSample
        		// or the filteredOffset
        		continue;
        	}
        	
        	this.dataEvent.data[filteredOffset + producerChannel] = filteredValue;         	        	
        	
        	filteredOffset += stride;
        	filteredSamples++;
        	
        	currentSample++;

        }
        
        dataEvent.setNumSamples(filteredSamples);
        notifyDataReceived();        
    }

    
    protected float filter(int sampleStartOffset, float [] values)
    {
    	int producerChannel = getTranslatedSourceChannel();
    	float value = values[sampleStartOffset + producerChannel];
    	
    	return filter(value);    	
    }

    /**
     * You can optionally override this method if your filter only works with one channel at a time
     *  
     * @param value
     * @return
     */
    protected float filter(float value)
    {
    	throw new IllegalStateException("The filter(float)  method needs to be overriden " +
    			"if the filter(int, float []) is not overriden");
    }

    
    
    /**
     * This method can be overridden to return false when the value returned by 
     * the filter method should not be used.  It will be called after the filter
     * method is called. 
     * 
     * This will be used for the differentiating filter because it won't have a valid
     * return value until 2 points have been passed in. 
     * 
     * @return
     */
    protected boolean isFilteredValueValid()
    {
    	return true;
    }
    
    /**
     * get the channel number that will be filtered.  This translates
     * the sourceChannel to the actual channel in the source data producer
     * If the sourceChannel is 0 it means the first channel.  If the dataProducer
     * has a dt then this will substract 1 from the sourceChannel.  So sourceChannel
     * of 0 returns a translatedSourceChannel of -1.  -1 is the dt value in a data
     * producer.   
     * @return
     */
    protected int getTranslatedSourceChannel()
    {
        if(source == null) {
            throw new RuntimeException("Null source");
        }
        
        if(source.getDataDescription().getDataType() ==
            DataStreamDescription.DATA_SEQUENCE) {
            // has a dt
            return sourceChannel - 1;
        } else {
            return sourceChannel;
        }
    }
    
    /**
     * set the channel number that will be filtered
     * this uses the virtual channel like is used by the DataGraphable
     * class.  So channel 0 means the first channel.  Even if the 
     * DataProducer has a dt which makes the first channel -1.  If the
     * DataProducer doesn't have a dt then the first channel is 0.  
     * 
     * @param channelNumber
     */
    public void setSourceChannel(int channelNumber)
    {
        // set the channel number 
    	sourceChannel = channelNumber;
    }   
    
    public int getSourceChannel()
    {
    	return sourceChannel;
    }
    
    @Override
    public boolean isRunning() {
    	if(source == null){
    		return false;
    	}
    	
    	return source.isRunning();
    }
    
    @Override
    public boolean isInInitialState() {
    	if(source == null){
    		return true;
    	}
    	
    	return source.isInInitialState();
    }
    
    @Override
    public boolean isAtEndOfStream() {
        if(source == null){
            return super.isAtEndOfStream();
        }
        
        return source.isAtEndOfStream();
    }
}
