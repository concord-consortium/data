/**
 * 
 */
package org.concord.data.state;

import org.concord.data.stream.DifferentiatingProducerFilter;
import org.concord.data.stream.smoothing.HoltsMethodSmoothing;
import org.concord.framework.data.stream.DataProducer;

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
	
	public DataProducer getSource() 
	{
		DataProducer firstSource = super.getSource();
		OTDifferentiatingProducerFilter otFilter = (OTDifferentiatingProducerFilter) otObject;

		switch(otFilter.getK0()){
		case OTDifferentiatingProducerFilter.OPTION_k0_No_Smoothing:			
			return firstSource;
		case OTDifferentiatingProducerFilter.OPTION_k0_Holts_Method_Smoothing:
			HoltsMethodSmoothing smoother = new HoltsMethodSmoothing();
			smoother.setSource(firstSource);
			smoother.setSourceChannel(otFilter.getSourceChannel());
			if(!Float.isNaN(otFilter.getK1())){
				smoother.setLevelSmoothingConstant(otFilter.getK1());
			}
			if(!Float.isNaN(otFilter.getK2())){
				smoother.setTrendSmoothingConstant(otFilter.getK2());
			}
			return smoother;
		}

		throw new IllegalStateException("Unhandled k0 value: " + otFilter.getK0());
	}
}
