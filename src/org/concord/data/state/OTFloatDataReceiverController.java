package org.concord.data.state;

import org.concord.framework.data.stream.DataProducer;
import org.concord.framework.otrunk.DefaultOTController;
import org.concord.framework.otrunk.OTObject;

public class OTFloatDataReceiverController extends DefaultOTController
{
	public static Class [] realObjectClasses =  {FloatDataReceiver.class};
	public static Class otObjectClass = OTFloatDataReceiver.class;
	
	public void loadRealObject(Object realObject)
	{
		OTFloatDataReceiver otReceiver = (OTFloatDataReceiver) otObject;
		FloatDataReceiver receiver = (FloatDataReceiver) realObject;
		DataProducer producer = (DataProducer) controllerService.getRealObject(otReceiver.getDataProducer());
		receiver.setOTFloat(otReceiver);
		receiver.setDataProducer(producer);
	}

	public void registerRealObject(Object realObject)
	{
		// TODO Auto-generated method stub

	}

	public void saveRealObject(Object realObject)
	{
		// TODO Auto-generated method stub

	}
	
	public boolean isRealObjectSharable(OTObject otObject, Object realObject) 
	{
		return true;
	}

}
