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
			(OTControllerRegistry) otrunk.getService(OTControllerRegistry.class);

		registry.registerControllerClass(OTWaveGeneratorController.class);
		registry.registerControllerClass(OTPropertyDataProducerController.class);
		registry.registerControllerClass(OTPropertyDataProducerController.class);
		registry.registerControllerClass(OTLinearProducerFilterController.class);
		registry.registerControllerClass(OTIntegratingProducerFilterController.class);
		registry.registerControllerClass(OTDataStoreController.class);
	}

}
