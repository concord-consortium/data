/*
 * Last modification information:
 * $Revision: 1.1 $
 * $Date: 2004-09-02 16:27:28 $
 * $Author: imoncada $
 *
 * Licence Information
 * Copyright 2004 The Concord Consortium 
*/
package org.concord.data.ui;

import java.awt.Color;


/**
 * TableColorModel
 * Class name and description
 *
 * Date created: Sep 1, 2004
 *
 * @author imoncada<p>
 *
 */
public interface TableCellColorModel
{
	/**
	 * 
	 * @param row
	 * @param col
	 * @param selected
	 * @return
	 */
	public Color getBackgroundColor(int row, int col, boolean selected, boolean focus);

	/**
	 * 
	 * @param row
	 * @param col
	 * @param selected
	 * @return
	 */
	public Color getForegroundColor(int row, int col, boolean selected, boolean focus);
	
	/**
	 * 
	 * @param row
	 * @param col
	 * @param selected
	 * @return
	 */
	public Color getBorderColor(int row, int col, boolean selected, boolean focus);
	
}
