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
 * $Revision: 1.1 $
 * $Date: 2006-09-29 21:06:15 $
 * $Author: scytacki $
 *
 * Licence Information
 * Copyright 2004 The Concord Consortium 
*/
package org.concord.data.stream;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

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
public class PropertyDataProducer extends TimerDataProducer
{
	Object target;
	Method propertyMethod;
	
	public void setProperty(Object target, String propertyName)
	{
		this.target = target;
		Class targetClass = target.getClass();
		try {
			String methodName = "get" + 
				propertyName.substring(0,1).toUpperCase() + 
				propertyName.substring(1);
			propertyMethod = targetClass.getMethod(methodName, null);
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
	/**
	 * @see org.concord.data.stream.TimerDataProducer#getFunction(float)
	 */
	protected float getValue(float t)
	{
		try {
			if(target == null){
				System.err.println("property data producer requires a target object");
			}
			Object result = propertyMethod.invoke(target, null);
			if(result instanceof Number){
				return ((Number)result).floatValue();
			} 
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return (float)(Math.sin(t));
	}
	
	public Object getCopy() {
		PropertyDataProducer producer = new PropertyDataProducer();
		producer.dataDesc = (DataStreamDescription)this.dataDesc.getCopy();
		
		producer.dataEvent = (DataStreamEvent)this.dataEvent.clone(new DataStreamEvent());
		producer.values = this.values;
		producer.setTimeScale(this.getTimeScale());
		
		
		return producer;
	}
}
