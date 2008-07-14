package org.concord.data.state;

import org.concord.data.Unit;
import org.concord.data.stream.TimerDataStoreDataProducer;
import org.concord.framework.data.stream.DataChannelDescription;
import org.concord.framework.data.stream.DataStore;
import org.concord.framework.data.stream.DataStreamDescription;
import org.concord.framework.otrunk.DefaultOTController;

public class OTTimerDataStoreDataProducerController extends DefaultOTController
{
	public static Class [] realObjectClasses =  {TimerDataStoreDataProducer.class};	
	public static Class otObjectClass = OTTimerDataStoreDataProducer.class;   
	
	public void loadRealObject(Object realObject)
	{
		TimerDataStoreDataProducer dataProducer = (TimerDataStoreDataProducer) realObject;
		OTTimerDataStoreDataProducer otDataProducer = (OTTimerDataStoreDataProducer) otObject;
		
		float sampleTime = otDataProducer.getSampleTime();
        DataStreamDescription dataDescription = dataProducer.getDataDescription();
        dataDescription.setDt(sampleTime);      
        DataChannelDescription chDesc = dataDescription.getDtChannelDescription();
        chDesc.setUnit(Unit.getUnit(Unit.UNIT_CODE_S));
        chDesc.setName("time");
        
        chDesc = dataDescription.getChannelDescription(0);
        chDesc.setUnit(Unit.getUnit(Unit.UNIT_CODE_METER));
        chDesc.setName("distance");
        
        dataProducer.setNumChannels(2);
        dataProducer.getDataDescription().setDataType(DataStreamDescription.DATA_SERIES);
        
        DataStore dataStore = (DataStore) controllerService.getRealObject(otDataProducer.getDataStore());
        dataProducer.setDataStore(dataStore);
	}

	public void registerRealObject(Object realObject)
	{
		// TODO Auto-generated method stub

	}

	public void saveRealObject(Object realObject)
	{
		// TODO Auto-generated method stub

	}

}
