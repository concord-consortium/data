

/*
 *  Copyright (C) 2004  The Concord Consortium, Inc.,
 *  10 Concord Crossing, Concord, MA 01741
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
 */
/*
 * Last modification information:
 * $Revision: 1.4 $
 * $Date: 2005-03-10 03:04:27 $
 * $Author: imoncada $
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

	public void addPointOrderFromTo(float x, float y, float otherX)
	{
		if (otherX <= x){
			addPointOrderFrom(x, y, otherX);
		}
		else{
			addPointOrderTo(x, y, otherX);
		}
	}
	
	public void addPointOrderFrom(float x, float y, float startX)
	{
		float value;
		int i;
		int startI;
		int t;
		int ti;
		
		if (x < startX){
			return;
		}
		if (x == startX){
			addPointOrder(x, y);
			return;
		}
		
		i = getIndexValue(x);
		t = getTotalNumSamples();
		startI = getIndexValue(startX);
		value = getXValue(i);
		
		//System.out.println("addPointOrderFrom("+x+" "+i+" "+startX+" "+startI+") in "+getFloatVectorStr((Vector)channelsValues.elementAt(0)));
		
		//Removing all values from startI to i
		ti = Math.min(i, t);
		for (int ii = startI+1; ii < ti; ii++){
			//for (int j=0; j < channelsValues.size(); j++){
			//	Vector channel = (Vector)channelsValues.elementAt(j);
			//	channel.remove(startI+1);
			//}
			//The only problem with doing this is that removeSampleAt fires a removed event
			removeSampleAt(startI+1);
			i--;
		}
		//i = i - (ti - (startI +1));
		
		//System.out.println("after removing, i:"+i+" "+getFloatVectorStr((Vector)channelsValues.elementAt(0)));
		
		addPointOrder(x, y, i, value);
		
	}
	
	public void addPointOrderTo(float x, float y, float endX)
	{
		float value;
		int i;
		int endI;
		int t;
		int ti;
		
		if (x > endX){
			return;
		}
		if (x == endX){
			addPointOrder(x, y);
			return;
		}
		
		i = getIndexValue(x);
		t = getTotalNumSamples();
		endI = getIndexValue(endX);
		value = getXValue(i);
		
		//System.out.println("addPointOrderFrom("+x+" "+i+" "+endX+" "+endI+") in "+getFloatVectorStr((Vector)channelsValues.elementAt(0)));
		
		//Removing all values from i to endI
		ti = Math.min(endI, t);
		for (int ii = i; ii < ti; ii++){
			for (int j=0; j < channelsValues.size(); j++){
				Vector channel = (Vector)channelsValues.elementAt(j);
				channel.remove(i);
			}
		}
		
		//System.out.println("after removing, i:"+i+" "+getFloatVectorStr((Vector)channelsValues.elementAt(0)));
		
		addPointOrder(x, y, i, value);
	}
	
	public void addPointOrder(float x, float y)
	{
		float value;
		int i = getIndexValue(x);
		value = getXValue(i);
		
		addPointOrder(x, y, i, value);
	}
	
	/**
	 * @param x
	 * @param i
	 * @param value
	 */
	private void addPointOrder(float x, float y, int i, float value)
	{
		if (i < getTotalNumSamples()){
			if (value != x){
				insertSampleAt(i);
			}
			for (int j=0; j < getTotalNumChannels(); j++){
				if (j == 0){
					setValueAt(i, j, new Float(x));
					//System.out.println("addPointOrder set "+getFloatVectorStr(channel));
				}
				else if (j == 1){
					setValueAt(i, j, new Float(y));
				}
				else{
					setValueAt(i, j, null);
				}				
			}
			notifyDataChanged();
		}
		else{
			addPoint(x,y);
		}
	}

	/**
	 * @param i
	 */
	private void insertSampleAt(int i)
	{
		for (int j=0; j < channelsValues.size(); j++){
			Vector channel = (Vector)channelsValues.elementAt(j);
			channel.add(i, null);
		}
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

	/**
	 * @param i
	 * @return
	 */
	private float getXValue(int i)
	{
		Object obj = getValueAt(i, 0);
		if (obj instanceof Float){
			return ((Float)obj).floatValue();
		}
		return Float.NaN;
	}

	/**
	 * @param x
	 * @return
	 */
	private int getIndexValue(float x)
	{
		float value;
		Object obj;
		int i;
		for (i = 0; i < getTotalNumSamples(); i++){
			obj = getValueAt(i, 0);
			if (obj instanceof Float){
				value = ((Float)obj).floatValue();
				if (value >= x){
					break;
				}
			}
		}
		
		//System.out.println("get index "+i+" of value "+x+" in "+getFloatVectorStr((Vector)channelsValues.elementAt(0)));
		
		return i;
	}
}
