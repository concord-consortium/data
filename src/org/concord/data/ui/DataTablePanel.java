/*
 * Last modification information:
 * $Revision: 1.2 $
 * $Date: 2004-08-26 20:43:28 $
 * $Author: imoncada $
 *
 * Licence Information
 * Copyright 2004 The Concord Consortium 
*/
package org.concord.data.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;


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
	implements TableModelListener, ComponentListener
{
	protected JTable table;
	protected DataTableModel tableModel;
	protected JScrollPane scrollPane;
	/**
	 * 
	 */
	public DataTablePanel()
	{
		super();
		
		tableModel = new DataTableModel();
		table = new JTable(tableModel);
		//table.setBackground(Color.pink);
		scrollPane = new JScrollPane(table);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		
		add(scrollPane);
				
		tableModel.addTableModelListener(this);
		
		addComponentListener(this);
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
	/**
	 * @return Returns the scrollPane.
	 */
	public JScrollPane getScrollPane()
	{
		return scrollPane;
	}
	/**
	 * @param scrollPane The scrollPane to set.
	 */
	public void setScrollPane(JScrollPane scrollPane)
	{
		this.scrollPane = scrollPane;
	}


	/* (non-Javadoc)
	 * @see javax.swing.event.TableModelListener#tableChanged(javax.swing.event.TableModelEvent)
	 */
	public void tableChanged(TableModelEvent e)
	{
		//Scroll down automatically
		table.scrollRectToVisible(table.getCellRect(table.getRowCount()-1, -1, true));	
	}


	/* (non-Javadoc)
	 * @see java.awt.event.ComponentListener#componentHidden(java.awt.event.ComponentEvent)
	 */
	public void componentHidden(ComponentEvent e)
	{
		// TODO Auto-generated method stub
		
	}


	/* (non-Javadoc)
	 * @see java.awt.event.ComponentListener#componentMoved(java.awt.event.ComponentEvent)
	 */
	public void componentMoved(ComponentEvent e)
	{
		// TODO Auto-generated method stub
		
	}


	/* (non-Javadoc)
	 * @see java.awt.event.ComponentListener#componentResized(java.awt.event.ComponentEvent)
	 */
	public void componentResized(ComponentEvent e)
	{
		table.setPreferredScrollableViewportSize(new Dimension(getSize().width - 20,getSize().height - 80));
	}


	/* (non-Javadoc)
	 * @see java.awt.event.ComponentListener#componentShown(java.awt.event.ComponentEvent)
	 */
	public void componentShown(ComponentEvent e)
	{
		// TODO Auto-generated method stub
		
	}
}
