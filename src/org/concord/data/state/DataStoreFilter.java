/*
 * Last modification information:
 * $Revision: 1.2 $
 * $Date: 2007-06-21 18:01:34 $
 * $Author: imoncada $
 *
 * Licence Information
 * Copyright 2007 The Concord Consortium 
*/
package org.concord.data.state;

import org.concord.framework.data.stream.DataStore;


/**
 * DataFilter
 * Filters the data on a data store and returns a new filtered data store
 *
 * Date created: Mar 18, 2007
 *
 * @author Ingrid Moncada<p>
 *
 */
public interface DataStoreFilter
{
	public DataStore getInputDataStore();
	public void setInputDataStore(DataStore ds);
	
	public DataStore getResultDataStore();
	public void setFilterDescription(DataStoreFilterDescription desc);
	public DataStoreFilterDescription getFilterDescription();
}
