package org.concord.data.stream;

import org.concord.framework.data.stream.DataStreamDescription;
import org.concord.framework.data.stream.DataStreamEvent;
import org.concord.framework.startable.StartableInfo;

public class SingleYPerXProducerFilter extends DataProducerFilter {
    private int timeChannel = 0;
    private float lastX = Float.NaN;
    private float[] lastValues = new float[100];
    
    public SingleYPerXProducerFilter() {
        super();
        clearLastValues();
    }
    
    private void clearLastValues() {
        lastX = Float.NaN;
        for (int i = 0; i < 100; i++) { lastValues[i] = Float.NaN; }
    }
    
    @Override
    protected void dataReceived(DataStreamEvent dataEvent)
    {
        if (isRunning()) {
            boolean notify = false;
            
            if(source.getDataDescription().getDataType() == DataStreamDescription.DATA_SEQUENCE) {
                this.dataEvent.data = dataEvent.data;
                this.dataEvent.setNumSamples(dataEvent.getNumSamples());
                notify = true;
            } else {
                int numSamples = dataEvent.getNumSamples();
                int offset = dataEvent.getDataDescription().getDataOffset();
                int stride = dataEvent.getDataDescription().getNextSampleOffset();
                int channelsPerSample = dataEvent.getDataDescription().getChannelsPerSample();
                
                // do the actual filtering
                int filteredOffset = 0;
                int filteredSampleCount = 0;
                for(int i=0; i<numSamples; i++){
                    float currentX = dataEvent.data[offset+timeChannel];
                    if (! Float.isNaN(currentX)) {
                        if (lastX != currentX) {
                            // copy the values for the lastX into the dataEvent
                            for(int j=0; j<channelsPerSample; j++){
                                this.dataEvent.data[filteredOffset+j] = lastValues[j];
                            }
                            
                            if (! Float.isNaN(lastX)) {
                                notify = true;
                            }
                            lastX = currentX;
                            filteredOffset += stride;
                            filteredSampleCount++;
                        }
                    }
                    
                    // copy the current values into the placeholder
                    for(int j=0; j<channelsPerSample; j++){
                        lastValues[j] = dataEvent.data[offset+j];
                    }
                                
                    offset += stride;
                }
                this.dataEvent.setNumSamples(filteredSampleCount);
            }
            
            if (notify) {
                notifyDataReceived();
            }
        }
    }
    
    @Override
    public void reset() {
        clearLastValues();
        super.reset();
    }
    
    @Override
    public void stop() {
        // If the most recent X value is valid and we can't start without being refreshed, be sure to flush the data point
        StartableInfo info = source.getStartableInfo();
        if (info == null) {
            info = new StartableInfo();
        }
        if ((! Float.isNaN(lastX)) && (! info.canRestartWithoutReset)) {
            this.dataEvent.data = lastValues;
            this.dataEvent.setNumSamples(1);
            lastX = Float.NaN;
            notifyDataReceived();
        }
        super.stop();
    }
    
    public void setTimeChannel(int channel) {
        this.timeChannel = channel;
    }
    
    @Override
    public StartableInfo getStartableInfo() {
        return source.getStartableInfo();
    }
    
    @Override
    public DataStreamDescription getDataDescription() {
        return dataDesc;
    }
}
