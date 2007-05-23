/**
 * 
 */
package org.concord.data.state;

import org.concord.data.stream.DataProducerFilter;
import org.concord.framework.data.stream.DataProducer;
import org.concord.framework.otrunk.DefaultOTController;

/**
 * @author scott
 *
 */
public class OTDataProducerFilterController extends DefaultOTController
{

	/* (non-Javadoc)
	 * @see org.concord.framework.otrunk.OTController#loadRealObject(java.lang.Object)
	 */
	public void loadRealObject(Object realObject)
	{
		OTDataProducerFilter otFilter = (OTDataProducerFilter) otObject;
		DataProducerFilter filter = (DataProducerFilter) realObject;
		
		DataProducer source = 
			(DataProducer) controllerService.getRealObject(otFilter.getSource());
		filter.setSource(source);		
		filter.setSourceChannel(otFilter.getSourceChannel());
	}

	/* (non-Javadoc)
	 * @see org.concord.framework.otrunk.OTController#registerRealObject(java.lang.Object)
	 */
	public void registerRealObject(Object realObject)
	{
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.concord.framework.otrunk.OTController#saveRealObject(java.lang.Object)
	 */
	public void saveRealObject(Object realObject)
	{
		OTDataProducerFilter otFilter = (OTDataProducerFilter) otObject;
		DataProducerFilter filter = (DataProducerFilter) realObject;
		
		OTDataProducer otSource = 
			(OTDataProducer) controllerService.getOTObject(filter.getSource());
		otFilter.setSource(otSource);
		otFilter.setSourceChannel(filter.getSourceChannel());		
	}

}
