package org.concord.data.state;


import org.concord.framework.otrunk.DefaultOTController;


public class OTAlphaDataProducerController extends DefaultOTController
{
	public static Class[] realObjectClasses = { AlphaDataProducer.class };
	public static Class otObjectClass = OTAlphaDataProducer.class;

	public void loadRealObject(Object realObject) {
		OTAlphaDataProducer otdp = (OTAlphaDataProducer) otObject;
		AlphaDataProducer dp = (AlphaDataProducer) realObject;
		
		dp.setNumChannels(otdp.getNumChannels());
		dp.setSum(otdp.getSum());
		otdp.addOTChangeListener(dp);
	}
	
	public Object createRealObject() {
		return new AlphaDataProducer(1.0f, ((OTAlphaDataProducer) otObject).getNumChannels());
	}

	public void registerRealObject(Object realObject) {
	}

	public void saveRealObject(Object realObject) {
	}
}
