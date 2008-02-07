package org.concord.data.state;

import org.concord.data.Unit;
import org.concord.data.stream.MultiWaveDataProducer;
import org.concord.framework.data.stream.DataChannelDescription;
import org.concord.framework.data.stream.DataStreamDescription;
import org.concord.framework.otrunk.DefaultOTController;
import org.concord.framework.otrunk.OTObjectList;

public class OTMultiWaveGeneratorController extends DefaultOTController 
{
	public static Class[] realObjectClasses = {MultiWaveDataProducer.class};	
	public static Class otObjectClass = OTMultiWaveGenerator.class;

	public void loadRealObject(Object realObject) {
		OTMultiWaveGenerator otWaveGenerator = (OTMultiWaveGenerator) otObject;
		MultiWaveDataProducer waveProducer = (MultiWaveDataProducer) realObject;
		
		OTObjectList otWaves = otWaveGenerator.getWaves();
		
		float [] amplitudes = new float[otWaves.size()];
		float [] wavelengths = new float[otWaves.size()];
		for(int i=0; i<otWaves.size(); i++){
			OTWave otWave = (OTWave) otWaves.get(i);
			amplitudes[i] = otWave.getAmplitude();
			wavelengths[i] = otWave.getWavelength();
		}
		
		waveProducer.configure(amplitudes, wavelengths);

		DataStreamDescription dataDescription = waveProducer.getDataDescription();
        DataChannelDescription chDesc = dataDescription.getDtChannelDescription();
        chDesc.setUnit(Unit.getUnit(Unit.UNIT_CODE_S));
        chDesc.setName("time");

        chDesc = dataDescription.getChannelDescription(0);
        chDesc.setUnit(Unit.getUnit(Unit.UNIT_CODE_METER));
        chDesc.setName("distance");
	}

	public void registerRealObject(Object realObject) {
		// TODO Auto-generated method stub

	}

	public void saveRealObject(Object realObject) {
		// TODO Auto-generated method stub

	}

}
