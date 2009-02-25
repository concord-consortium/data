package org.concord.data.state;

import org.concord.data.stream.TimeLimitDataProducerFilter;
import org.concord.framework.otrunk.OTChangeEvent;
import org.concord.framework.otrunk.OTChangeListener;
import org.concord.framework.otrunk.OTChangeNotifying;

public class OTTimeLimitDataProducerFilterController extends
        OTDataProducerFilterController
{
	public static Class [] realObjectClasses =  {TimeLimitDataProducerFilter.class};	
	public static Class otObjectClass = OTTimeLimitDataProducerFilter.class;
	private OTChangeListener changeListener;
	private TimeLimitDataProducerFilter realObject;    
	
	/* (non-Javadoc)
	 * @see org.concord.framework.otrunk.OTController#loadRealObject(java.lang.Object)
	 */
	public void loadRealObject(Object realObject)
	{
		super.loadRealObject(realObject);
		
		OTTimeLimitDataProducerFilter otFilter = (OTTimeLimitDataProducerFilter) otObject;
		TimeLimitDataProducerFilter filter = (TimeLimitDataProducerFilter) realObject;
		
		filter.setTimeLimit(otFilter.getTimeLimit());
	}
	
	/* (non-Javadoc)
	 * @see org.concord.data.state.OTDataProducerFilterController#saveRealObject(java.lang.Object)
	 */
	public void saveRealObject(Object realObject)
	{
	    super.saveRealObject(realObject);
	    
	    OTTimeLimitDataProducerFilter otFilter = (OTTimeLimitDataProducerFilter) otObject;
		TimeLimitDataProducerFilter filter = (TimeLimitDataProducerFilter) realObject;
		
		otFilter.setTimeLimit(filter.getTimeLimit());
	}
	
	public void registerRealObject(Object realObject) 
	{
		this.realObject = (TimeLimitDataProducerFilter) realObject;
		
		super.registerRealObject(realObject);
		changeListener = new OTChangeListener(){
			public void stateChanged(OTChangeEvent e) 
			{
				OTTimeLimitDataProducerFilter otFilter = (OTTimeLimitDataProducerFilter) otObject;
				TimeLimitDataProducerFilter filter = OTTimeLimitDataProducerFilterController.this.realObject;
				
				filter.setTimeLimit(otFilter.getTimeLimit());
			}
			
		};
		((OTChangeNotifying)otObject).addOTChangeListener(changeListener);
	}
}
