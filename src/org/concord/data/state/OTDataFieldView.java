/*
 * Created on Jan 26, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.concord.data.state;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.StringReader;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;

import org.concord.data.stream.DataStoreUtil;
import org.concord.data.ui.DataFlowControlToolBar;
import org.concord.data.ui.DataStoreLabel;
import org.concord.data.ui.DataValueLabel;
import org.concord.framework.data.stream.DataProducer;
import org.concord.framework.data.stream.DataProducerProxy;
import org.concord.framework.data.stream.DefaultDataStore;
import org.concord.framework.data.stream.WritableDataStore;
import org.concord.framework.otrunk.OTObject;
import org.concord.framework.otrunk.view.OTObjectView;
import org.concord.framework.otrunk.view.OTViewContainer;

/**
 * @author scott
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class OTDataFieldView 
	implements OTObjectView, ActionListener 
{
	OTDataField otObject;
	protected OTViewContainer viewContainer;
	protected DataValueLabel dataField;
	JButton saveButton = new JButton("Save");
	
	public OTDataFieldView(OTDataField otDataField, OTViewContainer vContainer)
	{
		this.otObject = otDataField;
		viewContainer = vContainer;
	}

	
	/* (non-Javadoc)
	 * @see org.concord.framework.otrunk.view.OTObjectView#getComponent(boolean)
	 */
	public JComponent getComponent(boolean editable) 
	{
		OTDataStore otDataStore = otObject.getDataStore();
		DataStoreLabel dataStoreField = 
			new DataStoreLabel(otDataStore,0);

		if(!editable) {
			return dataStoreField;
		}
		
		DataProducer dataProducer = null;
		OTObject dataProducerObj = otObject.getDataProducer();
		if(dataProducerObj instanceof DataProducerProxy)
		{
			dataProducer = ((DataProducerProxy)dataProducerObj).getDataProducer();
		} else {
			dataProducer = (DataProducer)dataProducerObj;
		}
		
		dataField = new DataValueLabel(dataProducer);

		JPanel fieldLabelPanel = new JPanel();
		fieldLabelPanel.setLayout(new BoxLayout(fieldLabelPanel, BoxLayout.X_AXIS));
		
		fieldLabelPanel.add(dataStoreField);
		fieldLabelPanel.add(dataField);
		
		// fieldPanel.add(fieldLabelPanel, BorderLayout.CENTER);
		
		DataFlowControlToolBar toolBar = new DataFlowControlToolBar(dataProducer);
		toolBar.setFloatable(false);
		fieldLabelPanel.add(toolBar);
		
		saveButton.addActionListener(this);
		fieldLabelPanel.add(saveButton);
		
		dataField.setColumns(7);
		dataStoreField.setColumns(7);
		return fieldLabelPanel;
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) 
	{
		float currentValue = dataField.getValue();
		otObject.getDataStore().setValueAt(0, 0, new Float(currentValue));
	}
}
