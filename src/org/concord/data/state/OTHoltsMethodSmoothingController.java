/**
 * 
 */
package org.concord.data.state;

import org.concord.data.stream.smoothing.HoltsMethodSmoothing;

/**
 * @author scott
 *
 */
public class OTHoltsMethodSmoothingController extends OTDataProducerFilterController
{
	public static Class [] realObjectClasses =  {HoltsMethodSmoothing.class};	
	public static Class otObjectClass = OTHoltsMethodSmoothing.class;    	

	public void loadRealObject(Object realObject)
	{
		super.loadRealObject(realObject);
		
		OTHoltsMethodSmoothing otFilter = (OTHoltsMethodSmoothing) otObject;
		HoltsMethodSmoothing filter = (HoltsMethodSmoothing) realObject;
		
		filter.setLevelSmoothingConstant(otFilter.getK0());
		filter.setTrendSmoothingConstant(otFilter.getK1());
	}

	public void saveRealObject(Object realObject)
	{
		super.saveRealObject(realObject);
		
		OTHoltsMethodSmoothing otFilter = (OTHoltsMethodSmoothing) otObject;
		HoltsMethodSmoothing filter = (HoltsMethodSmoothing) realObject;

		otFilter.setK0(filter.getLevelSmoothingConstant());
		otFilter.setK1(filter.getTrendSmoothingConstant());
	}
}
