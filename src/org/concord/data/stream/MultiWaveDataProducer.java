package org.concord.data.stream;

public class MultiWaveDataProducer extends TimerMultiDataProducer {

	float amplitudes [] = {1};
	float wavelengths [] = {1};

	public MultiWaveDataProducer()
	{
		// the default dt is normally 1 second.  0.1 is better in this case
		getDataDescription().setDt(0.1f);
	}
	
	public void configure(float []amplitudes, float[]wavelengths)
	{
		this.amplitudes = amplitudes;
		this.wavelengths = wavelengths;
		setNumberValues(amplitudes.length);
	}
	
	protected void updateValues(float t, float[] values) {
		for(int i=0; i<amplitudes.length; i++){
			values[i] = (float) (amplitudes[i] * Math.sin(t*2*Math.PI/wavelengths[i]));
		}
	}

}
