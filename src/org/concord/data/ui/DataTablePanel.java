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
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.StringReader;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.CellEditor;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.text.JTextComponent;

/**
 * DataTablePanel Class name and description
 * 
 * Date created: Aug 24, 2004
 * 
 * @author imoncada
 *         <p>
 * 
 */
/**
 * @author npaessel
 *
 */
public class DataTablePanel extends JPanel implements TableModelListener,
		MouseListener, ActionListener, FocusListener {
	/**
	 * Not intended to be serialized, added to remove compile warning.
	 */
	private static final long serialVersionUID = 1L;

	protected SelectAllJTable table;
	protected DataTableModel tableModel;
	protected JScrollPane scrollPane;
	protected int visibleRows;
	private static final String IMPORT_FROM_CLIPBOARD = "Import from Clipboard";

	/**
	 * 
	 */
	public DataTablePanel(int _visibleRows) {
		super();
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		this.visibleRows = _visibleRows;
		
		tableModel = new DataTableModel();
		table = new SelectAllJTable(tableModel);
		table.setRowSelectionAllowed(false);
		table.setGridColor(Color.GRAY);
		
		scrollPane = new JScrollPane(table);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);		
		
		add(scrollPane);
		
		tableModel.addTableModelListener(this);
		table.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);		// save last cell on loss of focus

		table.addMouseListener(this);
		table.addFocusListener(this);
		scrollPane.getViewport().addMouseListener(this);
	}
	
	public DataTablePanel() {
		this(9);
	}
	
	public void setTableWidth(int pixel_width) {
		scrollPane.setPreferredSize(new Dimension(pixel_width, getHeightNeeded()));
		setMaximumSize(new Dimension(pixel_width, 500));
	}

	public int getHeightNeeded(){
		return (this.visibleRows * 17) + 20;
	}

	/**
	 * @return Returns the tableModel.
	 */
	public DataTableModel getTableModel() {
		return tableModel;
	}

	/**
	 * @param tableModel
	 * The tableModel to set.
	 */
	public void setTableModel(DataTableModel tableModel) {
		this.tableModel = tableModel;
	}

	public void setRenderer(TableCellRenderer cellRenderer) {
		table.setDefaultRenderer(Object.class, cellRenderer);
	}
	
	public void setHeaderRenderer(TableCellRenderer cellRenderer) {
		for (int i = 0; i < table.getColumnCount(); i++) {
			table.getColumnModel().getColumn(i).setHeaderRenderer(cellRenderer);
		}
	}
	
	public void useDefaultHeaderRenderer(){
		TextAreaRenderer cellRenderer = new TextAreaRenderer();
		setHeaderRenderer(cellRenderer);
	}

	/**
	 * @return Returns the table.
	 */
	public JTable getTable() {
		return table;
	}

	/**
	 * @return Returns the scrollPane.
	 */
	public JScrollPane getScrollPane() {
		return scrollPane;
	}

	/**
	 * @param scrollPane
	 * The scrollPane to set.
	 */
	public void setScrollPane(JScrollPane scrollPane) {
		this.scrollPane = scrollPane;
	}

	/**
	 * @see javax.swing.event.TableModelListener#tableChanged(javax.swing.event.TableModelEvent)
	 */
	public void tableChanged(TableModelEvent e) {
		// Scroll down to currently selected row.
		// FIXME: This will not support auto-scrolling when data is being fed in automatically.
		// However, for the time being it is more important that it does not keep scrolling to the
		// bottom when a user is entering data.
		final int changedRow = e.getLastRow();
		DataTableModel model = (DataTableModel) e.getSource();
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				int newRow = table.getSelectedRow() - 1;
				Rectangle cellRect = table.getCellRect(newRow, 0, true);
				table.scrollRectToVisible(cellRect);
			}
		});
	}

	/*
	 * Unimplemented MouseListener event handlers
	 *
	 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
	 */
	public void mouseClicked(MouseEvent e) { }
	public void mouseEntered(MouseEvent e) { }
	public void mouseExited(MouseEvent e)  { }
	public void mousePressed(MouseEvent e) { }
	public void mouseReleased(MouseEvent e){ }
	
	private void evaluatePopup(MouseEvent e) {
		if (e.isPopupTrigger()) {
			JPopupMenu popup = new JPopupMenu();
			JMenuItem menuItem = new JMenuItem("Copy");
			menuItem.addActionListener(this);
			popup.add(menuItem);

			menuItem = new JMenuItem(IMPORT_FROM_CLIPBOARD);
			menuItem.addActionListener(this);
			popup.add(menuItem);

			popup.show(table, e.getX() + 5, e.getY() + 20);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		String strVal = "";

		if (e.getActionCommand().equals("Copy")) {
			ByteArrayOutputStream outS = new ByteArrayOutputStream();
			tableModel.printData(new PrintStream(outS),
					table.getSelectedRows(), false);
			strVal = outS.toString();
			try {
				Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
				clipboard.setContents(new StringSelection(strVal), null);
			} catch (Exception ex) {
			}
		} else if (e.getActionCommand().equals(IMPORT_FROM_CLIPBOARD)) {
			try {
				Clipboard clipboard = Toolkit.getDefaultToolkit()
						.getSystemClipboard();

				Transferable contents = clipboard.getContents(null);
				final String data = (String) contents
						.getTransferData(DataFlavor.stringFlavor);

				Thread importer = new Thread() {
					public void run() {
						StringReader dataReader = new StringReader(data);
						tableModel.loadData(dataReader);
					}
				};
				importer.start();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}
	

	/**
	 * Custom renderer class
	 *
	 */
	class TextAreaRenderer extends JTextArea implements TableCellRenderer {
	  private final DefaultTableCellRenderer adaptee = new DefaultTableCellRenderer();
	  /** map from table to map of rows to map of column heights */
	  private final Map cellSizes = new HashMap();
	  public TextAreaRenderer() {
	    setLineWrap(true);
	    setWrapStyleWord(true);
	  }

	  public Component getTableCellRendererComponent(
			  JTable table, 
			  Object obj, 
			  boolean isSelected,
			  boolean hasFocus, 
			  int row, 
			  int column) {
	    // set the colors, etc. using the standard for that platform
	    adaptee.getTableCellRendererComponent(table, obj,isSelected, hasFocus, row, column);
	    setForeground(adaptee.getForeground());
	    setBackground(new Color(240,240,240));
	    setBorder(BorderFactory.createLineBorder(new Color(200,200,240)));
	    setFont(adaptee.getFont());
	    setText(adaptee.getText());

	    // This line was very important to get it working with JDK1.4
	    TableColumnModel columnModel = table.getColumnModel();
	    setSize(columnModel.getColumn(column).getWidth(), 100000);
	    int height_wanted = (int) getPreferredSize().getHeight();
	    addSize(table, row, column, height_wanted);
	    height_wanted = findTotalMaximumRowSize(table, row);
	    if (height_wanted != table.getRowHeight(row) && height_wanted >= 1) {
	      table.setRowHeight(row, height_wanted);
	    }
	    return this;
	  }

	  private void addSize(JTable table, int row, int column,int height) {
	    Map rows = (Map) cellSizes.get(table);
	    if (rows == null) {
	      cellSizes.put(table, rows = new HashMap());
	    }
	    Map rowheights = (Map) rows.get(new Integer(row));
	    if (rowheights == null) {
	      rows.put(new Integer(row), rowheights = new HashMap());
	    }
	    rowheights.put(new Integer(column), new Integer(height));
	  }

	  /**
	   * Look through all columns and get the renderer.  If it is
	   * also a TextAreaRenderer, we look at the maximum height in
	   * its hash table for this row.
	   */
	  private int findTotalMaximumRowSize(JTable table, int row) {
	    int maximum_height = 0;
	    Enumeration columns = table.getColumnModel().getColumns();
	    while (columns.hasMoreElements()) {
	      TableColumn tc = (TableColumn) columns.nextElement();
	      TableCellRenderer cellRenderer = tc.getCellRenderer();
	      if (cellRenderer instanceof TextAreaRenderer) {
	        TextAreaRenderer tar = (TextAreaRenderer) cellRenderer;
	        maximum_height = Math.max(maximum_height,
	            tar.findMaximumRowSize(table, row));
	      }
	    }
	    return maximum_height;
	  }

	  private int findMaximumRowSize(JTable table, int row) {
	    Map rows = (Map) cellSizes.get(table);
	    if (rows == null) return 0;
	    Map rowheights = (Map) rows.get(new Integer(row));
	    if (rowheights == null) return 0;
	    int maximum_height = 0;
	    for (Iterator it = rowheights.entrySet().iterator();
	         it.hasNext();) {
	      Map.Entry entry = (Map.Entry) it.next();
	      int cellHeight = ((Integer) entry.getValue()).intValue();
	      maximum_height = Math.max(maximum_height, cellHeight);
	    }
	    return maximum_height;
	  }
	  	  
	}

	public void setCellRenderer(Class c, TableCellRenderer r) {
		table.setDefaultRenderer(c,r);
	}
	
	public void focusGained(FocusEvent arg0) { }
	public void focusLost(FocusEvent e)
    {
	    if (e.isTemporary())
	    	return;
	    
	    CellEditor editor = table.getCellEditor();
	    // check to see if we're the editor. If not, we're only acting as the renderer
	    if (editor != null && editor == this){
	    	editor.stopCellEditing();
	    }
    }
	
	
	/**
	 * @author npaessel
	 * A JTable which akss text component editors
	 * to select all, when entering a new cell.
	 * 
	 * @see JTable
	 * 
	 */
	private class SelectAllJTable extends JTable {
		public SelectAllJTable(DataTableModel tableModel) {
			super(tableModel);
		}
		@Override
		// Select full text of 
		public Component prepareEditor(TableCellEditor editor, int row, int column) {
		    Component c = super.prepareEditor(editor, row, column);
		    if (c instanceof JTextComponent) {
		        ((JTextComponent) c).selectAll();
		    } 
		    return c;
		}
	}
}
