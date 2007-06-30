/*
 * Last modification information:
 * $Revision: 1.2 $
 * $Date: 2007-06-30 03:59:21 $
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
}
