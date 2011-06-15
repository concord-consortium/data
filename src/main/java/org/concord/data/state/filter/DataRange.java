/*
 * Last modification information:
 * $Revision: 1.1 $
 * $Date: 2007-03-22 05:29:25 $
 * $Author: imoncada $
 *
 * Licence Information
 * Copyright 2007 The Concord Consortium 
*/
package org.concord.data.state.filter;


/**
 * DataRange
 * Class name and description
 *
 * Date created: Mar 19, 2007
 *
 * @author Ingrid Moncada<p>
 *
 */
public class DataRange
{
	protected float minValue;
	protected float maxValue;
	
	public DataRange(float minValue, float maxValue)
	{
		setMinValue(minValue);
		setMaxValue(maxValue);
	}
	
	public float getMaxValue()
	{
		return maxValue;
	}
	
	public void setMaxValue(float maxValue)
	{
		this.maxValue = maxValue;
	}
	
	public float getMinValue()
	{
		return minValue;
	}
	
	public void setMinValue(float minValue)
	{
		this.minValue = minValue;
	}
	
	public boolean isInside(float value)
	{
		if (value <= minValue && value >= maxValue){
			return true;
		}
		return false;
	}
}
