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
 * $Revision: 1.3 $
 * $Date: 2005-08-05 16:17:19 $
 * $Author: maven $
 *
 * Licence Information
 * Copyright 2004 The Concord Consortium 
*/
package org.concord.data.ui;

import java.awt.Color;

import org.concord.framework.data.stream.DataStore;


/**
 * DataColumnDescription
 * Class name and description
 *
 * Date created: Aug 24, 2004
 *
 * @author imoncada<p>
 *
 */
public class DataColumnDescription
{
	protected DataStore dataStore;
	protected int dataStoreColumn;
	protected boolean visible = true;
	protected Color color;
	protected String label;
	protected boolean locked = false;
	
	/**
	 * 
	 */
	public DataColumnDescription(DataStore dataStore, int dataStoreColumn)
	{
		this.dataStore = dataStore;
		this.dataStoreColumn = dataStoreColumn;
		color = Color.black;
	}

	/**
	 * @return Returns the color.
	 */
	public Color getColor()
	{
		return color;
	}
	
	/**
	 * @param color The color to set.
	 */
	public void setColor(Color color)
	{
		this.color = color;
	}
	
	/**
	 * @return Returns the dataStoreColumn.
	 */
	public int getDataStoreColumn()
	{
		return dataStoreColumn;
	}
	
	/**
	 * @return Returns the visible.
	 */
	public boolean isVisible()
	{
		return visible;
	}
	
	/**
	 * @param visible The visible to set.
	 */
	public void setVisible(boolean visible)
	{
		this.visible = visible;
	}
	
	/**
	 * @return Returns the dataStore.
	 */
	public DataStore getDataStore()
	{
		return dataStore;
	}
	
	/**
	 * @return Returns the label.
	 */
	public String getLabel()
	{
		return label;
	}
	
	/**
	 * @param label The label to set.
	 */
	public void setLabel(String label)
	{
		this.label = label;
	}
	
	/**
	 * @return Whether the student may edit the contents of the column
	 */
	public boolean isLocked(){
		return locked;
	}
	
	/**
	 * 
	 * @param locked Whether the student may edit the contents of the column
	 */
	public void setLocked(boolean locked){
		this.locked = locked;
	}
}
