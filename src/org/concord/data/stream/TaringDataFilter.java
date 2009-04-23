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
 * Last modification information:
 * $Revision: 1.3 $
 * $Date: 2006-10-03 21:07:21 $
 * $Author: scytacki $
 *
 * Licence Information
 * Copyright 2004 The Concord Consortium 
*/
package org.concord.data.stream;

import java.util.Vector;

import org.concord.framework.data.stream.DataConsumer;
import org.concord.framework.data.stream.DataListener;
import org.concord.framework.data.stream.DataProducer;
import org.concord.framework.data.stream.DataStreamDescription;
import org.concord.framework.data.stream.DataStreamEvent;

/**
 * TaringDataFilter
 * Class name and description
 *
 * Date created: Jun 2, 2005
 *
 * @author scott<p>
 *
 */
public class TaringDataFilter
    implements DataProducer, DataConsumer
{
    private Vector dataListeners = new Vector();
    private DataProducer source;
    private boolean doingTare = false;
    private DataStreamEvent dataEvent;
    private DataListener dataListener;

    protected float tareOffset = 0;
    protected int tareChannel = 0;
    
    public TaringDataFilter()
    {
        /*
        * Send the data listening to this inner class so
        * it hides that from users of this class.
        */
        this.dataListener = new DataListener() {
            public void dataReceived(DataStreamEvent dataEvent)
            {
                TaringDataFilter.this.dataReceived(dataEvent);
            }  

            public void dataStreamEvent(DataStreamEvent dataEvent)
            {
                TaringDataFilter.this.dataStreamEvent(dataEvent);
            }
        };
    }
    
    /**
     * Do the actual tare.  This will offset the channel that is being
     * tared.  It will probably do this by adding another listener to the 
     * data.  So the tare might take one sample before it takes effect
     *
     */
    public void tare()
    {
    	doingTare = true;
    }
    
    /**
     * set the channel number that will be adjusted by the tare
     * this uses the virtual channel like is used by the DataGraphable
     * class.  So channel 0 means the first channel.  Even if the 
     * DataProducer has a dt which makes the first channel -1.  If the
     * DataProducer doesn't have a dt then the first channel is 0.  
     * 
     * @param channelNumber
     */
    public void setChannel(int channelNumber)
    {
        // set the channel number 
        tareChannel = channelNumber;
    }
    
    protected int getProducerChannel()
    {
        if(source == null) {
            throw new RuntimeException("Null source");
        }
        
        if(source.getDataDescription().getDataType() ==
            DataStreamDescription.DATA_SEQUENCE) {
            // has a dt
            return tareChannel - 1;
        } else {
            return tareChannel;
        }
    }
    
    protected void dataReceived(DataStreamEvent dataEvent)
    {
        int producerChannel = getProducerChannel();
        int numSamples = dataEvent.getNumSamples();
        int offset = dataEvent.getDataDescription().getDataOffset();
        int stride = dataEvent.getDataDescription().getNextSampleOffset();

        if(doingTare) {
            // save this for the calibration
        	float value = dataEvent.data[offset + producerChannel];
        	tareOffset = -value;        	
            doingTare = false;
        } 
        
        // send the event to our listeners
        // however we need to do the actual filter
        // here and adjust the values
        for(int i=0; i<numSamples; i++){
        	float value = dataEvent.data[offset + producerChannel];        	
        	dataEvent.data[offset + producerChannel] = value + tareOffset;
        	offset += stride;

        }
        
        notifyDataReceived(dataEvent);
    }
    
    /* (non-Javadoc)
     * @see org.concord.framework.data.stream.DataListener#dataStreamEvent(org.concord.framework.data.stream.DataStreamEvent)
     */
    protected void dataStreamEvent(DataStreamEvent dataEvent)
    {
        if(doingTare) {
            // save this for the calibration
        } 
        // send the event to our listeners
        notifyDataStreamEvent(dataEvent);
    }
            
    /* (non-Javadoc)
     * @see org.concord.framework.data.stream.DataProducer#addDataListener(org.concord.framework.data.stream.DataListener)
     */
    public void addDataListener(DataListener listener)
    {
        // we need our own list listeners so that we can shield 
        // these listeners when we do the calibration run
        dataListeners.add(listener);

    }

    /* (non-Javadoc)
     * @see org.concord.framework.data.stream.DataProducer#removeDataListener(org.concord.framework.data.stream.DataListener)
     */
    public void removeDataListener(DataListener listener)
    {
        // TODO Auto-generated method stub
        dataListeners.remove(listener);
    }

    /* (non-Javadoc)
     * @see org.concord.framework.data.stream.DataProducer#getDataDescription()
     */
    public DataStreamDescription getDataDescription()
    {
        // TODO Auto-generated method stub
        if(source == null) {
            return null;
        }

        return source.getDataDescription();
    }

    /* (non-Javadoc)
     * @see org.concord.framework.data.stream.DataConsumer#addDataProducer(org.concord.framework.data.stream.DataProducer)
     */
    public void addDataProducer(DataProducer source)
    {
        if(source == null) {
            return;
        }
        
        if(this.source != null) {
            this.source.removeDataListener(dataListener);
        }
        this.source = source;
        this.source.addDataListener(dataListener);
    }

    /* (non-Javadoc)
     * @see org.concord.framework.data.stream.DataConsumer#removeDataProducer(org.concord.framework.data.stream.DataProducer)
     */
    public void removeDataProducer(DataProducer source)
    {
        if(this.source == source) {
            this.source.removeDataListener(dataListener);            
            this.source = null;
        }
    }

    /* (non-Javadoc)
     * @see org.concord.framework.data.DataFlow#reset()
     */
    public void reset()
    {
        if(source != null) {
            source.reset();
        }
    }

    /* (non-Javadoc)
     * @see org.concord.framework.data.DataFlow#stop()
     */
    public void stop()
    {
        if(source != null) {
            source.stop();
        }
    }

    /* (non-Javadoc)
     * @see org.concord.framework.data.DataFlow#start()
     */
    public void start()
    {
        if(source != null) {
            source.start();
        }
    }

	protected void notifyDataReceived(DataStreamEvent sourceEvent)
	{
	    dataEvent = sourceEvent.clone(dataEvent);
	    dataEvent.setSource(this);
		for(int i=0; i<dataListeners.size(); i++) {
			DataListener dataListener = (DataListener)dataListeners.elementAt(i);
			if(dataListener != null)
			{
				dataListener.dataReceived(dataEvent);
			}
		}
	}
	
	protected void notifyDataStreamEvent(DataStreamEvent sourceEvent)
	{
	    dataEvent = sourceEvent.clone(dataEvent);
	    dataEvent.setSource(this);
		for(int i=0; i<dataListeners.size(); i++) {
			DataListener dataListener = (DataListener)dataListeners.elementAt(i);
			if(dataListener != null)
			{
				dataListener.dataStreamEvent(dataEvent);
			}
		}
	}
	
	public boolean isRunning() {
		if(source != null){
			return source.isRunning();
		}
		return false;
	}

	public boolean isInInitialState() {
		if(source != null){
			return source.isInInitialState();
		}
		
		return true;
	}
}
