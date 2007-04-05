/*
 * Last modification information:
 * $Revision: 1.1 $
 * $Date: 2007-04-05 02:58:52 $
 * $Author: imoncada $
 *
 * Licence Information
 * Copyright 2004 The Concord Consortium 
*/
package org.concord.data.state;

import org.concord.framework.otrunk.OTObjectInterface;


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
}
