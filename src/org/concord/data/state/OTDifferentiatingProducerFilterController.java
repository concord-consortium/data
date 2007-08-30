/**
 * 
 */
package org.concord.data.state;

import org.concord.data.stream.DifferentiatingProducerFilter;

/**
 * @author scott
 *
 */
public class OTDifferentiatingProducerFilterController extends OTDataProducerFilterController
{
	public static Class [] realObjectClasses =  {DifferentiatingProducerFilter.class};	
	public static Class otObjectClass = OTDifferentiatingProducerFilter.class;    	

	public void loadRealObject(Object realObject)
	{
		super.loadRealObject(realObject);
		
		OTDifferentiatingProducerFilter otFilter = (OTDifferentiatingProducerFilter) otObject;
		DifferentiatingProducerFilter filter = (DifferentiatingProducerFilter) realObject;
		
		filter.setIndependentChannel(otFilter.getIndependentChannel());
	}

	public void saveRealObject(Object realObject)
	{
		super.saveRealObject(realObject);
		
		OTDifferentiatingProducerFilter otFilter = (OTDifferentiatingProducerFilter) otObject;
		DifferentiatingProducerFilter filter = (DifferentiatingProducerFilter) realObject;

		otFilter.setIndependentChannel(filter.getIndependentChannel());
	}
}
