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

import javax.swing.JComponent;

import org.concord.data.ui.DataTablePanel;
import org.concord.framework.data.stream.DataStore;
import org.concord.framework.otrunk.OTObject;
import org.concord.framework.otrunk.view.OTJComponentView;


/**
 * OTDataTableView
 * Class name and description
 *
 * Date created: Apr 4, 2007
 *
 * @author imoncada<p>
 *
 */
public class OTDataTableView
	implements OTJComponentView
{
	protected OTDataTable otTable;
	
	/**
	 * @see org.concord.framework.otrunk.view.OTJComponentView#getComponent(org.concord.framework.otrunk.OTObject, boolean)
	 */
	public JComponent getComponent(OTObject otObject, boolean editable)
	{
		otTable = (OTDataTable)otObject;
		
		DataTablePanel table = new DataTablePanel();
		
		DataStore dataStore;
		dataStore = otTable.getDataStore();
		table.getTableModel().addDataStore(dataStore);
		
		return table;
	}

	/**
	 * @see org.concord.framework.otrunk.view.OTJComponentView#viewClosed()
	 */
	public void viewClosed()
	{
		// TODO Auto-generated method stub

	}

}
