package org.concord.data.stream;

import org.concord.framework.data.stream.DataStreamDescription;
import org.concord.framework.data.stream.DataStreamEvent;

public class SingleYPerXProducerFilter extends DataProducerFilter {
    private int timeChannel = 0;
    private float lastX = Float.NaN;
    private float[] lastValues = new float[100];
    
    public SingleYPerXProducerFilter() {
        super();
        for (int i = 0; i < 100; i++) { lastValues[i] = Float.NaN; }
    }
    
    @Override
    protected void dataReceived(DataStreamEvent dataEvent)
    {
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
    
    public void setTimeChannel(int channel) {
        this.timeChannel = channel;
    }
}
