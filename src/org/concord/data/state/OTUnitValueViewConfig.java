package org.concord.data.state;

/**
 * OTUnitValueViewConfig
 * Class name and description
 *
 * Date created: March 6, 2007
 *
 * @author Shengyao Wang<p>
 *
 */

public interface OTUnitValueViewConfig 
	extends org.concord.framework.otrunk.view.OTViewEntry {
	
	public static int DEFAULT_precision = 2;
	public int getPrecision();
	public void setPrecision(int precision);
}
