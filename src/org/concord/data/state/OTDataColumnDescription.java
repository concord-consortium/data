/*
 * Last modification information:
 * $Revision: 1.1 $
 * $Date: 2007-06-30 03:59:20 $
 * $Author: imoncada $
 *
 * Licence Information
 * Copyright 2007 The Concord Consortium 
*/
package org.concord.data.state;

import org.concord.framework.otrunk.OTObjectInterface;


/**
 * OTDataColumnDescription
 * Class name and description
 *
 * Date created: Jun 29, 2007
 *
 * @author Ingrid Moncada<p>
 *
 */
public interface OTDataColumnDescription
	extends OTObjectInterface
{
	public int getColor();
	public void setColor();
	
	public String getLabel();
	public void setLabel(String label);
}
