/*
 * Last modification information:
 * $Revision: 1.1 $
 * $Date: 2004-08-24 23:15:07 $
 * $Author: imoncada $
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
}
