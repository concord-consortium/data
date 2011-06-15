package org.concord.data.state;

import org.concord.framework.otrunk.DefaultOTController;
import org.concord.framework.otrunk.OTObject;

public class OTDataStoreController extends DefaultOTController 
{
	public static Class [] realObjectClasses =  {OTDataStoreRealObject.class};	
	public static Class otObjectClass = OTDataStore.class;    

	public void loadRealObject(Object realObject) 
	{
		OTDataStoreRealObject dsView = (OTDataStoreRealObject) realObject;
		dsView.setControllerService(controllerService);
		dsView.setOTDataStore((OTDataStore) otObject);
	}

	public void registerRealObject(Object realObject) 
	{
		// don't need to do anything here because the view is actually listening directly to the 
		// otObject.
	}

	public void saveRealObject(Object realObject) 
	{
		// don't need to do anything here because the view is updating the real object 
		// as it goes
	}

	/**
	 * The real object returned here can handle being used in multiple places at the 
	 * same time.  So if we return true here the number of real objects created will be reduced. 
	 */
	public boolean isRealObjectSharable(OTObject otObject, Object realObject) 
	{
		return true;
	}
}
