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

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;


/**
 * DataTablePanel
 * Class name and description
 *
 * Date created: Aug 24, 2004
 *
 * @author imoncada<p>
 *
 */
public class DataTablePanel extends JPanel
{
	protected JTable table;
	protected DataTableModel tableModel;
	
	/**
	 * 
	 */
	public DataTablePanel()
	{
		super();
		
		tableModel = new DataTableModel();
		table = new JTable(tableModel);
		table.setBackground(Color.pink);
		JScrollPane scrollPane = new JScrollPane(table);
		
		add(scrollPane);
	}

	
	/**
	 * @return Returns the tableModel.
	 */
	public DataTableModel getTableModel()
	{
		return tableModel;
	}
	
	/**
	 * @param tableModel The tableModel to set.
	 */
	public void setTableModel(DataTableModel tableModel)
	{
		this.tableModel = tableModel;
	}
	
	/**
	 * @return Returns the table.
	 */
	public JTable getTable()
	{
		return table;
	}
}
