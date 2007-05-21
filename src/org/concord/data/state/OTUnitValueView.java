package org.concord.data.state;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.concord.framework.otrunk.OTChangeEvent;
import org.concord.framework.otrunk.OTChangeListener;
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
	private OTUnitValue otObject;
	private OTUnitValueViewConfig viewConfig;
	private JComponent newComponent;
	private boolean editable;

	public JComponent getComponent(OTObject otObject, boolean editable) {
		this.otObject = (OTUnitValue)otObject;
		this.editable = editable;
		
		newComponent = new JPanel();
		
		this.otObject.addOTChangeListener(otChangeListener);
	    
		newComponent.add(getUnitValueView());
	    
	    return newComponent;
	}
	
	private JComponent getUnitValueView() {
		int precision = 2;
		if(viewConfig != null) {
			precision = viewConfig.getPrecision();
		}
		DecimalFormat nf = (DecimalFormat)NumberFormat.getNumberInstance();
	    nf.setMinimumFractionDigits(precision);
	    nf.setMaximumFractionDigits(precision);
	    
	    if(editable) {
	    	JTextField tf = 
	    		new JTextField(nf.format(this.otObject.getValue()) + 
	    				" " + this.otObject.getUnit());
	    	tf.setEditable(true);
	    	return tf;
	    } else {
	    	JTextField tf = 
	    		new JTextField(nf.format(this.otObject.getValue()) + 
	    				" " + this.otObject.getUnit());
	    	tf.setEditable(false);
		    return tf;
	    }
	}

	public void viewClosed() {
		otObject.removeOTChangeListener(otChangeListener);
	}

	public void setViewEntry(OTViewEntry viewConfig) {
		this.viewConfig = (OTUnitValueViewConfig) viewConfig;
	}
	
	OTChangeListener otChangeListener = new OTChangeListener() {

		public void stateChanged(OTChangeEvent e) {
			if(e.getProperty() != null && (e.getProperty().equals("value")
					|| e.getProperty().equals("unit"))) {
				newComponent.removeAll();
				newComponent.add(getUnitValueView());
				newComponent.validate();
			}
		}
	};
	
	
	
}
