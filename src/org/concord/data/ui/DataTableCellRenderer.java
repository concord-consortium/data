/**
 * Last modification information:
 * $Revision: 1.1 $
 * $Date: 2004-09-02 16:27:28 $
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
 * The renderer for the functionList in the table view
 * It shows the foreground according to each graphable's color
 * when the row is selected it shows a border and in BOLD
 *
 * @author Ingrid Moncada<p>
 *
*/

public class DataTableCellRenderer extends DefaultTableCellRenderer
{
	TableCellColorModel tableCellColorModel;
	
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
	public void setTableCellColorModel(TableCellColorModel model)
	{
		this.tableCellColorModel = model;
	}
}


