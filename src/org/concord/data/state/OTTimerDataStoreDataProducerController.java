package org.concord.data.state;

import org.concord.data.Unit;
import org.concord.data.stream.TimerDataStoreDataProducer;
import org.concord.framework.data.stream.DataChannelDescription;
import org.concord.framework.data.stream.DataStore;
import org.concord.framework.data.stream.DataStreamDescription;
import org.concord.framework.otrunk.DefaultOTController;
import org.concord.framework.otrunk.OTObject;
import org.concord.framework.otrunk.OTObjectList;

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
        
        dataProducer.setStartAtZero(otDataProducer.getStartAtZero());
        dataProducer.setKeepExistingXValues(otDataProducer.getKeepExistingXValues());
        
        if (otDataProducer.getDataStore() != null) {
            // for backwards compatibility
            dataProducer.setNumChannels(2);
            dataProducer.getDataDescription().setDataType(DataStreamDescription.DATA_SERIES);
            
            // FIXME Eventually this could be abstracted and settable on the otobject
            chDesc = dataDescription.getChannelDescription(0);
            chDesc.setUnit(Unit.getUnit(Unit.UNIT_CODE_METER));
            chDesc.setName("distance");
            
            DataStore dataStore = (DataStore) controllerService.getRealObject(otDataProducer.getDataStore());
            dataProducer.addDataStore(dataStore);
        } else {
            OTObjectList dataStores = otDataProducer.getDataStores();
            
            dataProducer.setNumChannels(dataStores.size() + 1);
            dataProducer.getDataDescription().setDataType(DataStreamDescription.DATA_SERIES);
            int channelNum = 0; 
            for (OTObject store : dataStores) {
                // FIXME Eventually this could be abstracted and settable on the otobject
                chDesc = dataDescription.getChannelDescription(channelNum);
                chDesc.setUnit(Unit.getUnit(Unit.UNIT_CODE_METER));
                chDesc.setName("distance");
                channelNum++;
                
                DataStore dataStore = (DataStore) controllerService.getRealObject(store);
                dataProducer.addDataStore(dataStore);
            }
        }
	}

	public void registerRealObject(Object realObject)
	{
		// TODO Auto-generated method stub

	}

	public void saveRealObject(Object realObject)
	{
		// TODO Auto-generated method stub

	}
	
	@Override
	public boolean isRealObjectSharable(OTObject otObject, Object realObject) {
	    return true;
	}

}
