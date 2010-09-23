package org.concord.data.stream;

import java.util.ArrayList;

import org.concord.framework.data.stream.DataChannelDescription;
import org.concord.framework.data.stream.DataStreamDescription;
import org.concord.framework.data.stream.DataStreamEvent;
import org.concord.framework.startable.StartableInfo;

public class DataChannelProducerFilter extends DataProducerFilter {
    ArrayList<Integer> channelsToDiscard = new ArrayList<Integer>();
    
    @Override
    protected void updateDescription()
    {
        DataStreamDescription srcDesc = source.getDataDescription();
        dataDesc.setDt(srcDesc.getDt());
        dataDesc.setChannelsPerSample(srcDesc.getChannelsPerSample() - channelsToDiscard.size());
        dataDesc.setDataType(srcDesc.getDataType());
        dataDesc.setDataOffset(srcDesc.getDataOffset());
        dataDesc.setNextSampleOffset(srcDesc.getNextSampleOffset());
        
        int chan = 0;
        for (int i = 0; i < srcDesc.getChannelsPerSample(); i++) {
            if (! channelsToDiscard.contains(i)) {
                DataChannelDescription channelDesc = srcDesc.getChannelDescription(i);
                dataDesc.setChannelDescription(channelDesc, chan);
                chan++;
            }
        }
        
    }

    public void addChannelToDiscard(int channel) {
        if (! channelsToDiscard.contains(channel)) {
            channelsToDiscard.add(channel);
        }
    }
    
    @Override
    protected void dataReceived(DataStreamEvent dataEvent)
    {
        int numSamples = dataEvent.getNumSamples();
        int offset = dataEvent.getDataDescription().getDataOffset();
        int stride = dataEvent.getDataDescription().getNextSampleOffset();
        int channelsPerSample = dataEvent.getDataDescription().getChannelsPerSample();
        
        // do the actual filtering
        for(int i=0; i<numSamples; i++){
            
            // initialize the other channels
            int chanNum = 0;
            for(int j=0; j<channelsPerSample; j++){
                if (channelsToDiscard.contains(j)) {
                    continue;
                }
                this.dataEvent.data[offset+chanNum] = dataEvent.data[offset+j];
                chanNum++;
            }
                        
            offset += stride;
        }
        
        dataEvent.setNumSamples(numSamples);
        notifyDataReceived();        
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
