package org.concord.data.state;

import org.concord.framework.otrunk.DefaultOTController;

public class OTDataStoreController extends DefaultOTController 
{
	public static Class [] realObjectClasses =  {OTDataStoreView.class};	
	public static Class otObjectClass = OTDataStore.class;    

	public void loadRealObject(Object realObject) 
	{
		OTDataStoreView dsView = (OTDataStoreView) realObject;
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

}
