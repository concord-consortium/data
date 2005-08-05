/*
 *  Copyright (C) 2004  The Concord Consortium, Inc.,
 *  10 Concord Crossing, Concord, MA 01742
 *
 *  Web Site: http://www.concord.org
 *  Email: info@concord.org
 *
 *  This library is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public
 *  License as published by the Free Software Foundation; either
 *  version 2.1 of the License, or (at your option) any later version.
 *
 *  This library is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *  Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public
 *  License along with this library; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 * END LICENSE */

/*
 * Created on Jan 14, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.concord.data.stream;

import java.io.PrintStream;

import org.concord.framework.data.stream.DataConsumer;
import org.concord.framework.data.stream.DataListener;
import org.concord.framework.data.stream.DataProducer;
import org.concord.framework.data.stream.DataStreamEvent;

/**
 * @author scytacki
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class PrintingDataConsumer 
	implements DataConsumer, DataListener
{
	PrintStream output= null;

	public PrintingDataConsumer(PrintStream output)
	{
		this.output = output;
	}
	
	/* (non-Javadoc)
	 * @see org.concord.framework.data.stream.DataConsumer#addDataProducer(org.concord.framework.data.stream.DataProducer)
	 */
	public void addDataProducer(DataProducer source) 
	{
		// TODO Auto-generated method stub
		source.addDataListener(this);
	}

	/* (non-Javadoc)
	 * @see org.concord.framework.data.stream.DataConsumer#removeDataProducer(org.concord.framework.data.stream.DataProducer)
	 */
	public void removeDataProducer(DataProducer source) {
		// TODO Auto-generated method stub
		source.removeDataListener(this);
	}

	/* (non-Javadoc)
	 * @see org.concord.framework.data.stream.DataListener#dataReceived(org.concord.framework.data.stream.DataStreamEvent)
	 */
	public void dataReceived(DataStreamEvent dataEvent) {
		DataProducer dataProducer = (DataProducer)dataEvent.getSource();

		int numberOfSamples = dataEvent.getNumSamples();
		int sampleOffset = dataProducer.getDataDescription().getDataOffset();
		int nextSampleOffset = dataProducer.getDataDescription().getNextSampleOffset();
		int numChannelsPerSample = dataProducer.getDataDescription().getChannelsPerSample();
		
		for(int i=0; i<numberOfSamples; i++) {
			for(int j=0; j<numChannelsPerSample; j++) {
				// should take precision into account here
				output.print("" + dataEvent.data[sampleOffset + i*nextSampleOffset + j] + "\t");
			}
			output.println();
		}
	}
	
	/* (non-Javadoc)
	 * @see org.concord.framework.data.stream.DataListener#dataStreamEvent(org.concord.framework.data.stream.DataStreamEvent)
	 */
	public void dataStreamEvent(DataStreamEvent dataEvent) 
	{
		// TODO maybe print out these events.
	}
}
