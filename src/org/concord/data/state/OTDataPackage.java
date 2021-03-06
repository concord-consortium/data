/**
 * 
 */
package org.concord.data.state;

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
			otrunk.getService(OTControllerRegistry.class);

		registry.registerControllerClass(OTWaveGeneratorController.class);
		registry.registerControllerClass(OTMultiWaveGeneratorController.class);
		registry.registerControllerClass(OTPropertyDataProducerController.class);
		registry.registerControllerClass(OTPropertyDataProducerController.class);
		registry.registerControllerClass(OTLinearProducerFilterController.class);
		registry.registerControllerClass(OTIntegratingProducerFilterController.class);
		registry.registerControllerClass(OTDifferentiatingProducerFilterController.class);
		registry.registerControllerClass(OTHoltsMethodSmoothingController.class);
		registry.registerControllerClass(OTDataStoreController.class);
		registry.registerControllerClass(OTTimerDataStoreDataProducerController.class);
		registry.registerControllerClass(OTFloatDataReceiverController.class);
		registry.registerControllerClass(OTTimeLimitDataProducerFilterController.class);
		registry.registerControllerClass(OTAlphaDataProducerController.class);
		registry.registerControllerClass(OTDataChannelDescriptionController.class);
		registry.registerControllerClass(OTSingleYPerXProducerFilterController.class);
		registry.registerControllerClass(OTDataChannelProducerFilterController.class);
	}
	
	public Class<?> [] getOTClasses() 
	{
		return new Class<?> [] {
				OTDataChannelDescription.class,
				OTDataField.class,
				OTDataProducer.class,
				OTDataProducerFilter.class,
				OTDataStore.class,
				OTDataTable.class,
				OTIntegratingProducerFilter.class,
				OTLinearProducerFilter.class,
				OTPropertyDataProducer.class,
				OTUnitValue.class,
				OTWaveGenerator.class,
				OTTimerDataStoreDataProducer.class,
				OTFloatDataReceiver.class,
				OTTimeLimitDataProducerFilter.class,
				OTDataChannelDescription.class,
				OTSingleYPerXProducerFilter.class,
				OTDataChannelProducerFilter.class
		};
	}

	public Class<? extends OTPackage> [] getPackageDependencies() 
	{
		return null;
	}


}
