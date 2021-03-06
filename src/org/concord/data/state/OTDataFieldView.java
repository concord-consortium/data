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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;

import org.concord.data.ui.DataStoreLabel;
import org.concord.data.ui.DataValueLabel;
import org.concord.data.ui.StartableToolBar;
import org.concord.framework.data.stream.DataProducer;
import org.concord.framework.data.stream.WritableDataStore;
import org.concord.framework.otrunk.OTControllerService;
import org.concord.framework.otrunk.OTObject;
import org.concord.framework.otrunk.view.AbstractOTJComponentView;

/**
 * @author scott
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class OTDataFieldView extends AbstractOTJComponentView
	implements ActionListener 
{
	OTDataField otDataField;
    protected OTControllerService controllerService;
    WritableDataStore dataStore;
    
	protected DataValueLabel dataField;
	JButton saveButton = new JButton("Save");
		
	/* (non-Javadoc)
	 * @see org.concord.framework.otrunk.view.OTJComponentView#getComponent(boolean)
	 */
	public JComponent getComponent(OTObject otObject) 
	{
		this.otDataField = (OTDataField)otObject;
    	controllerService = createControllerService(otObject);
		
		OTDataStore otDataStore = otDataField.getDataStore();
		dataStore = (WritableDataStore) controllerService.getRealObject(otDataStore);
		
		DataStoreLabel dataStoreField = 
			new DataStoreLabel(dataStore,0);
		
		DataProducer dataProducer = (DataProducer)otDataField.getDataProducer();
		
		dataField = new DataValueLabel(dataProducer);

		JPanel fieldLabelPanel = new JPanel();
		fieldLabelPanel.setLayout(new BoxLayout(fieldLabelPanel, BoxLayout.X_AXIS));
		
		fieldLabelPanel.add(dataStoreField);
		fieldLabelPanel.add(dataField);
		
		// fieldPanel.add(fieldLabelPanel, BorderLayout.CENTER);
		
		StartableToolBar toolBar = new StartableToolBar();
		toolBar.setStartable(dataProducer);
		toolBar.setFloatable(false);
		fieldLabelPanel.add(toolBar);
		
		saveButton.addActionListener(this);
		fieldLabelPanel.add(saveButton);
		
		dataField.setColumns(7);
		dataStoreField.setColumns(7);
		return fieldLabelPanel;
	}

    /* (non-Javadoc)
     * @see org.concord.framework.otrunk.view.OTJComponentView#viewClosed()
     */
    public void viewClosed()
    {
    	controllerService.dispose();
    }
    	
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) 
	{
		float currentValue = dataField.getValue();
		dataStore.setValueAt(0, 0, new Float(currentValue));
	}


	public String getXHTMLText(File folder, int containerDisplayWidth, int containerDisplayHeight) {
		// TODO Auto-generated method stub
		return null;
	}
}
