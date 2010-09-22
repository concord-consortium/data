package org.concord.data.state;

import org.concord.data.stream.DataChannelProducerFilter;


public class OTDataChannelProducerFilterController extends OTDataProducerFilterController {
    public static Class [] realObjectClasses =  {DataChannelProducerFilter.class};  
    public static Class otObjectClass = OTDataChannelProducerFilter.class;    
    
    /* (non-Javadoc)
     * @see org.concord.framework.otrunk.OTController#loadRealObject(java.lang.Object)
     */
    @Override
    public void loadRealObject(Object realObject)
    {
        super.loadRealObject(realObject);
        
        OTDataChannelProducerFilter otFilter = (OTDataChannelProducerFilter) otObject;
        DataChannelProducerFilter filter = (DataChannelProducerFilter) realObject;

        for (Object channelNum : otFilter.getChannelsToDiscard()) {
            Integer channel = (Integer) channelNum;
            filter.addChannelToDiscard(channel.intValue());
        }
    }
}
