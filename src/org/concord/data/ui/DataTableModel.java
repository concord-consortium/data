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
 * $Revision: 1.14 $
 * $Date: 2007-04-05 02:58:31 $
 * $Author: imoncada $
 *
 * Licence Information
 * Copyright 2004 The Concord Consortium 
*/
package org.concord.data.ui;

import java.awt.Color;
import java.io.IOException;
import java.io.PrintStream;
import java.io.Reader;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.table.AbstractTableModel;

import org.concord.data.stream.DataStoreUtil;
import org.concord.framework.data.stream.DataChannelDescription;
import org.concord.framework.data.stream.DataStore;
import org.concord.framework.data.stream.DataStoreEvent;
import org.concord.framework.data.stream.DataStoreListener;
import org.concord.framework.data.stream.DeltaDataStore;
import org.concord.framework.data.stream.WritableDataStore;

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
	implements DataStoreListener
{
	protected Vector dataStores;	//DataStore objects
	protected int step = 1;
	protected Vector dataColumns;	//DataColumnDescription objects
	
	//Hastable that keeps the data stores added with the addDataStore method. If a data store has been added
	//using this method, then the table model has to display ALYWAYS all its columns
	private Hashtable dataStoresWithFullColumns;

	/**
	 * 
	 */
	public DataTableModel()
	{
		super();
		dataStores = new Vector();
		dataColumns = new Vector();
		dataStoresWithFullColumns = new Hashtable();
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
		//This is in case the data store doesn't have any column YET
		addListenerToDataStore(dataStore);
		
		dataStoresWithFullColumns.put(dataStore, new Boolean(true));
		
		//Create a default DataColumnDescription for each channel in the data store
		int startChannel = 0;
		if (dataStore instanceof DeltaDataStore){
			if (((DeltaDataStore)dataStore).isUseDtAsChannel()){
				startChannel = -1;
			}
		}
		for (int i = startChannel; i<dataStore.getTotalNumChannels(); i++){
			addDataColumn(dataStore, i);
		}
	}
	
	/**
	 * 
	 * @param dataStore
	 */
	public void updateDataStore(DataStore dataStore)
	{
		if (isDataStoreWithAllColumns(dataStore)){
			removeDataStore(dataStore);
			addDataStore(dataStore);
		}
	}
	
	/**
	 * 
	 * @param dataStore
	 */
	public void removeDataStore(DataStore dataStore)
	{
		//Remove the listener and remove the data store from the vector 
		dataStore.removeDataStoreListener(this);
		dataStores.remove(dataStore);
		
		//Remove all columns of the data store 
		for (int i=0; i<dataColumns.size(); i++){
			DataColumnDescription colDesc = (DataColumnDescription)dataColumns.elementAt(i);
			if (colDesc.getDataStore() == dataStore){
				dataColumns.remove(colDesc);
				i = i - 1;
			}
		}
	}
	
	/**
	 * 
	 */
	public DataColumnDescription addDataColumn(DataColumnDescription dcol)
	{
		dataColumns.add(dcol);
		
		DataStore dataStore = dcol.getDataStore();
		addListenerToDataStore(dataStore);

		fireTableStructureChanged();
		
		return dcol;
	}
	
	/**
	 * @param dataStore
	 */
	private void addListenerToDataStore(DataStore dataStore)
	{
		//Make sure we have its data source in our vector
		int i;
		for (i=0; i<dataStores.size(); i++){
			if (dataStores.elementAt(i) == dataStore){
				break;
			}
		}
		if (i == dataStores.size()){
			
			dataStores.add(dataStore);
			dataStore.addDataStoreListener(this);
			
		}
		//
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
		DataColumnDescription dcol = (DataColumnDescription)dataColumns.elementAt(col);
		if (dcol == null) return null;
		
		DataStore dataStore = dcol.getDataStore();
		
		int indexSample = row*step;
		//Not all the data stores have the same number of samples
		if (indexSample >= dataStore.getTotalNumSamples()){
			return new String();
		}
		Object val = dataStore.getValueAt(indexSample, dcol.getDataStoreColumn());
		
		DataChannelDescription channelDesc = dataStore.getDataChannelDescription(dcol.getDataStoreColumn());
		
		if (channelDesc != null){
			if (val instanceof Float){
				//TEMP
				//((Float)val).
				if (channelDesc.isUsePrecision()){
					double precision = Math.pow(10, channelDesc.getPrecision());
					float retval = (float)(Math.floor(((precision) * ((Float)val).floatValue()) + 0.5) / precision);
					val = Float.toString(retval);
				}
			}
		} 
		
		return val;
	}

	/**
	 * @see javax.swing.table.TableModel#getColumnName(int)
	 */
	public String getColumnName(int col) 
	{
		String strLabel;
		
		DataColumnDescription dcol = (DataColumnDescription)dataColumns.elementAt(col);
		if (dcol == null) return null;
		
		DataStore dataStore = dcol.getDataStore();
		DataChannelDescription channelDesc = dataStore.getDataChannelDescription(dcol.getDataStoreColumn());
		
		strLabel = dcol.getLabel();
		if (strLabel == null) strLabel = "";
		if (channelDesc != null && channelDesc.getUnit() != null){
			strLabel = strLabel + " ("+ channelDesc.getUnit().getDimension() +")";
		}
		
		return strLabel;
    }

	
	
	/**
	 * @see javax.swing.table.TableModel#isCellEditable(int, int)
	 */
	public boolean isCellEditable(int rowIndex, int columnIndex)
	{
		//The cell is editable if the data store of the column is Writable
		DataColumnDescription dcol = (DataColumnDescription)dataColumns.elementAt(columnIndex);
		if (dcol == null) return false;
		
		DataStore dataStore = dcol.getDataStore();
		
		//The dt cannot be changed!
		if (dcol.getDataStoreColumn() == -1){
			return false;
		}
		
		if (dataStore instanceof WritableDataStore){
			return true;
		}
		return false;
	}
	
	
	/**
	 * @see javax.swing.table.TableModel#setValueAt(java.lang.Object, int, int)
	 */
	public void setValueAt(Object aValue, int rowIndex, int columnIndex)
	{
		DataColumnDescription dcol = (DataColumnDescription)dataColumns.elementAt(columnIndex);
		if (dcol == null) return;
		
		DataStore dataStore = dcol.getDataStore();
		
		//The cell is editable only if the data store of the column is Writable
		if (!(dataStore instanceof WritableDataStore)) return;

		//The dt cannot be changed!
		if (dcol.getDataStoreColumn() == -1){
			return;
		}
		
		DataChannelDescription channelDesc = dataStore.getDataChannelDescription(dcol.getDataStoreColumn());
		if (channelDesc != null && channelDesc.isNumericData()){
			//TEMP
			if (!(aValue instanceof Float)){
				String strVal = aValue.toString();
				if (strVal.equals("")){
					aValue = null;
				}
				else{
					try{
						float val = Float.parseFloat(strVal);
						if (channelDesc.isUsePrecision()){
							double precision = Math.pow(10, channelDesc.getPrecision());
							val = (float)(Math.floor(((precision) * val) + 0.5) / precision);
						}
						aValue = new Float(val);
					}
					catch(Exception ex){
						//Don't set the value if it is not a valid number
						return;
					}
				}
			}
		}
		
		Object oldValue = getValueAt(rowIndex, columnIndex);
		
		if (aValue == oldValue) return;
		if (aValue != null && aValue.equals(oldValue)) return;
		if (aValue != null && oldValue != null && aValue.toString().equals(oldValue.toString())) return;
			
		((WritableDataStore)dataStore).setValueAt(rowIndex, columnIndex, aValue);
	}
	
	/**
	 * the dataStep determines which rows of data will be displayed
	 * if the step is 5 then every 5th row will be displayed. The rest will be skipped 
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
		printData(System.out, null, true);
	}
	
	public void printData(PrintStream outS, int rowsToPrint[], boolean blnDebug)
	{
		if (blnDebug){
			outS.println("# Channels Total: "+getTotalNumChannels());		
			outS.println("# Samples Total: "+getTotalNumSamples());		
			outS.println("Step: "+getDataStep());
		}
		int numRows, numCols;
		numRows = getRowCount();
		numCols = getColumnCount();
		if (blnDebug){
			outS.println("Table: "+numRows+" X "+numCols);
		}
		for (int j=0; j<numCols; j++){
			outS.print(getColumnName(j)+"\t");
		}
		outS.println("");
		int r = 0;
		boolean blnPrint;
		for (int i=0; i<numRows; i++){
			blnPrint = false;
			if (rowsToPrint == null){
				blnPrint = true;
			}
			else {
				if (r < rowsToPrint.length && rowsToPrint[r] == i){
					blnPrint = true;
					r++;
				}
			}
			if (blnPrint){
				Object obj;
				for (int j=0; j<numCols; j++){
					obj = getValueAt(i, j);
					if (obj != null){
						outS.print(obj.toString());
					}
					outS.print("\t");
				}
				outS.println("");
			}
		}
	}

	/**
	 * @see org.concord.framework.data.stream.DataStoreListener#dataAdded(org.concord.framework.data.stream.DataStoreEvent)
	 */
	public void dataAdded(DataStoreEvent evt)
	{
		fireTableDataChanged();
	}

	/**
	 * @see org.concord.framework.data.stream.DataStoreListener#dataRemoved(org.concord.framework.data.stream.DataStoreEvent)
	 */
	public void dataRemoved(DataStoreEvent evt)
	{
		fireTableDataChanged();
	}

	/**
	 * @see org.concord.framework.data.stream.DataStoreListener#dataChanged(org.concord.framework.data.stream.DataStoreEvent)
	 */
	public void dataChanged(DataStoreEvent evt)
	{
		fireTableDataChanged();
	}
	
	/**
	 * @return Returns the dataColumns.
	 */
	public Vector getDataColumns()
	{
		return dataColumns;
	}

	/* (non-Javadoc)
	 * @see org.concord.framework.data.stream.DataStoreListener#dataChannelDescChanged(org.concord.framework.data.stream.DataStoreEvent)
	 */
	public void dataChannelDescChanged(DataStoreEvent evt)
	{
		DataStore dataStore = evt.getSource();
		if (isDataStoreWithAllColumns(dataStore)){
			updateDataStore(dataStore);
		}
		
		fireTableStructureChanged();
	}

	public boolean isDataStoreWithAllColumns(DataStore dataStore)
	{
		Boolean allColumns = (Boolean)dataStoresWithFullColumns.get(dataStore);
		if (allColumns != null && allColumns.booleanValue()){
			return true;
		}
		return false;
	}

	/**
	 * @param dataReader
	 */
	public void loadData(Reader dataReader)
	{
		DataColumnDescription dcol = (DataColumnDescription)dataColumns.elementAt(0);
		if (dcol == null) {
			throw new RuntimeException("data store doesn't have a single column");
		}
		
		DataStore dataStore = dcol.getDataStore();
		try {
			DataStoreUtil.loadData(dataReader, (WritableDataStore)dataStore, false);					
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}


/*

//getValueAt:
// 
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

 */