package org.concord.data.state;

import java.util.Arrays;

import org.concord.framework.data.stream.DataListener;
import org.concord.framework.data.stream.DataProducer;
import org.concord.framework.data.stream.DataStreamEvent;
import org.concord.framework.otrunk.wrapper.OTFloat;

public class FloatDataReceiver implements DataListener
{
	private DataProducer dataProducer;
	private OTFloat otfloat;
	
	public void setOTFloat(OTFloat otFloat){
		this.otfloat = otFloat;
	}
	
	public void setDataProducer(DataProducer dataProducer){
		this.dataProducer = dataProducer;
		dataProducer.addDataListener(this);
	}

	public void dataReceived(DataStreamEvent dataEvent)
    {
		if (dataEvent.getNumSamples() <= 0)
			return;
		float x = dataEvent.getData()[dataEvent.getNumSamples()-1];
		otfloat.setValue(x);
    }

	public void dataStreamEvent(DataStreamEvent dataEvent)
    {
    }
}
