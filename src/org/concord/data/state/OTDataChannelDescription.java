/*
 * Created on Jan 28, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.concord.data.state;

import org.concord.framework.data.DataDimension;
import org.concord.framework.otrunk.OTObject;
import org.concord.framework.otrunk.OTResourceSchema;

/**
 * @author scott
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public interface OTDataChannelDescription extends OTObject
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
