/**
 * Last modification information:
 * $Revision: 1.2 $
 * $Date: 2004-10-29 05:22:45 $
 * $Author: imoncada $
 *
 * Copyright 2004 The Concord Consortium
*/
package org.concord.data.ui;

import java.awt.Component;
import java.awt.Color;
import java.awt.Font;

import javax.swing.*;
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


