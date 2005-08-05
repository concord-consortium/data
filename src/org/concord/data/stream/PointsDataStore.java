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
 * $Revision: 1.7 $
 * $Date: 2005-08-05 16:17:19 $
 * $Author: maven $
 *
 * Licence Information
 * Copyright 2004 The Concord Consortium 
*/
package org.concord.data.stream;

import java.util.Vector;

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

	/**
	 * @param channel
	 * @return
	 */
	private String getFloatVectorStr(Vector v)
	{
		String strS = "[";
		for (int i=0; i<v.size(); i++){
			if (i > 0){
				strS = strS + ",";
			}
			Object obj = v.elementAt(i);
			if (obj instanceof Float){
				Float f = (Float)obj;
				strS = strS + f.floatValue();
			}
		}
		strS = strS + "]";
		return strS;
	}
	
}
