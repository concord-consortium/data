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
	
	public static float DEFAULT_value = Float.NaN;
	public void setValue(float value);
	public float getValue();

	public static String DEFAULT_unit = "";
	public void setUnit(String unit);
	public String getUnit();
}
