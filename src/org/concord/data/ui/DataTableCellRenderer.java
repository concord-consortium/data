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

/**
 * Last modification information:
 * $Revision: 1.6 $
 * $Date: 2007-09-06 16:07:09 $
 * $Author: scytacki $
 *
 * Copyright 2004 The Concord Consortium
*/
package org.concord.data.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JTable;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * A cell renderer that uses a color model
 *
 * @author Ingrid Moncada<p>
 *
*/

public class DataTableCellRenderer extends DefaultTableCellRenderer
{
	/**
	 * Not intended to be serialized, added to remove compile warning.
	 */
	private static final long serialVersionUID = 1L;

	protected TableCellColorModel tableCellColorModel;
	
	/*
	 * Default constructor
	 */
	public DataTableCellRenderer()
	{
		super();
	}
	
	/*
	 * Creates a table cell renderer based on the specified color model
	 */
	public DataTableCellRenderer(TableCellColorModel colorModel)
	{
		this();
		setTableCellColorModel(colorModel);
	}
	
    /**
     *
     * Returns the default table cell renderer.
     *
	 * This method is overriden to use the specific colors for each row
	 * @see javax.swing.table.DefaultTableCellRenderer
     */
	public Component getTableCellRendererComponent(JTable table, Object value,
                          boolean isSelected, boolean hasFocus, int row, int column) {

		Color backColor = null;
		Color foreColor = null;
		Color borderColor = null;
		Border border = null;
			
		//if (hasFocus) {
		//	return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		//}
		
		if (tableCellColorModel != null){
			backColor = tableCellColorModel.getBackgroundColor(row, column, isSelected, hasFocus);
			foreColor = tableCellColorModel.getForegroundColor(row, column, isSelected, hasFocus);
			borderColor = tableCellColorModel.getBorderColor(row, column, isSelected, hasFocus);
		}
		
		if (isSelected) {			
			
			if(foreColor == null){
				foreColor = (table.getSelectionForeground() != null) ? table.getSelectionForeground() : table.getForeground();
			}

			if (backColor == null){
				backColor = (table.getSelectionBackground() != null) ? table.getSelectionBackground() : table.getBackground();
			}						

			setFont(table.getFont().deriveFont(Font.BOLD));
		}
		else {
			
			if(foreColor == null){
				foreColor = table.getForeground();
			}
			if(backColor == null){
				backColor = table.getBackground();
			}
			
			setFont(table.getFont());
		}		
		
		super.setBackground(backColor);
		super.setForeground(foreColor);
		if (borderColor != null){
			border = BorderFactory.createLineBorder(borderColor);
		}
		else {
			border = noFocusBorder;
		}
		super.setBorder(border);
	
		setValue(value); 
	
		// ---- begin optimization to avoid painting background ----
		Color back = getBackground();
		boolean colorMatch = (back != null) && ( back.equals(table.getBackground()) ) && table.isOpaque();
			setOpaque(!colorMatch);
		// ---- end optimization to aviod painting background ----
	
		return this;
	}
	
	/**
	 * @return Returns the tableCellcolorModel.
	 */
	public TableCellColorModel getTableCellColorModel()
	{
		return tableCellColorModel;
	}
	
	/**
	 * @param tableCellcolorModel The tableCellcolorModel to set.
	 */
	public void setTableCellColorModel(TableCellColorModel colorModel)
	{
		this.tableCellColorModel = colorModel;
	}
}


