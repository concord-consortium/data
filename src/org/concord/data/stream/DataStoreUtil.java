/*
 * Last modification information:
 * $Revision: 1.1 $
 * $Date: 2004-11-16 18:48:07 $
 * $Author: scytacki $
 *
 * Licence Information
 * Copyright 2004 The Concord Consortium 
*/
package org.concord.data.stream;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.StringTokenizer;

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
	public static void loadData(Reader reader, WritableDataStore ds, boolean hasHeaders)
		throws IOException
	{
		BufferedReader lineReader = new BufferedReader(reader);
		
		if(hasHeaders) {
			// parse the headers and set the channel descriptions
		}

		int row = 0;
		int col = 0;

		String line = lineReader.readLine();
		while(line != null) {
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

			line = lineReader.readLine();
		}		
	}
}
