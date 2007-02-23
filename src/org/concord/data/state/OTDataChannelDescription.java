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
 * Created on Jan 28, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.concord.data.state;

import org.concord.framework.otrunk.OTObjectInterface;

/**
 * @author scott
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public interface OTDataChannelDescription extends OTObjectInterface
{
	public String getUnit();
	public void setUnit(String unit);
	
	// power of 10 precision
	// default should be max int so we can tell if this is set or not
	public static int DEFAULT_precision = Integer.MAX_VALUE;
	public int getPrecision();
	public void setPrecision(int precision);
	
	public static float DEFAULT_absoluteMin = Float.NaN;
	public float getAbsoluteMin();
	public void setAbsoluteMin(float min);
	
	public static float DEFAULT_absoluteMax = Float.NaN;
	public float getAbsoluteMax();
	public void setAbsoluteMax(float max);
	
	// The recommended min and max of the data or NaN if
	// not available
	public static float DEFAULT_recommendMin = Float.NaN;
	public float getRecommendMin();
	public void setRecommendMin(float min);
	
	public static float DEFAULT_recommendMax = Float.NaN;
	public float getRecommendMax();
	public void setRecommendMax(float min);	
}
