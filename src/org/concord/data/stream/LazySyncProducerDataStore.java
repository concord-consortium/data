package org.concord.data.stream;

import org.concord.framework.data.stream.DataListener;
import org.concord.framework.data.stream.DataProducer;
import org.concord.framework.data.stream.DataStore;
import org.concord.framework.data.stream.DataStoreEvent;
import org.concord.framework.data.stream.DataStoreListener;
import org.concord.framework.data.stream.DataStreamEvent;

public class LazySyncProducerDataStore 
{
	public interface DataProducerAndDataStoreProvider {
		DataProducer getDataProducer();
		DataStore getDataStore();
	}
	
	DataProducerAndDataStoreProvider provider;
	
	DataStoreListener dataStoreListener = new DataStoreListener(){

		public void dataAdded(DataStoreEvent evt){}

		public void dataChanged(DataStoreEvent evt){}

		public void dataChannelDescChanged(DataStoreEvent evt)
		{
			// TODO we should verify that this is coming from
			// the producer, if the description is coming
			// from somewhere else then it isn't clear what
			// should happen to the producer.
		}

		public void dataRemoved(DataStoreEvent evt)
		{
			DataStore dataStore = evt.getSource();
			DataProducer dataProducer = provider.getDataProducer();

			if(dataProducer != null && dataStore.getTotalNumSamples() == 0){
				if(dataStore instanceof ProducerDataStore){
					ProducerDataStore pDataStore = (ProducerDataStore) dataStore;
					pDataStore.setDataProducer(dataProducer);
				}
			}
		}
	};
	
	DataListener dataProducerListener = new DataListener(){
		boolean started = false;
		
		public void dataReceived(DataStreamEvent dataEvent)
        {
			if(started){
				return;
			}

			started = true;
			// Check if this dataProducer has already been added to the 
			// datastore, if not then add it.
			// This will modify the list of dataListeners in the dataStore
			// while it is iterating over it.  It seems like this will
			// work ok though
			// This will happen whenever data arrives so we should 
			// remove ourselves from the list but we can't safely do that
			// while in the method.  So we have the boolean above instead.
			DataProducer modelDataProducer = provider.getDataProducer();
			DataStore dataStore = provider.getDataStore();
			
			if(dataStore instanceof ProducerDataStore){
				ProducerDataStore pDataStore = (ProducerDataStore) dataStore;
				DataProducer oldDataProducer = pDataStore.getDataProducer();
				if(oldDataProducer != modelDataProducer){
					pDataStore.setDataProducer(modelDataProducer);
				}
			}
        }			

		public void dataStreamEvent(DataStreamEvent dataEvent){}
		
	};

	public LazySyncProducerDataStore(DataProducerAndDataStoreProvider provider)
	{
		this.provider = provider;
	}
	
	public void init()
	{
		DataProducer producer = provider.getDataProducer();
		DataStore dataStore = provider.getDataStore();

        // we should not set the producer onto the datastore
        // if there is data in the data store, this could possibly
        // mess up the data that is in the data store, because the data
        // description of the data in the data store might be different
        // from the data description in the producer.
        // instead we need to listen to the dataStore and dataproducer
        // and when the datastore is cleared, or the producer is started
        // then we set the producer on the datastore.
        // cleared, or the start 
		if (producer != null){
			if(dataStore.getTotalNumSamples() == 0){
				if(dataStore instanceof ProducerDataStore){
					ProducerDataStore pDataStore = (ProducerDataStore) dataStore;
					pDataStore.setDataProducer(producer);
				}
			} 
			
			// listen to the dataStore so if the data is cleared at some
			// point then we will reset the producer 
			// we should be careful not to add a listener twice			
			dataStore.addDataStoreListener(dataStoreListener);

			producer.addDataListener(dataProducerListener);
		}
	}
	
	public void dispose()
	{
		DataProducer producer = provider.getDataProducer();
		DataStore dataStore = provider.getDataStore();

		if(dataStore != null){
			dataStore.removeDataStoreListener(dataStoreListener);
		}

		if(producer != null){
			producer.removeDataListener(dataProducerListener);
		}

	}
}
