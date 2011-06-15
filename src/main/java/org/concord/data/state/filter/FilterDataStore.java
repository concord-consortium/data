/*
 * Last modification information:
 * $Revision: 1.1 $
 * $Date: 2007-06-29 22:03:38 $
 * $Author: imoncada $
 *
 * Licence Information
 * Copyright 2007 The Concord Consortium 
*/
package org.concord.data.state.filter;

import org.concord.data.state.DataStoreFilter;
import org.concord.framework.data.stream.DelegatingDataStore;

/**
 * FilterDataStore
 * 
 * This class is a data store that wraps around a filter
 * behaving like its resulting data store.  
 *
 * Date created: Jun 24, 2007
 *
 * @author Ingrid Moncada<p>
 *
 */
public class FilterDataStore extends DelegatingDataStore
{
	protected DataStoreFilter filter;
	
	/**
	 * @param resources
	 */
	public FilterDataStore()
	{
		super();
	}

	/**
	 * @param resources
	 */
	public FilterDataStore(DataStoreFilter filter)
	{
		super();
		setFilter(filter);
	}
	
	/**
	 * 
	 * @return
	 */
	public DataStoreFilter getFilter()
	{
		return filter;
	}

	/**
	 * 
	 * @param filter
	 */
	public void setFilter(DataStoreFilter filter)
	{
		this.filter = filter;
		setDataStore(filter.getResultDataStore());
	}
}
