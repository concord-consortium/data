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
 * $Date: 2005-08-05 16:17:19 $
 * $Author: maven $
 *
 * Licence Information
 * Copyright 2004 The Concord Consortium 
*/
package org.concord.data.stream;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

import org.concord.framework.data.stream.DefaultDataProducer;


/**
 * WaveDataProducer
 * Class name and description
 *
 * Date created: Oct 27, 2004
 *
 * @author scott<p>
 *
 */
public abstract class TimerDataProducer extends DefaultDataProducer
	implements ActionListener
{
	private Timer timer = null;
	private float currentTime = 0;
	
	public void reset()
	{
		currentTime = 0;
		stop();
	}
	
	public void stop()
	{
		timer.stop();
	}
	
	public void start()
	{
		if(timer == null) {
			float dt = getDataDescription().getDt();
			int tt = (int)(1000f * dt);				
			timer = new Timer(tt, this);				
		}
		timer.start();
	}
	
	public void actionPerformed(ActionEvent e)
	{
		
		//Since this is just an example: random values from -1 to 1 
		addValue(getValue(currentTime));	
		currentTime += getDataDescription().getDt();
	}
	
	protected abstract float getValue(float t); 
}
