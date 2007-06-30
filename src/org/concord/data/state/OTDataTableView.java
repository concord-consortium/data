/*
 * Last modification information:
 * $Revision: 1.3 $
 * $Date: 2007-06-30 03:59:21 $
 * $Author: imoncada $
 *
 * Licence Information
 * Copyright 2004 The Concord Consortium 
*/
package org.concord.data.state;

import java.awt.Color;

import javax.swing.JComponent;

import org.concord.data.ui.DataColumnDescription;
import org.concord.data.ui.DataTableModel;
import org.concord.data.ui.DataTablePanel;
import org.concord.framework.data.stream.DataStore;
import org.concord.framework.otrunk.OTControllerService;
import org.concord.framework.otrunk.OTObject;
import org.concord.framework.otrunk.OTObjectList;
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
    protected OTControllerService controllerService;
	
	/**
	 * @see org.concord.framework.otrunk.view.OTJComponentView#getComponent(org.concord.framework.otrunk.OTObject, boolean)
	 */
	public JComponent getComponent(OTObject otObject, boolean editable)
	{
		otTable = (OTDataTable)otObject;
    	controllerService = otTable.getOTObjectService().createControllerService();

		
		DataTablePanel table = new DataTablePanel();
		
		DataStore dataStore;
		OTDataStore otDataStore;
		otDataStore = otTable.getDataStore();
		dataStore = (DataStore) controllerService.getRealObject(otDataStore);
		table.getTableModel().addDataStore(dataStore);
		
		updateOTColumns(table.getTableModel(), dataStore, otTable.getColumns());
		
		return table;
	}

	/**
	 * @param dataStore
	 * @param columns
	 */
	protected void updateOTColumns(DataTableModel tableModel, DataStore dataStore, OTObjectList columns)
	{
		for (int i = 0; i < columns.size(); i++){
			OTDataColumnDescription otColDesc = (OTDataColumnDescription)columns.get(i); 
			if (otColDesc == null) continue;
			DataColumnDescription colDesc = tableModel.getDataColumn(dataStore, i);
			
			String label = otColDesc.getLabel();
			if (label != null){
				colDesc.setLabel(label);
			}
			if (otColDesc.isResourceSet("color")){
				System.out.println("set color to "+otColDesc.getColor());
				colDesc.setColor(new Color(otColDesc.getColor()));
			}
		}
		tableModel.fireTableStructureChanged();
	}

	/**
	 * @see org.concord.framework.otrunk.view.OTJComponentView#viewClosed()
	 */
	public void viewClosed()
	{
		controllerService.dispose();
	}

}
