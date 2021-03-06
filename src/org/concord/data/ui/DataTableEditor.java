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
 * $Revision: 1.6 $
 * $Date: 2007-09-06 16:07:09 $
 * $Author: scytacki $
 *
 * Licence Information
 * Copyright 2004 The Concord Consortium 
*/
package org.concord.data.ui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.concord.framework.data.stream.DataStore;
import org.concord.framework.data.stream.WritableDataStore;


/**
 * DataTableExample
 * Class name and description
 *
 * Date created: Oct 24, 2004
 *
 * @author imoncada<p>
 *
 */
public class DataTableEditor extends JPanel
{
	/**
	 * Not intended to be serialized, added to remove compile warning.
	 */
	private static final long serialVersionUID = 1L;

	DataTablePanel tablePanel;
	DataStore dataStore;
	
	public DataTableEditor()
	{		
		tablePanel = new DataTablePanel();
		
		setLayout(new BorderLayout());
		add(tablePanel);
		
		JPanel inputPanel = createInputPanel();
		
		add(inputPanel, BorderLayout.SOUTH);
	}
	
	protected JPanel createInputPanel()
	{
		return new InputPanel();
	}

	public void setDataStore(DataStore dataStore)
	{
		this.dataStore = dataStore;
		
		dataStore.getDataChannelDescription(0).setPrecision(2);
		dataStore.getDataChannelDescription(1).setPrecision(2);
		tablePanel.getTableModel().addDataStore(dataStore);
	}
	
	public void addValue(int sample, int channel, float value)
	{
		if (dataStore instanceof WritableDataStore){
			((WritableDataStore)dataStore).setValueAt(sample, channel, new Float(value));
		}
	}
	
	public void removeValue()
	{
		int row = tablePanel.getTable().getSelectedRow();
		if (dataStore instanceof WritableDataStore){
			((WritableDataStore)dataStore).removeSampleAt(row);
		}
	}
	
	public void clearValues()
	{
		dataStore.clearValues();
	}
	
	class InputPanel extends JPanel 
		implements ActionListener
	{
		/**
		 * Not intended to be serialized, added to remove compile warning.
		 */
		private static final long serialVersionUID = 1L;

		JTextField f1 = new JTextField("0",3);
		JTextField f2 = new JTextField("0",3);
		JTextField f3 = new JTextField(3);
		
		public InputPanel()
		{
			JButton b;
			
			add(f1);
			add(f2);
			add(f3);
			
			b = new JButton("Add Value");
			b.setActionCommand("add");
			b.addActionListener(this);						
			add(b);

			b = new JButton("Remove Value");
			b.setActionCommand("remove");
			b.addActionListener(this);						
			add(b);
			
			b = new JButton("Clear");
			b.setActionCommand("clear");
			b.addActionListener(this);
			add(b);
		}

		/* (non-Javadoc)
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		public void actionPerformed(ActionEvent e)
		{
			String strAction = e.getActionCommand();
			
			if (strAction.equals("add")){
				
				try{
					addValue(
							Integer.parseInt(f1.getText()), 
							Integer.parseInt(f2.getText()), 
							Float.parseFloat(f3.getText()));
				}
				catch(Exception ex){
				}
			}
			else if (strAction.equals("remove")){
				removeValue();
			}
			else if (strAction.equals("clear")){
				clearValues();
			}
		}
		
	}
	/**
	 * @return Returns the dataStore.
	 */
	public DataStore getDataStore()
	{
		return dataStore;
	}
	/**
	 * @return Returns the tablePanel.
	 */
	public DataTablePanel getTablePanel()
	{
		return tablePanel;
	}
}
