/*
 * Last modification information:
 * $Revision: 1.7 $
 * $Date: 2007-09-06 23:30:09 $
 * $Author: imoncada $
 *
 * Licence Information
 * Copyright 2007 The Concord Consortium 
*/
package org.concord.data.state.filter;

import org.concord.data.state.DataStoreFilter;
import org.concord.data.state.DataStoreFilterDescription;
import org.concord.data.state.DefaultDataStoreFilterDescription;

/**
 * DataFilterFactory
 * Class name and description
 *
 * Date created: Mar 21, 2007
 *
 * @author Ingrid Moncada<p>
 *
 */
public class DataStoreFilterFactory
{
	/**
	 * Factory method that returns the right class to handle the filter
	 * @param operation
	 * @return
	 */
	public static Class<?> getAvailableFilterClass(String operation)
	{
		operation.toLowerCase();
		if (operation.equals("maximum")){
			return DataMaximumFilter.class;
		}
		else if (operation.equals("range")){
			return DataRangeFilter.class;
		}
		else if (operation.equals("slope")){
			return DataSlopeFilter.class;
		}
		else if (operation.equals("average")){
			return DataAverageFilter.class;
		}
		return null;
	}
	
	public static DataStoreFilter getNewFilter(String operation) 
		throws InstantiationException, IllegalAccessException
	{
		//Create a new empty data filter description
		DataStoreFilterDescription desc = new DefaultDataStoreFilterDescription(operation);
		
		return getNewFilter(desc);
	}
	
	public static DataStoreFilter getNewFilter(DataStoreFilterDescription desc) 
		throws InstantiationException, IllegalAccessException
	{
		return getNewFilter(desc, null);
	}

	public static DataStoreFilter getNewFilter(DataStoreFilterDescription desc, Class filterClass) 
		throws InstantiationException, IllegalAccessException
	{
		if (desc == null) return null;
		if (filterClass == null) {
			filterClass = getAvailableFilterClass(desc.getOperation());
		}
		if (filterClass == null) return null;
		
		Object obj = filterClass.newInstance();		
		DataStoreFilter df = (DataStoreFilter)obj;
		df.setFilterDescription(desc);
		
		return df;
	}
	
}

