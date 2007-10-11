/*
 * Last modification information:
 * $Revision: 1.4 $
 * $Date: 2007-10-11 21:18:41 $
 * $Author: imoncada $
 *
 * Licence Information
 * Copyright 2007 The Concord Consortium 
*/
package org.concord.data.state;

import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.concord.data.state.ui.UnitEditPanel;
import org.concord.framework.otrunk.OTChangeEvent;
import org.concord.framework.otrunk.OTResourceList;
import org.concord.swing.CustomDialog;


/**
 * OTUnitValueEditView
 * 
 * This view allows the user to change the units of the value object
 *
 * Date created: Sep 25, 2007
 *
 * @author Ingrid Moncada<p>
 *
 */
public class OTUnitValueEditView extends OTUnitValueView
	implements MouseListener, ActionListener
{
	protected JLabel valueLabel;
	protected JLabel unitLabel;
	protected JComboBox unitSelection;
	protected CardLayout unitLayout;
	protected JPanel unitPanel;
	
	protected JButton unitsChangeButton;
	
	protected JPanel viewPanel;
	
	protected boolean selected;
	
	protected boolean useComboBox = true;
	protected boolean autoSelectable = false;
	protected boolean mouseSelectionEnabled = false;
	
	protected OTResourceList unitSelectionList;
	
	public OTUnitValueEditView()
	{
		super();
	}
	
	/**
	 * @see org.concord.data.state.OTUnitValueView#getUnitValueView()
	 */
	protected JComponent getUnitValueView()
	{
		valueLabel = new JLabel();
		unitLabel = new JLabel();
		
		viewPanel = new JPanel();
		
		if (useComboBox){
			unitSelection = new JComboBox();
			unitSelection.setOpaque(false);	
			
			unitPanel = new JPanel();
			unitLayout = new CardLayout();
			unitPanel.setLayout(unitLayout);
			unitPanel.add(unitLabel, "view");
			unitPanel.add(unitSelection, "edit");
			
			//viewPanel.setLayout(new GridLayout(1,2));
			//viewPanel.setLayout(new FlowLayout(FlowLayout.LEADING));
			viewPanel.setLayout(new BoxLayout(viewPanel, BoxLayout.X_AXIS));

			viewPanel.add(valueLabel);
			viewPanel.add(Box.createHorizontalStrut(10));
			viewPanel.add(unitPanel);
		}
		else{
			unitsChangeButton = new JButton("...");
			unitsChangeButton.setSize(new Dimension(30, 18));
			unitsChangeButton.setMaximumSize(new Dimension(30, 18));
			unitsChangeButton.setPreferredSize(new Dimension(30, 18));
			
			JPanel buttonPanel = new JPanel();
			buttonPanel.add(unitsChangeButton);
			unitsChangeButton.addActionListener(this);

			viewPanel.setLayout(new GridLayout(1,3));
			
			viewPanel.add(valueLabel);
			viewPanel.add(unitLabel);
			viewPanel.add(buttonPanel);
		}		
		
		viewPanel.setOpaque(true);
		
		setSelected(autoSelectable);
		
		if (mouseSelectionEnabled && !autoSelectable){
			viewPanel.addMouseListener(this);
		}
		
		updateView();
		
		return viewPanel;
	}
	
	/**
	 * @see org.concord.data.state.OTUnitValueView#updateView()
	 */
	protected void updateView()
	{
		String strValue = getStringValue(otObject, nf, false); 
		
		valueLabel.setText(strValue);
		unitLabel.setText(" " + otObject.getUnit());
		
		fillUnitSelection();
	}

	/**
	 * 
	 */
	public void setSelected(boolean b)
	{
		selected = b;
		
		if (unitLayout == null) return;
		
		if (selected){
//			viewPanel.setBackground(Color.gray);
			unitLayout.show(unitPanel, "edit");
		}
		else{
//			viewPanel.setBackground(null);
			unitLayout.show(unitPanel, "view");
		}
	}
	
	public boolean isSelected()
	{
		return selected;
	}

	public void mouseClicked(MouseEvent e){}
	public void mouseEntered(MouseEvent e){}
	public void mouseExited(MouseEvent e){}
	public void mouseReleased(MouseEvent e){}

	/**
	 * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
	 */
	public void mousePressed(MouseEvent e)
	{
		if (autoSelectable) return;
		
		setSelected(!isSelected());
	}

	/**
	 * Called when the user presses the "change units" button
	 * or when the user selects a unit in the combo box
	 * 
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e)
	{
		String newUnit = (String)unitSelection.getSelectedItem();
		
		//Check that they selected a different unit
		if (newUnit.equals(otObject.getUnit())) return;
		
		//Special case when they didn't have a unit or a value originally 
		//then it doesn't matter what the conversion rate is
		if (otObject.getUnit().equals("") || Float.isNaN(otObject.getValue())){
			//Just change unit, don't bother showing dialog
			otObject.setUnit(newUnit);
			return;
		}
		
		//UnitEditPanel editUnitPanel = new UnitEditPanel(otObject);
		UnitEditPanel editUnitPanel = new UnitEditPanel(otObject, newUnit);
		
		//Pop up a dialog to allow changing units
		int retVal = CustomDialog.showOKCancelDialog(
				viewPanel, editUnitPanel, "Change the units");
		
		if (retVal == JOptionPane.OK_OPTION){
			//Change unit
			OTUnitValue newValue = editUnitPanel.getNewValue();
			
			//Do an atomic change
			otObject.setDoNotifyChangeListeners(false);
			otObject.setValue(newValue.getValue());
			otObject.setUnit(newValue.getUnit());
			otObject.setDoNotifyChangeListeners(true);
			otObject.notifyOTChange("unit", OTChangeEvent.OP_CHANGE, otObject, null);
			//
			
			String strMsg = editUnitPanel.getErrorMessage();
			if (strMsg != null){
				JOptionPane.showMessageDialog(viewPanel, strMsg, 
						"Conversion value error", JOptionPane.ERROR_MESSAGE);
				updateView();
			}
		}
		else{
			//Show original value
			updateView();
		}
	}
	
	/**
	 * Fills the combo box with all the possible units to choose
	 */
	protected void fillUnitSelection()
	{
		unitSelection.removeActionListener(this);
		
		unitSelection.removeAllItems();
		unitSelection.addItem(otObject.getUnit());
		
		if (unitSelectionList != null){
			for (int i=0; i < unitSelectionList.size(); i++){
				String strUnit = (String)unitSelectionList.get(i);
				if (!strUnit.equals(otObject.getUnit())){
					unitSelection.addItem(strUnit);
				}
			}
		}

		unitSelection.addActionListener(this);
	}
	
	/**
	 * Sets the list that will be used when selecting the unit from the combo box
	 * @param list
	 */
	public void setUnitSelectionList(OTResourceList list)
	{
		unitSelectionList = list;
		fillUnitSelection();
	}
}
