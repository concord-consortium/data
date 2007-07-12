/**
 * 
 */
package org.concord.data.state;

import java.util.ArrayList;
import java.util.List;

import org.concord.framework.otrunk.OTControllerRegistry;
import org.concord.framework.otrunk.OTPackage;
import org.concord.framework.otrunk.OTrunk;

/**
 * @author scott
 *
 */
public class OTDataPackage
    implements OTPackage
{

	/* (non-Javadoc)
	 * @see org.concord.framework.otrunk.OTPackage#initialize(org.concord.framework.otrunk.OTrunk)
	 */
	public void initialize(OTrunk otrunk)
	{
		OTControllerRegistry registry = 
			(OTControllerRegistry) otrunk.getService(OTControllerRegistry.class);

		registry.registerControllerClass(OTWaveGeneratorController.class);
		registry.registerControllerClass(OTPropertyDataProducerController.class);
		registry.registerControllerClass(OTPropertyDataProducerController.class);
		registry.registerControllerClass(OTLinearProducerFilterController.class);
		registry.registerControllerClass(OTIntegratingProducerFilterController.class);
		registry.registerControllerClass(OTDataStoreController.class);
	}
	
	public List getOTClasses() 
	{
		ArrayList list = new ArrayList();

		list.add(OTDataChannelDescription.class);
		list.add(OTDataField.class);
		list.add(OTDataFlowControl.class);
		list.add(OTDataProducer.class);
		list.add(OTDataProducerFilter.class);
		list.add(OTDataStore.class);
		list.add(OTDataTable.class);
		list.add(OTIntegratingProducerFilter.class);
		list.add(OTLinearProducerFilter.class);
		list.add(OTPropertyDataProducer.class);
		list.add(OTUnitValue.class);
		list.add(OTWaveGenerator.class);
		
		return list;
	}


}
