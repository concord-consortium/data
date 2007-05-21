package org.concord.data.state;

import org.concord.framework.otrunk.OTObjectInterface;

/**
 * OTUnitValue
 * Class name and description
 *
 * Date created: March 6, 2007
 *
 * @author Shengyao Wang<p>
 *
 */
public interface OTUnitValue extends OTObjectInterface{
	
	public void setValue(float value);
	public float getValue();

	public void setUnit(String unit);
	public String getUnit();
}
