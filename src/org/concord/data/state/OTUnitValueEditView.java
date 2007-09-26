/*
 * Last modification information:
 * $Revision: 1.1 $
 * $Date: 2007-09-26 00:00:18 $
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

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.concord.data.state.ui.UnitEditPanel;
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
	
	protected OTResourceList unitSelectionList;
	
	public OTUnitValueEditView()
	{
		super();
		selected = false;
	}
	
	/**
	 * @see org.concord.data.state.OTUnitValueView#getUnitValueView()
	 */
	protected JComponent getUnitValueView()
	{
		String strValue = getStringValue(otObject, nf, false); 
		
		valueLabel = new JLabel(strValue);
		unitLabel = new JLabel(otObject.getUnit());
		
		viewPanel = new JPanel();
		
		if (useComboBox){
			setupUnitSelection(otObject);
			
			unitPanel = new JPanel();
			unitLayout = new CardLayout();
			unitPanel.setLayout(unitLayout);
			unitPanel.add(unitLabel, "view");
			unitPanel.add(unitSelection, "edit");
			
			viewPanel.setLayout(new GridLayout(1,2));
			
			viewPanel.add(valueLabel);
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
		viewPanel.addMouseListener(this);
				
		return viewPanel;
	}
	
	/**
	 * @see org.concord.data.state.OTUnitValueView#updateView()
	 */
/*	protected void updateView()
	{
		String strValue = getStringValue(otObject, nf, false); 
		
		valueLabel.setText(strValue);
		unitLabel.setText(otObject.getUnit());
	}*/

	/**
	 * 
	 */
	private void setSelected(boolean b)
	{
		selected = b;
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
		
		//Special case when they didn't have a unit originally		
		if (otObject.getUnit().equals("")){
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
			otObject.setValue(newValue.getValue());
			otObject.setUnit(newValue.getUnit());
			
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

	protected void setupUnitSelection(OTUnitValue value)
	{
		unitSelection = new JComboBox();
		unitSelection.setOpaque(false);	
		
		fillUnitSelection();
	}
	
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
	
	public void setUnitSelectionList(OTResourceList list)
	{
		unitSelectionList = list;
		fillUnitSelection();
	}
}
