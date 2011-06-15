package org.concord.data.state;

import org.concord.data.stream.SingleYPerXProducerFilter;

public class OTSingleYPerXProducerFilterController extends OTDataProducerFilterController {
    public static Class [] realObjectClasses =  {SingleYPerXProducerFilter.class};  
    public static Class otObjectClass = OTSingleYPerXProducerFilter.class;
    
    /* (non-Javadoc)
     * @see org.concord.framework.otrunk.OTController#loadRealObject(java.lang.Object)
     */
    @Override
    public void loadRealObject(Object realObject)
    {
        super.loadRealObject(realObject);
        
        OTSingleYPerXProducerFilter otFilter = (OTSingleYPerXProducerFilter) otObject;
        SingleYPerXProducerFilter filter = (SingleYPerXProducerFilter) realObject;

        filter.setTimeChannel(otFilter.getTimeChannel());
    }
}
