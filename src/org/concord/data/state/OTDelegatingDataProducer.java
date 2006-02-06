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
