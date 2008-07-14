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


/**
 * DataAnalysisDescription
 * Class name and description
 *
 * Date created: Mar 18, 2007
 *
 * @author Ingrid Moncada<p>
 *
 */
public interface DataStoreFilterDescription
{
	public String getOperation();
	
	//For now it has x and y values only
	public int getChannelX();
	public int getChannelY();
	
	public String getProperty(String name);
	public void setProperty(String name, String value);
	
	public void addPropertyListener(DataStoreFilterPropertyListener listener);
	public void removePropertyListener(DataStoreFilterPropertyListener listener);	
}
