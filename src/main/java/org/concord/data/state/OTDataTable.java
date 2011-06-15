/*
 * Last modification information:
 * $Revision: 1.3 $
 * $Date: 2007-10-01 16:21:36 $
 * $Author: imoncada $
 *
 * Licence Information
 * Copyright 2004 The Concord Consortium 
*/
package org.concord.data.state;

import org.concord.framework.otrunk.OTObjectInterface;
import org.concord.framework.otrunk.OTObjectList;


/**
 * OTDataTable
 * Class name and description
 *
 * Date created: Apr 4, 2007
 *
 * @author imoncada<p>
 *
 */
public interface OTDataTable
	extends OTObjectInterface
{
	//For now, I will support only one data store (it's easier :p)
	public OTDataStore getDataStore();
	public void setDataStore(OTDataStore dataStore);
	
	//List of column descriptions (optional)
	public OTObjectList getColumns();
	
	public boolean getShowLastRowForAddingNew();	
	
	public int getVisibleRows();
	public void setVisibleRows(int visibleRows);
	public static int DEFAULT_visibleRows = 9;
	
	// decimal precision for display.
	public int getPrecision();
	public void setPresision(int newPrecision);
	public static int DONT_FORMAT = -1;
	public static int DEFAULT_precision = DONT_FORMAT;
	
	// allow authors to specify the width of the table
	public int getWidth();
	public void setWidth(int newWidth);
	public static int FULL_WIDTH = 1200;
	public static int DEFAULT_width = FULL_WIDTH;
	
}
