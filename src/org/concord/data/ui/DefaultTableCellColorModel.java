

/*
 *  Copyright (C) 2004  The Concord Consortium, Inc.,
 *  10 Concord Crossing, Concord, MA 01741
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
 */
/*
 * Last modification information:
 * $Revision: 1.2 $
 * $Date: 2004-11-12 19:43:27 $
 * $Author: eblack $
 *
 * Licence Information
 * Copyright 2004 The Concord Consortium 
*/
package org.concord.data.ui;

import java.awt.Color;
import java.util.Hashtable;


/**
 * DefaultTableCellColorModel
 * Class name and description
 *
 * Date created: Sep 1, 2004
 *
 * @author imoncada<p>
 *
 */
public class DefaultTableCellColorModel
	implements TableCellColorModel
{
	Hashtable backColors;
	Hashtable foreColors;
	Hashtable borderColors;
	
	Hashtable backColorsRow;
	Hashtable foreColorsRow;
	Hashtable borderColorsRow;

	Hashtable backColorsCol;
	Hashtable foreColorsCol;
	Hashtable borderColorsCol;
	
	/**
	 * 
	 */
	public DefaultTableCellColorModel()
	{
		super();
	}

	/**
	 * @see org.concord.data.ui.TableCellColorModel#getBackgroundColor(int, int, boolean, boolean)
	 */
	public Color getBackgroundColor(int row, int col, boolean selected, boolean focus)
	{
		Color color;
		color = getColor(backColors, row, col, selected, focus);
		if (color == null){
			color = getColor(backColorsRow, row, selected, focus);
			if (color == null){
				color = getColor(backColorsCol, col, selected, focus);
			}
		}
		return color;
	}

	/* (non-Javadoc)
	 * @see org.concord.data.ui.TableCellColorModel#getForegroundColor(int, int, boolean, boolean)
	 */
	public Color getForegroundColor(int row, int col, boolean selected, boolean focus)
	{
		Color color;
		color = getColor(foreColors, row, col, selected, focus);
		if (color == null){
			color = getColor(foreColorsRow, row, selected, focus);
			if (color == null){
				color = getColor(foreColorsCol, col, selected, focus);
			}
		}
		return color;
	}

	/**
	 * @see org.concord.data.ui.TableCellColorModel#getBorderColor(int, int, boolean, boolean)
	 */
	public Color getBorderColor(int row, int col, boolean selected, boolean focus)
	{
		Color color;
		color = getColor(borderColors, row, col, selected, focus);
		if (color == null){
			color = getColor(borderColorsRow, row, selected, focus);
			if (color == null){
				color = getColor(borderColorsCol, col, selected, focus);
			}
		}
		return color;
	}

	/**
	 * @see org.concord.data.ui.TableCellColorModel#getBorderColor(int, int, boolean, boolean)
	 */
	protected Color getColor(Hashtable colors, int row, int col, boolean selected, boolean focus)
	{
		if (colors == null) return null;
		Hashtable rowHash = (Hashtable)(colors.get(new Integer(row)));
		if (rowHash == null) return null;
		return getColor(rowHash, col, selected, focus);
	}
	
	protected Color getColor(Hashtable colors, int i, boolean selected, boolean focus)
	{
		if (colors == null) return null;
		Color color = null;
		Color[] cols = (Color[])colors.get(new Integer(i));
		if (cols == null || cols.length < 3) return null;
		
		if (focus){
			color = cols[2];
		}
		if (selected || (focus && color == null)){
			color = cols[1];
		}
		else if (!selected || color == null){
			color = cols[0];
		}
		
		return color;
	}
		
	public void setBackgroundColor(Color color, int row, int col, boolean selected, boolean focus)
	{	
		if (backColors == null){
			backColors = new Hashtable();
		}
		setColor(backColors, color, row, col, selected, focus);
	}

	public void setForegroundColor(Color color, int row, int col, boolean selected, boolean focus)
	{	
		if (foreColors == null){
			foreColors = new Hashtable();
		}
		setColor(foreColors, color, row, col, selected, focus);
	}

	public void setBordergroundColor(Color color, int row, int col, boolean selected, boolean focus)
	{	
		if (borderColors == null){
			borderColors = new Hashtable();
		}
		setColor(borderColors, color, row, col, selected, focus);
	}
	
	public void setColorRow(Color backColor, Color foreColor, Color borderColor, int row)
	{
		setBackgroundColorRow(backColor, row, false, false);
		setBackgroundColorRow(backColor, row, true, false);
		setBackgroundColorRow(backColor, row, false, true);
		
		setForegroundColorRow(foreColor, row, false, false);
		setForegroundColorRow(foreColor, row, true, false);
		setForegroundColorRow(foreColor, row, false, true);
		
		setBorderColorRow(borderColor, row, false, false);
		setBorderColorRow(borderColor, row, true, false);
		setBorderColorRow(borderColor, row, false, true);
	}
	
	public void setBackgroundColorRow(Color color, int row, boolean selected, boolean focus)
	{	
		if (backColorsRow == null){
			backColorsRow = new Hashtable();
		}
		setColor(backColorsRow, color, row, selected, focus);
	}
	
	public void setForegroundColorRow(Color color, int row, boolean selected, boolean focus)
	{	
		if (foreColorsRow == null){
			foreColorsRow = new Hashtable();
		}
		setColor(foreColorsRow, color, row, selected, focus);
	}
	
	public void setBorderColorRow(Color color, int row, boolean selected, boolean focus)
	{	
		if (borderColorsRow == null){
			borderColorsRow = new Hashtable();
		}
		setColor(borderColorsRow, color, row, selected, focus);
	}
	
	public void setColorColumn(Color backColor, Color foreColor, Color borderColor, int col)
	{
		setBackgroundColorColumn(backColor, col, false, false);
		setBackgroundColorColumn(backColor, col, true, false);
		setBackgroundColorColumn(backColor, col, false, true);
		
		setForegroundColorColumn(foreColor, col, false, false);
		setForegroundColorColumn(foreColor, col, true, false);
		setForegroundColorColumn(foreColor, col, false, true);
		
		setBorderColorColumn(borderColor, col, false, false);
		setBorderColorColumn(borderColor, col, true, false);
		setBorderColorColumn(borderColor, col, false, true);
	}

	public void setBackgroundColorColumn(Color color, int column, boolean selected, boolean focus)
	{	
		if (backColorsCol == null){
			backColorsCol = new Hashtable();
		}
		setColor(backColorsCol, color, column, selected, focus);
	}
	
	public void setForegroundColorColumn(Color color, int column, boolean selected, boolean focus)
	{	
		if (foreColorsCol == null){
			foreColorsCol = new Hashtable();
		}
		setColor(foreColorsCol, color, column, selected, focus);
	}
	
	public void setBorderColorColumn(Color color, int column, boolean selected, boolean focus)
	{	
		if (borderColorsCol == null){
			borderColorsCol = new Hashtable();
		}
		setColor(borderColorsCol, color, column, selected, focus);
	}

	protected void setColor(Hashtable colors, Color color, int row, int col, boolean selected, boolean focus)
	{
		Integer intI;
		intI = new Integer(row);
		
		Hashtable rowHash = (Hashtable)(colors.get(intI));
		if (rowHash == null){
			rowHash = new Hashtable();
			colors.put(intI, rowHash);
		}
		setColor(rowHash, color, col, selected, focus);
	}

	protected void setColor(Hashtable colors, Color color, int i, boolean selected, boolean focus)
	{
		Integer intI;
		intI = new Integer(i);
		
		Color[] cols = (Color[])colors.get(intI);
		if (cols == null){
			cols = new Color[3];
			cols[0] = null;
			cols[1] = null;
			cols[2] = null;
			colors.put(intI, cols);
		}
		
		if (focus){
			cols[2] = color;
		}
		else if (selected){
			cols[1] = color;
		}
		else {
			cols[0] = color;
		}		
	}
}
