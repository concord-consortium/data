package org.concord.data.state;


import java.util.Random;

import org.concord.framework.data.stream.DefaultMultipleDataProducer;
import org.concord.framework.otrunk.OTChangeEvent;
import org.concord.framework.otrunk.OTChangeListener;

/*
 * It addes values whenever the "step" property of the ot object changes.
 * N random values are generated each time that adds up to this.sum,
 * which equals the number of channels.
 * 
 * i.e., For N=3 and sum=1.0, 
 *       n1 + n2 + n3 = 1.0
 */
public class AlphaDataProducer extends DefaultMultipleDataProducer
	implements OTChangeListener
{
	private static Random rand = new Random();
	private float sum = 0;
	
	public AlphaDataProducer(float dt, int size) {
		super(dt, size);
	}

	public void setSum(float sum) {
		this.sum = sum;
	}

	public void stateChanged(OTChangeEvent e) {
		if (e.getProperty().equals("step")) {
			float[] vals = getValues();
			for (int i = 0; i < vals.length; ++i) {
				System.out.print(vals[i] + " ");
			}
			System.out.println();
			addValues(vals);
		}
	}
	
	private float[] getValues() {
		int numChannels = getNumChannels();
		float tempSum = 0.0f;
		float[] vals = new float[numChannels];
		for (int i = 0; i < numChannels; ++i) {
			vals[i] = rand.nextFloat();
			tempSum += vals[i];
		}
		for (int i = 0; i < numChannels; ++i) {
			vals[i] = vals[i] * sum / tempSum;
		}
		tempSum = 0.0f;
		for (int i = 0; i < numChannels - 1; ++i) {
			tempSum += vals[i];
		}
		vals[numChannels - 1] = sum - tempSum;
		//System.out.println("Sum=" + (tempSum + vals[numChannels-1]));
		return vals;
	}
}
