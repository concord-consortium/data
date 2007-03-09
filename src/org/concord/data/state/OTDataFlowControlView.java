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
 * Created on Jan 26, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.concord.data.state;

import java.io.File;

import javax.swing.JButton;
import javax.swing.JComponent;

import org.concord.data.ui.DataFlowControlToolBar;
import org.concord.data.ui.DataValueLabel;
import org.concord.framework.data.DataFlow;
import org.concord.framework.otrunk.OTObject;
import org.concord.framework.otrunk.OTObjectList;
import org.concord.framework.otrunk.view.OTJComponentView;

/**
 * @author scott
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class OTDataFlowControlView 
	implements OTJComponentView
{
	OTDataFlowControl otObject;
	protected DataValueLabel dataField;
	JButton saveButton = new JButton("Save");
	
	/* (non-Javadoc)
	 * @see org.concord.framework.otrunk.view.OTJComponentView#getComponent(boolean)
	 */
	public JComponent getComponent(OTObject otObject, boolean editable) 
	{
		this.otObject = (OTDataFlowControl)otObject; 
		OTObjectList dataFlows = this.otObject.getDataFlows();
		
		DataFlowControlToolBar toolBar = new DataFlowControlToolBar();
		
		for(int i=0; i<dataFlows.size(); i++) {
			DataFlow dataFlow = (DataFlow)dataFlows.get(i);
			toolBar.addDataFlowObject(dataFlow);
		}
		
		return toolBar;
	}

    /* (non-Javadoc)
     * @see org.concord.framework.otrunk.view.OTJComponentView#viewClosed()
     */
    public void viewClosed()
    {
        // TODO Auto-generated method stub
    }
    	
	public String getXHTMLText(File folder, int containerDisplayWidth, int containerDisplayHeight) 
	{
		// TODO Auto-generated method stub		
		return null;
	}
}
