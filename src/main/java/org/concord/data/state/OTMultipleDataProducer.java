/**
 * 
 */
package org.concord.data.state;

import org.concord.framework.otrunk.OTObjectList;

/**
 * @author qxie
 * 
 */
public interface OTMultipleDataProducer extends OTDataProducer {

	public OTObjectList getData();

}
