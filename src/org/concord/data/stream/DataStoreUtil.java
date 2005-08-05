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
 * $Revision: 1.9 $
 * $Date: 2005-08-05 16:17:19 $
 * $Author: maven $
 *
 * Licence Information
 * Copyright 2004 The Concord Consortium 
*/
package org.concord.data.stream;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;
import java.io.Reader;
import java.io.StringReader;
import java.util.StringTokenizer;

import org.concord.framework.data.stream.DataChannelDescription;
import org.concord.framework.data.stream.DataStore;
import org.concord.framework.data.stream.WritableDataStore;


/**
 * DataStoreUtil
 * Class name and description
 *
 * Date created: Nov 15, 2004
 *
 * @author scott<p>
 *
 */
public class DataStoreUtil
{
	public static void loadData(String data, WritableDataStore ds, 
			boolean hasHeaders)
		throws IOException
	{
		StringReader dataReader = new StringReader(data);
		
		loadData(dataReader, ds, hasHeaders);
	}
	
	public static void loadData(Reader reader, WritableDataStore ds, 
			boolean hasHeaders)
		throws IOException
	{
		BufferedReader lineReader = new BufferedReader(reader);

		ds.clearValues();
		int numChannels = ds.getTotalNumChannels();
		
		if(hasHeaders) {
			// parse the headers and set the channel descriptions
		}

		int row = 0;
		int col = 0;
		
		
		String line = lineReader.readLine();
		while(line != null) {
			if(numChannels > 0) {
				StringTokenizer toks = new StringTokenizer(line, "\t ");
				
				while(toks.hasMoreTokens()) {
					String strValue = toks.nextToken();
					
					try {
						Float fValue = Float.valueOf(strValue);
						ds.setValueAt(row, col, fValue);
					} catch (NumberFormatException e) {
						ds.setValueAt(row, col, strValue);
					}
					col++;
					if(col == numChannels) {
						col = 0;
						row++;
					} 
				}				
			} else {
				col = 0;

				StringTokenizer toks = new StringTokenizer(line, "\t");
				
				while(toks.hasMoreTokens()) {
					String strValue = toks.nextToken();
					
					try {
						Float fValue = Float.valueOf(strValue);
						ds.setValueAt(row, col, fValue);
					} catch (NumberFormatException e) {
						ds.setValueAt(row, col, strValue);
					}
					col++;
				}
				row++;
			}
				
			line = lineReader.readLine();
		}
		
	}
	
	public static void printData(PrintStream outS, DataStore ds, 
			int rowsToPrint[], boolean printHeaders)
	{
		int numRows, numCols;
		numRows = ds.getTotalNumSamples();
		numCols = ds.getTotalNumChannels();

		if (printHeaders){
			for (int j=0; j<numCols; j++){
				DataChannelDescription cDesc = ds.getDataChannelDescription(j);
				outS.print(cDesc.getName()+"\t");
			}
			outS.println("");
		}

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
					obj = ds.getValueAt(i, j);
					if (obj != null){
						outS.print(obj.toString());
					}
					outS.print("\t");
				}
				outS.println("");
			}
		}
	}

}
