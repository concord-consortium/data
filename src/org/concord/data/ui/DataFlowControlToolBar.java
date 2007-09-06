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
 * $Revision: 1.5 $
 * $Date: 2007-09-06 16:07:09 $
 * $Author: scytacki $
 *
 * Licence Information
 * Copyright 2004 The Concord Consortium 
*/
package org.concord.data.ui;

import java.awt.Component;
import java.util.Vector;

import javax.swing.JToolBar;

import org.concord.framework.data.DataFlow;


/**
 * DataFlowControlToolBar
 * Class name and description
 *
 * Date created: Sep 20, 2004
 *
 * @author imoncada<p>
 *
 */
public class DataFlowControlToolBar extends JToolBar
{
	/**
	 * Not intended to be serialized, added to remove compile warning.
	 */
	private static final long serialVersionUID = 1L;

	protected Vector objsFlow;				//DataFlow objects

	/**
	 * Default constructor
	 * Creates by default START, STOP and RESET buttons
	 */
	public DataFlowControlToolBar()
	{
		this(true);
	}
		
	/**
	 * Creates by default START, STOP and RESET buttons
	 * acting on the data flow object specified
	 * @param objFlow
	 */
	public DataFlowControlToolBar(DataFlow objFlow)
	{
		this();
		addDataFlowObject(objFlow);
	}
	
	/**
	 * If bCreateDefaultButtons is true,
	 * creates by default START, STOP and RESET buttons
	 * @param bCreateDefaultButtons
	 */
	public DataFlowControlToolBar(boolean bCreateDefaultButtons)
	{
		super();
		objsFlow = new Vector();
		if (bCreateDefaultButtons){
			addDataFlowControlButton(DataFlowControlAction.FLOW_CONTROL_START);
			addDataFlowControlButton(DataFlowControlAction.FLOW_CONTROL_STOP);
			addDataFlowControlButton(DataFlowControlAction.FLOW_CONTROL_RESET);
		}
	}
	
	public void addDataFlowControlButton(int type)
	{
		DataFlowControlButton b = new DataFlowControlButton(type);
		add(b);
	}
	
	public void add(DataFlowControlButton b)
	{
		super.add(b);
		addDataFlowObjects(b);
	}
	
	/**
	 * Adds a DataFlow object that will be controlled by this button
	 * @param objFlow The data flow or simulation object to control
	 */
	public void addDataFlowObject(DataFlow objFlow)
	{
		if (objsFlow.contains(objFlow)) return;
		objsFlow.addElement(objFlow);
		
        int n = this.getComponentCount();
        if (n <= 0) return;
        Component[] comps = this.getComponents();
        DataFlowControlButton b;
        for ( int i = 0; i < n; i++) {
        	if (comps[i] instanceof DataFlowControlButton){
        		b = (DataFlowControlButton)comps[i];
        		b.addDataFlowObject(objFlow);
        	}
        }
	}

	/**
	 * Removes a DataFlow object that was previously controlled by this button
	 * @param objFlow The data flow or simulation object to remove and not be controlled anymore
	 */
	public void removeDataFlowObject(DataFlow objFlow)
	{
		objsFlow.removeElement(objFlow);
		
        int n = this.getComponentCount();
        if (n <= 0) return;
        Component[] comps = this.getComponents();
        DataFlowControlButton b;
        for ( int i = 0; i < n; i++) {
        	if (comps[i] instanceof DataFlowControlButton){
        		b = (DataFlowControlButton)comps[i];
        		b.removeDataFlowObject(objFlow);
        	}
        }
	}
	
	/**
	 * Add data flow objects to an "empty" button
	 * @param b
	 */
	protected void addDataFlowObjects(DataFlowControlButton b)
	{
		for (int i=0; i<objsFlow.size(); i++){
			b.addDataFlowObject((DataFlow)objsFlow.elementAt(i));
		}
	}
	
}
