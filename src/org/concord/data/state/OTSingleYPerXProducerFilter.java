package org.concord.data.state;

public interface OTSingleYPerXProducerFilter extends OTDataProducerFilter {
    public static int DEFAULT_timeChannel = 0;
    public int getTimeChannel();
    public void setTimeChannel(int channel);
}
