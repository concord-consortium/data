/*
 * Last modification information:
 * $Revision: 1.1 $
 * $Date: 2004-10-28 19:04:26 $
 * $Author: imoncada $
 *
 * Licence Information
 * Copyright 2004 The Concord Consortium 
*/
package org.concord.data.stream;

import org.concord.framework.data.stream.DefaultDataStore;


/**
 * PointsDataStore
 * Class name and description
 *
 * Date created: Oct 28, 2004
 *
 * @author imoncada<p>
 *
 */
public class PointsDataStore extends DefaultDataStore
{
	public PointsDataStore()
	{
		super();
	}
	
	public void addPoint(float x, float y)
	{
		int sample = getTotalNumSamples();
		setValueAt(sample, 0, new Float(x));
		setValueAt(sample, 1, new Float(y));
	}
}
