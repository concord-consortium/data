package org.concord.data.state;

import org.concord.framework.data.stream.DataListener;
import org.concord.framework.data.stream.DataProducer;
import org.concord.framework.data.stream.DataStreamDescription;
import org.concord.framework.otrunk.DefaultOTObject;
import org.concord.framework.otrunk.OTResourceSchema;
import org.concord.framework.util.Copyable;

public abstract class OTDelegatingDataProducer extends DefaultOTObject
implements DataProducer
{
	public OTDelegatingDataProducer(OTResourceSchema resources)
	{
		super(resources);
	}
	
	private DataProducer myProducer;
	
	public void setMyDataProducer(DataProducer dProducer)
	{
		myProducer = dProducer;
	}
	
	public DataProducer getMyDataProducer()
	{
		return myProducer;
	}
	
	/* (non-Javadoc)
	 * @see org.concord.framework.data.stream.DataProducer#addDataListener(org.concord.framework.data.stream.DataListener)
	 */
	public void addDataListener(DataListener listener)
	{
		myProducer.addDataListener(listener);
	}
	
	
	/* (non-Javadoc)
	 * @see org.concord.framework.data.stream.DataProducer#getDataDescription()
	 */
	public DataStreamDescription getDataDescription()
	{
		return myProducer.getDataDescription();
	}
	
	
	/* (non-Javadoc)
	 * @see org.concord.framework.data.stream.DataProducer#removeDataListener(org.concord.framework.data.stream.DataListener)
	 */
	public void removeDataListener(DataListener listener)
	{
		myProducer.removeDataListener(listener);		
	}
	
	/* (non-Javadoc)
	 * @see org.concord.framework.data.DataFlow#reset()
	 */
	public void reset()
	{
		myProducer.reset();
	}
	
	
	/* (non-Javadoc)
	 * @see org.concord.framework.data.DataFlow#start()
	 */
	public void start()
	{
		myProducer.start();
	}
	
	/* (non-Javadoc)
	 * @see org.concord.framework.data.DataFlow#stop()
	 */
	public void stop()
	{
		myProducer.stop();
	}
	
}
