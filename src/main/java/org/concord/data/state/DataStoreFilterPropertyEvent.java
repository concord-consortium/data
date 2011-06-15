/*
 * Last modification information:
 * $Revision: 1.1 $
 * $Date: 2007-06-21 18:01:34 $
 * $Author: imoncada $
 *
 * Licence Information
 * Copyright 2007 The Concord Consortium 
*/
package org.concord.data.state;

import java.util.EventObject;


/**
 * DataFilterPropertyEvent
 * Class name and description
 *
 * Date created: May 21, 2007
 *
 * @author Ingrid Moncada<p>
 *
 */
@SuppressWarnings("serial")
public class DataStoreFilterPropertyEvent extends EventObject
{
	public static final int PROPERTY_ADDED = 0;
	public static final int PROPERTY_CHANGED = 0;

	protected int type;
	
	protected String propertyName;
	
	/**
	 * @param source
	 */
	public DataStoreFilterPropertyEvent(Object source, String propertyName)
	{
		this(source, propertyName, PROPERTY_CHANGED);
	}

	/**
	 * @param source
	 */
	public DataStoreFilterPropertyEvent(Object source, String propertyName, int type)
	{
		super(source);
		this.propertyName = propertyName;
		this.type = type;
	}
	
	public String toString()
	{
		return "\"" + propertyName + "\" changed. " + super.toString();
	}
}
