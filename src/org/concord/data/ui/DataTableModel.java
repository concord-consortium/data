/*
 * Last modification information:
 * $Revision: 1.1 $
 * $Date: 2004-08-24 23:15:07 $
 * $Author: imoncada $
 *
 * Licence Information
 * Copyright 2004 The Concord Consortium 
*/
package org.concord.data.ui;

import java.awt.Color;
import java.util.Vector;

import javax.swing.table.AbstractTableModel;

import org.concord.framework.data.stream.DataChannelDescription;
import org.concord.framework.data.stream.DataStore;


/**
 * DataTableModel
 * Class name and description
 *
 * Date created: Aug 23, 2004
 *
 * @author imoncada<p>
 *
 */
public class DataTableModel extends AbstractTableModel
{
	protected Vector dataStores;	//DataStore objects
	protected int step = 1;
	protected Vector dataColumns;	//DataColumnDescription objects

	/**
	 * 
	 */
	public DataTableModel()
	{
		super();
		dataStores = new Vector();
		dataColumns = new Vector();
	}

	/**
	 * 
	 */
	public DataTableModel(DataStore dataStore)
	{
		this();
		addDataStore(dataStore);
	}

	/**
	 * 
	 * @param dataStore
	 */
	public void addDataStore(DataStore dataStore)
	{
		this.dataStores.add(dataStore);
		
		//Create a default DataColumnDescription for each channel in the data store
		for (int i=0; i<dataStore.getTotalNumChannels(); i++){
			addDataColumn(dataStore, i);
		}
	}
	
	/**
	 * 
	 */
	public DataColumnDescription addDataColumn(DataColumnDescription dcol)
	{
		dataColumns.add(dcol);
		
		//Make sure we have its data source in our vector
		DataStore dataStore = dcol.getDataStore();
		int i;
		for (i=0; i<dataStores.size(); i++){
			if (dataStores.elementAt(i) == dataStore){
				break;
			}
		}
		if (i == dataStores.size()){
			dataStores.add(dataStore);
		}
		//
		
		fireTableStructureChanged();
		
		return dcol;
	}
	
	/**
	 * 
	 */
	public DataColumnDescription addDataColumn(DataStore dataStore, int dataStoreColumn)
	{
		//return addDataColumn(dataStore, dataStoreColumn, "", null);
		DataColumnDescription dcol;
		dcol = new DataColumnDescription(dataStore, dataStoreColumn);
		DataChannelDescription channelDesc = dataStore.getDataChannelDescription(dataStoreColumn);
		if (channelDesc != null){
			dcol.setLabel(channelDesc.getName());
		}
		return addDataColumn(dcol);
	}

	/**
	 * 
	 */
	public DataColumnDescription addDataColumn(DataStore dataStore, int dataStoreColumn, String label, Color color)
	{
		DataColumnDescription dcol = new DataColumnDescription(dataStore, dataStoreColumn);
		dcol.setLabel(label);
		dcol.setColor(color);
		return addDataColumn(dcol);
	}
	
	/**
	 * 
	 */
	public DataColumnDescription getDataColumn(DataStore dataStore, int dataStoreColumn)
	{
		for (int i=0; i<dataColumns.size(); i++){
			DataColumnDescription dcol = (DataColumnDescription)dataColumns.elementAt(i);
			if (dcol.getDataStore() == dataStore && dcol.getDataStoreColumn() == dataStoreColumn){
				return dcol;
			}
		}
		return null;
	}
	
	/**
	 * 
	 */
	public void setDataColumnPosition(DataColumnDescription dcol, int newPosition)
	{
		if (newPosition < 0 || newPosition >= dataColumns.size()){
			System.err.println("DataTableModel setDataColumnPosition failed: Position "+newPosition+" not valid.");
			return;
		}
		
		if (dataColumns.remove(dcol)){
			dataColumns.insertElementAt(dcol, newPosition);
		}
	}
	
	/**
	 * 
	 */
	public void removeAllColumns()
	{
		dataColumns.removeAllElements();
		dataStores.removeAllElements();
	}
	
	/**
	 * @see javax.swing.table.TableModel#getColumnCount()
	 */
	public int getColumnCount()
	{
		return dataColumns.size();
	}
	
	/**
	 * 
	 */
	public int getTotalNumChannels()
		{
		int n = 0;
		for (int i=0; i<dataStores.size(); i++){
			n = n + ((DataStore)dataStores.elementAt(i)).getTotalNumChannels();
		}
		return n;
	}

	/**
	 * @see javax.swing.table.TableModel#getRowCount()
	 */
	public int getRowCount()
	{
		return (int)(Math.ceil((double)getTotalNumSamples()/step));
	}

	/**
	 * @see javax.swing.table.TableModel#getValueAt(int, int)
	 */
	public Object getValueAt(int row, int col)
	{
//		DataStore dataStore;
//		int n;
//		int m = 0;
		
//		if (col == 0){
//			//Time column
//			return new String("ha");
//		}
		
//		col = col-1;
//		for (int i=0; i<dataStores.size(); i++){
//			dataStore = (DataStore)dataStores.elementAt(i);
//			n = dataStore.getTotalNumChannels();
//			if (col < (m+n)) return dataStore.getValueAt(row*step, col - m);
//			m = m + n;
//		}
//		return null;
		
		DataColumnDescription dcol = (DataColumnDescription)dataColumns.elementAt(col);
		DataStore dataStore = dcol.getDataStore();
		return dataStore.getValueAt(row*step, dcol.getDataStoreColumn());
	}

	/**
	 * @see javax.swing.table.TableModel#getColumnName(int)
	 */
	public String getColumnName(int col) {
		DataColumnDescription dcol = (DataColumnDescription)dataColumns.elementAt(col);
		return dcol.getLabel();
    }

	/**
	 * @return Returns the step.
	 */
	public int getDataStep()
	{
		return step;
	}
	
	/**
	 * @param step The step to set.
	 */
	public void setDataStep(int step)
	{
		this.step = step;
	}
	
	/**
	 * @see javax.swing.table.TableModel#getRowCount()
	 */
	public int getTotalNumSamples()
	{
		int n;
		int m = 0;
		for (int i=0; i<dataStores.size(); i++){
			n = ((DataStore)dataStores.elementAt(i)).getTotalNumSamples();
			if (n > m){
				m = n;
			}
		}
		return m;
	}

	/**
	 * Debugging purposes :p
	 */
	public void printData()
	{
		System.out.println("# Channels Total: "+getTotalNumChannels());		
		System.out.println("# Samples Total: "+getTotalNumSamples());		
		System.out.println("Step: "+getDataStep());
		int ti, tj;
		ti = getRowCount();
		tj = getColumnCount();
		System.out.println("Table: "+ti+" X "+tj);
		for (int i=0; i<ti; i++){
			for (int j=0; j<tj; j++){
				System.out.print(getValueAt(i, j).toString()+"   ");
			}
			System.out.println("");
		}
	}
}
