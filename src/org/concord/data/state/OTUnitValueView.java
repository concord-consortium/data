package org.concord.data.state;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import javax.swing.JComponent;
import javax.swing.JLabel;

import org.concord.framework.otrunk.OTObject;
import org.concord.framework.otrunk.view.OTJComponentView;
import org.concord.framework.otrunk.view.OTViewEntry;
import org.concord.framework.otrunk.view.OTViewEntryAware;

/**
 * OTUnitValueView
 * Class name and description
 *
 * Date created: March 6, 2007
 *
 * @author Shengyao Wang<p>
 *
 */

public class OTUnitValueView implements OTViewEntryAware, OTJComponentView{
	OTUnitValue otObject;
	OTUnitValueViewConfig viewConfig;
	
	public JComponent getComponent(OTObject otObject, boolean editable) {
		this.otObject = (OTUnitValue)otObject;
		
		int precision = 2;
		if(viewConfig != null) {
			precision = viewConfig.getPrecision();
		}
		DecimalFormat nf = (DecimalFormat)NumberFormat.getNumberInstance();
	    nf.setMinimumFractionDigits(precision);
	    nf.setMaximumFractionDigits(precision);
	    
	    return new JLabel(nf.format(this.otObject.getValue()) + " " + this.otObject.getUnit());
	}

	public void viewClosed() {
		// TODO Auto-generated method stub
		
	}

	public void setViewEntry(OTViewEntry viewConfig) {
		this.viewConfig = (OTUnitValueViewConfig) viewConfig;
	}
}
