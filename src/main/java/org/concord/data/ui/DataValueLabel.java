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
 * $Revision: 1.8 $
 * $Date: 2007-09-06 16:07:09 $
 * $Author: scytacki $
 *
 * Licence Information
 * Copyright 2004 The Concord Consortium 
*/
package org.concord.data.ui;

import java.awt.Font;

import javax.swing.JTextField;

import org.concord.framework.data.DataDimension;
import org.concord.framework.data.stream.DataChannelDescription;


/**
 * DataValueLabel
 * This is a JLabel can be used to display a single 
 * data value 
 *
 */
public class DataValueLabel extends JTextField
{
	/**
	 * Not intended to be serialized, added to remove compile warning.
	 */
	private static final long serialVersionUID = 1L;
	private float currentValue;

	private String blankValue = "-----";

	/**
	 * Default constructor. Creates an empty label that doesn't listen to
	 * any data producer yet.
	 */
	public DataValueLabel()
	{
		super();
		setEditable(false);
		setHorizontalAlignment(JTextField.TRAILING);
		setColumns(10);
		setText(blankValue);
	}
	
	/**
	 * Sets the value of the label. 
	 * Pays attention to the properties of the channel that is being displayed
	 * (channel description), specially the precision
	 * @param val	float value to display
	 */
	public void setValue(float val)
	{
		currentValue = val;
		DataChannelDescription channelDesc = getChannelDescription();
		
		//If the channel has a description, display the value with the correct precision
		String units = "";
		if (channelDesc != null){
			if (channelDesc.isUsePrecision()){
				double precision = Math.pow(10, channelDesc.getPrecision());
				val = (float)(Math.floor((val / precision ) + 0.5) * precision);
			}
			DataDimension unit = channelDesc.getUnit();
			if(unit != null){
				units = " " + unit.getDimension();
			}
		}
		setText(Float.toString(val) + units);
	}
	
	public float getValue()
	{
		return currentValue;
	}
	
	public void clear()
	{
		currentValue = Float.NaN;
		setText(blankValue);
	}

	public void setFontSize(int size)
	{
		Font newTextFieldFont=new Font(getFont().getName(),getFont().getStyle(),size);  
		setFont(newTextFieldFont);
	}
	
	protected DataChannelDescription getChannelDescription()
	{
		return null;
	}
}
