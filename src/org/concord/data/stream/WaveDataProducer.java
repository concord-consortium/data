/*
 * Last modification information:
 * $Revision: 1.1 $
 * $Date: 2004-10-27 21:58:22 $
 * $Author: scytacki $
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
public class WaveDataProducer extends DefaultDataProducer
	implements ActionListener
{
	private Timer timer = null;
	float currentTime = 0;
	
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
		addValue((float)(Math.sin(currentTime)));	
		currentTime += getDataDescription().getDt();
	}
}
