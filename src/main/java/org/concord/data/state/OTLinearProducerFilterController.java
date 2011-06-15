/**
 * 
 */
package org.concord.data.state;

import org.concord.data.stream.LinearProducerFilter;
import org.concord.framework.otrunk.OTChangeEvent;
import org.concord.framework.otrunk.OTChangeListener;
import org.concord.framework.otrunk.OTChangeNotifying;

/**
 * @author scott
 *
 */
public class OTLinearProducerFilterController extends OTDataProducerFilterController
{
	public static Class [] realObjectClasses =  {LinearProducerFilter.class};	
	public static Class otObjectClass = OTLinearProducerFilter.class;
	private OTChangeListener changeListener;
	private LinearProducerFilter realObject;    
	
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
	
	public void registerRealObject(Object realObject) 
	{
		this.realObject = (LinearProducerFilter) realObject;
		
		super.registerRealObject(realObject);
		changeListener = new OTChangeListener(){
			public void stateChanged(OTChangeEvent e) 
			{
				OTLinearProducerFilter otFilter = (OTLinearProducerFilter) otObject;
				LinearProducerFilter filter = OTLinearProducerFilterController.this.realObject;
				
				filter.setK0(otFilter.getK0());
				filter.setK1(otFilter.getK1());
			}
			
		};
		((OTChangeNotifying)otObject).addOTChangeListener(changeListener);
	}
}
