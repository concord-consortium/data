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
	
	//List of column descriptions (optional)
	public OTObjectList getColumns();
	
	public boolean getShowLastRowForAddingNew();	
}
