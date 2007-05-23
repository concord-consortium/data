/**
 * 
 */
package org.concord.data.state;

import org.concord.data.stream.LinearProducerFilter;

/**
 * @author scott
 *
 */
public class OTLinearProducerFilterController extends OTDataProducerFilterController
{
	public static Class [] realObjectClasses =  {LinearProducerFilter.class};	
	public static Class otObjectClass = OTLinearProducerFilter.class;    
	
	/* (non-Javadoc)
	 * @see org.concord.framework.otrunk.OTController#loadRealObject(java.lang.Object)
	 */
	public void loadRealObject(Object realObject)
	{
		super.loadRealObject(realObject);
		
		OTLinearProducerFilter otFilter = (OTLinearProducerFilter) otObject;
		LinearProducerFilter filter = (LinearProducerFilter) realObject;
		
		filter.setK0(otFilter.getK0());
		filter.setK1(otFilter.getK1());
	}
	
	/* (non-Javadoc)
	 * @see org.concord.data.state.OTDataProducerFilterController#saveRealObject(java.lang.Object)
	 */
	public void saveRealObject(Object realObject)
	{
	    super.saveRealObject(realObject);
	    
		OTLinearProducerFilter otFilter = (OTLinearProducerFilter) otObject;
		LinearProducerFilter filter = (LinearProducerFilter) realObject;
		
		otFilter.setK0(filter.getK0());
		otFilter.setK1(filter.getK1());
	}
	
}
