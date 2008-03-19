/*
 * Last modification information:
 * $Revision: 1.2 $
 * $Date: 2007-10-16 21:13:47 $
 * $Author: sfentress $
 *
 * Licence Information
 * Copyright 2004 The Concord Consortium 
*/
package org.concord.data.state;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.concord.data.ui.DataColumnDescription;
import org.concord.data.ui.DataTableModel;
import org.concord.data.ui.DataTablePanel;
import org.concord.framework.data.stream.DataStore;
import org.concord.framework.otrunk.OTControllerService;
import org.concord.framework.otrunk.OTObject;
import org.concord.framework.otrunk.OTObjectList;
import org.concord.framework.otrunk.view.AbstractOTJComponentView;


/**
 * 
 * @author sfentress
 *
 */
public class OTDataTableEditView extends AbstractOTJComponentView
{
	protected OTDataTable otTable;
    protected OTControllerService controllerService;
    private DataStore dataStore;
    private OTDataStore otDataStore;
    private DataTablePanel table;
	private JPanel tableWrapper;
	private JPanel titlesPanel;
	
	/**
	 * @see org.concord.framework.otrunk.view.OTJComponentView#getComponent(org.concord.framework.otrunk.OTObject)
	 */
	public JComponent getComponent(OTObject otObject)
	{
		otTable = (OTDataTable)otObject;
    	controllerService = createControllerService();
		
		table = new DataTablePanel();
		
		otDataStore = otTable.getDataStore();
		dataStore = (DataStore) controllerService.getRealObject(otDataStore);
		table.getTableModel().addDataStore(dataStore);
		
		//can always add values
		table.getTableModel().setShowLastRowForAddingNew(true);
		
		updateOTColumns(table.getTableModel(), dataStore, otTable.getColumns());
		
		tableWrapper = new JPanel(new BorderLayout());
		tableWrapper.add(table, BorderLayout.CENTER);
		
		JPanel optionsPanel = new JPanel(new FlowLayout(FlowLayout.LEADING));
		JLabel numColumnsLabel = new JLabel("Number of columns: ");
		final JTextField numColumnsField = new JTextField(""+dataStore.getTotalNumChannels());
		JButton updateButton = new JButton("Update");
		updateButton.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent e) {
				System.out.println("action");
				int numColumns = Integer.parseInt(numColumnsField.getText());
				otDataStore.setNumberChannels(numColumns);
				update();
				
			}});
		JLabel warningLabel = new JLabel("Warning: this will remove existing data");
		optionsPanel.add(numColumnsLabel);
		optionsPanel.add(numColumnsField);
		optionsPanel.add(updateButton);
		optionsPanel.add(warningLabel);
		
		JPanel lockedPanel = new JPanel();
		for (int i = 0; i < otDataStore.getNumberChannels(); i++) {
			final int columnNumber = i;
			final JCheckBox lockedBox = new JCheckBox();
			if (table.getTableModel().getDataColumn(dataStore, columnNumber).isLocked()){
				lockedBox.setSelected(true);
			}
			lockedBox.addActionListener(new ActionListener(){

				public void actionPerformed(ActionEvent arg0)
                {
					OTDataChannelDescription otChannelDesc = (OTDataChannelDescription) otDataStore.getChannelDescriptions().get(columnNumber);
	                if (lockedBox.isSelected()){
	                	otChannelDesc.setLocked(true);

	                	// set locked directly on the tablemodel here, so authors have immediate feedback
	                	// without reloading the model
	                	table.getTableModel().getDataColumn(dataStore, columnNumber).setLocked(true);
	                } else {
	                	otChannelDesc.setLocked(false);
	                	table.getTableModel().getDataColumn(dataStore, columnNumber).setLocked(false);
	                }
                }});
	        lockedPanel.add(lockedBox);
	        lockedPanel.add(Box.createGlue());
        }
		
		JPanel bottomPanel = new JPanel();
		bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));
		bottomPanel.add(lockedPanel);
		bottomPanel.add(optionsPanel);
		
		
		tableWrapper.add(bottomPanel, BorderLayout.SOUTH);
		
		createTitles();
		
		return tableWrapper;
	}
	
	private void update(){
		table.getTableModel().removeDataStore(dataStore);
		otDataStore = otTable.getDataStore();
		dataStore = (DataStore) controllerService.getRealObject(otDataStore);
		table.getTableModel().addDataStore(dataStore);
		
		//can always add values
		table.getTableModel().setShowLastRowForAddingNew(true);
		
		updateOTColumns(table.getTableModel(), dataStore, otTable.getColumns());
		
		tableWrapper.remove(titlesPanel);
		createTitles();
	}
	
	private void createTitles(){
		titlesPanel = new JPanel();
		for (int i = 0; i < dataStore.getTotalNumChannels(); i++) {
			String title = "";
			if (dataStore.getDataChannelDescription(i) != null){
				title = dataStore.getDataChannelDescription(i).getName();
			}
			
			final JTextField titleField = new JTextField(title);
			titleField.setColumns(4);
			final int index = i;
			titleField.addFocusListener(new FocusListener(){

				public void focusGained(FocusEvent arg0) {
					// TODO Auto-generated method stub
					
				}

				public void focusLost(FocusEvent arg0) {
					otDataStore = otTable.getDataStore();
					String title = titleField.getText();
					
					if (otDataStore.getChannelDescriptions().size() > index && otDataStore.getChannelDescriptions().get(index) != null){
						otDataStore.getChannelDescriptions().get(index).setName(title);
					} else {
						OTDataChannelDescription channelDescription;
						try {
							channelDescription = (OTDataChannelDescription) otDataStore.getOTObjectService().createObject(OTDataChannelDescription.class);
							channelDescription.setName(title);
							channelDescription.setNumericData(false);
							otDataStore.getChannelDescriptions().add(channelDescription);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					update();
				}});
			titlesPanel.add(titleField);
		}
		tableWrapper.add(titlesPanel, BorderLayout.NORTH);
	}

	/**
	 * @param dataStore
	 * @param columns
	 */
	protected void updateOTColumns(DataTableModel tableModel, DataStore dataStore, OTObjectList columns)
	{
		for (int i = 0; i < columns.size(); i++){
			OTDataColumnDescription otColDesc = (OTDataColumnDescription)columns.get(i); 
			if (otColDesc == null) continue;
			DataColumnDescription colDesc = tableModel.getDataColumn(dataStore, i);
			
			String label = otColDesc.getLabel();
			if (label != null){
				colDesc.setLabel(label);
			}
			if (otColDesc.isResourceSet("color")){
				colDesc.setColor(new Color(otColDesc.getColor()));
			}
		}
		tableModel.fireTableStructureChanged();
		
	}

	/**
	 * @see org.concord.framework.otrunk.view.OTJComponentView#viewClosed()
	 */
	public void viewClosed()
	{
		controllerService.dispose();
	}

}
