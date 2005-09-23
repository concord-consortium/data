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
 * $Revision: 1.5 $
 * $Date: 2005-09-23 19:57:29 $
 * $Author: swang $
 *
 * Licence Information
 * Copyright 2004 The Concord Consortium 
*/
package org.concord.data.stream;

import org.concord.framework.data.stream.DataStreamDescription;
import org.concord.framework.data.stream.DataStreamEvent;

/**
 * WaveDataProducer
 * Class name and description
 *
 * Date created: Oct 27, 2004
 *
 * @author scott<p>
 *
 */
public class WaveDataProducer extends TimerDataProducer
{
	/**
	 * @see org.concord.data.stream.TimerDataProducer#getFunction(float)
	 */
	protected float getValue(float t)
	{
		return (float)(Math.sin(t));
	}
	
	public Object getCopy() {
		WaveDataProducer producer = new WaveDataProducer();
		producer.dataDesc = (DataStreamDescription)this.dataDesc.getCopy();
		
		producer.dataEvent = (DataStreamEvent)this.dataEvent.clone(new DataStreamEvent());
		producer.values = this.values;
		producer.setTimeScale(this.getTimeScale());
		
		
		return producer;
	}
}
