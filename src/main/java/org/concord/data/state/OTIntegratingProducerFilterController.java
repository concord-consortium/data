/**
 * 
 */
package org.concord.data.state;

import org.concord.data.stream.IntegratingProducerFilter;

/**
 * @author scott
 *
 */
public class OTIntegratingProducerFilterController extends OTDataProducerFilterController
{
	public static Class [] realObjectClasses =  {IntegratingProducerFilter.class};	
	public static Class otObjectClass = OTIntegratingProducerFilter.class;    
	
	/* (non-Javadoc)
	 * @see org.concord.framework.otrunk.OTController#loadRealObject(java.lang.Object)
	 */
	public void loadRealObject(Object realObject)
	{
		super.loadRealObject(realObject);
		
		OTIntegratingProducerFilter otFilter = (OTIntegratingProducerFilter) otObject;
		IntegratingProducerFilter filter = (IntegratingProducerFilter) realObject;

		if(otFilter.isResourceSet("offset")){
			filter.setOffset(otFilter.getOffset());
		}
	}
	
	/* (non-Javadoc)
	 * @see org.concord.data.state.OTDataProducerFilterController#saveRealObject(java.lang.Object)
	 */
	public void saveRealObject(Object realObject)
	{
	    super.saveRealObject(realObject);
	    
	    OTIntegratingProducerFilter otFilter = (OTIntegratingProducerFilter) otObject;
	    IntegratingProducerFilter filter = (IntegratingProducerFilter) realObject;
		
	    if(filter.isOffsetSet()){
	    	otFilter.setOffset(filter.getOffset());
	    }
	}
	
}
