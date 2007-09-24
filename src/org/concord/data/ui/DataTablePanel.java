/*
 *  Copyright (C) 2004  The Concord Consortium, Inc.,
 *  10 Concord Crossing, Concord, MA 01742
 *
 *  Web Site: http://www.concord.org
 *  Email: info@concord.org
 *
 *  This library is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public
 *  License as published by the Free Software Foundation; either
 *  version 2.1 of the License, or (at your option) any later version.
 *
 *  This library is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *  Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public
 *  License along with this library; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 * END LICENSE */

/*
 * Last modification information:
 * $Revision: 1.15 $
 * $Date: 2007-09-24 16:56:38 $
 * $Author: scytacki $
 *
 * Licence Information
 * Copyright 2004 The Concord Consortium 
*/
package org.concord.data.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.StringReader;

import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableCellRenderer;


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
	implements TableModelListener, MouseListener, ActionListener
{
	/**
	 * Not intended to be serialized, added to remove compile warning.
	 */
	private static final long serialVersionUID = 1L;

	protected JTable table;
	protected DataTableModel tableModel;
	protected JScrollPane scrollPane;
	private static final String IMPORT_FROM_CLIPBOARD = "Import from Clipboard";
	/**
	 * 
	 */
	public DataTablePanel()
	{
		super();
	
		setLayout(new BorderLayout());
		
		tableModel = new DataTableModel();
		table = new JTable(tableModel);
	
		scrollPane = new JScrollPane(table);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		
		add(scrollPane, BorderLayout.CENTER);
		
		tableModel.addTableModelListener(this);
		
		table.addMouseListener(this);
		scrollPane.getViewport().addMouseListener(this);
		setPreferredSize(new Dimension(200, 150));
		
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
	
	public void setRenderer(TableCellRenderer cellRenderer)
	{
		table.setDefaultRenderer(Object.class, cellRenderer);
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


	/**
	 * @see javax.swing.event.TableModelListener#tableChanged(javax.swing.event.TableModelEvent)
	 */
	public void tableChanged(TableModelEvent e)
	{
		// Scroll down automatically
		// This has to be invoked later so the table component before we try to scroll it.
		SwingUtilities.invokeLater(new Runnable(){
			public void run() {
				int newRow = table.getRowCount()-1;
				Rectangle cellRect = table.getCellRect(newRow, 0, true);
				table.scrollRectToVisible(cellRect);					
			}
		});
	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
	 */
	public void mouseClicked(MouseEvent e)
	{
		// TODO Auto-generated method stub
		
	}


	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
	 */
	public void mouseEntered(MouseEvent e)
	{
		// TODO Auto-generated method stub
		
	}


	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
	 */
	public void mouseExited(MouseEvent e)
	{
		// TODO Auto-generated method stub
		
	}


	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
	 */
	public void mousePressed(MouseEvent e)
	{
		// isPopupTrigger happens on moused pressed for osx and linux
		evaluatePopup(e);		
	}


	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
	 */
	public void mouseReleased(MouseEvent e)
	{
		// isPopupTrigger happens on moused released for windows
		evaluatePopup(e);		
	}

	private void evaluatePopup(MouseEvent e)
	{
		if (e.isPopupTrigger()){
			JPopupMenu popup = new JPopupMenu();
			JMenuItem menuItem = new JMenuItem("Copy");			
		    menuItem.addActionListener(this);
		    popup.add(menuItem);
			
		    menuItem = new JMenuItem(IMPORT_FROM_CLIPBOARD);			
		    menuItem.addActionListener(this);
		    popup.add(menuItem);
		    
		    popup.show(this, e.getX() + 5, e.getY() + 20);			
		}
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e)
	{
		String strVal="";
		
		if(e.getActionCommand().equals("Copy")) {
		
			ByteArrayOutputStream outS = new ByteArrayOutputStream();
			tableModel.printData(new PrintStream(outS), table.getSelectedRows(), false);
			strVal = outS.toString();
			
			try{
				Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
				
				clipboard.setContents(new StringSelection(strVal), null);
			}
			catch(Exception ex){}
		} else if (e.getActionCommand().equals(IMPORT_FROM_CLIPBOARD)){
			try {
				Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();

				Transferable contents = clipboard.getContents(null);
				final String data = (String)contents.getTransferData(DataFlavor.stringFlavor);

				Thread importer = new Thread(){
					public void run()
					{
						StringReader dataReader = new StringReader(data);
						
						tableModel.loadData(dataReader);						
					}
				};
				importer.start();
			} catch(Exception ex){
				ex.printStackTrace();
			}			
		}
	}
}
