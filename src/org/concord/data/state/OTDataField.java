/*
 * Created on Jan 26, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.concord.data.state;

import org.concord.framework.otrunk.OTObject;

/**
 * @author scott
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public interface OTDataField extends OTObject 
{
	public OTObject getDataProducer();
	
	public OTDataStore getDataStore();
	public void setDataStore(OTDataStore dataStore);	
}
