/*
 * Last modification information:
 * $Revision: 1.2 $
 * $Date: 2004-10-28 16:36:38 $
 * $Author: imoncada $
 *
 * Licence Information
 * Copyright 2004 The Concord Consortium 
*/
package org.concord.data.stream;

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
}
