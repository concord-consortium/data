package org.concord.data.state;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

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

public class OTUnitValueView implements OTViewEntryAware, OTJComponentView {
	private OTUnitValue otObject;
	private OTUnitValueViewConfig viewConfig;
	private JComponent newComponent;
	private boolean editable;
	
	private String unitValueFormat = "[0-9]*\\.?[0-9]* +[a-zA-Z][0-9a-zA-Z]*";

	public JComponent getComponent(OTObject otObject, boolean editable) {
		this.otObject = (OTUnitValue)otObject;
		this.editable = editable;
		
		newComponent = new JPanel();
		
		this.otObject.addOTChangeListener(otChangeListener);
	    
		newComponent.add(getUnitValueView());
	    
	    return newComponent;
	}
	
	private JComponent getUnitValueView() {
		final JComponent tf;

		int precision = 2;
		if(viewConfig != null) {
			precision = viewConfig.getPrecision();
		}
		final DecimalFormat nf = (DecimalFormat)NumberFormat.getNumberInstance();
	    nf.setMinimumFractionDigits(precision);
	    nf.setMaximumFractionDigits(precision);
	    
	    if(Float.isNaN(otObject.getValue())) {
	    	if(editable)
	    		tf = new JTextField("NaN");
	    	else
	    		tf = new JLabel("NaN");
	    } else {
	    	if(editable) {
		    	tf = new JTextField(nf.format(otObject.getValue()) + 
						" " + otObject.getUnit());
		    	((JTextField)tf).setColumns(((JTextField)tf).getText().length());
		    	((JTextField)tf).setHorizontalAlignment(JTextField.CENTER);
		    	tf.addMouseListener(new MouseAdapter() {
		    		public void mouseClicked(MouseEvent e) {
		    			tf.requestFocusInWindow();
		    		}
		    		
		    	});
		    	tf.addFocusListener(new FocusAdapter() {

					public void focusLost(FocusEvent e) {
						updateFieldAndValue(e, nf);
					}
		    		
		    	});
	    	}
	    	else
		    	tf = new JLabel(nf.format(otObject.getValue()) + 
						" " + otObject.getUnit());
	    }
	    
    	return tf;
	}
	
	private void updateFieldAndValue(FocusEvent e, DecimalFormat nf) {
		JTextField tf = (JTextField) e.getSource();
		String text = tf.getText().trim();
		
		if(text.matches(unitValueFormat)) {
			System.out.println("matched");
			updateValue(text);
			updateView();
			System.out.println("new value: " + otObject.getValue() + " new Unit: " + otObject.getUnit());
		} else {
			System.out.println("unmatched");
			tf.setText(nf.format(otObject.getValue()) + 
						" " + otObject.getUnit());
		}
	}
	
	private void updateValue(String text) {
		String flt = text.substring(0, text.indexOf(' '));
		String unit = text.substring(text.lastIndexOf(' ')+1);
		otObject.setValue(Float.valueOf(flt).floatValue());
		otObject.setUnit(unit);
		otObject.notifyOTChange();
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
				updateView();
			}
		}
	};
	
	private void updateView() {
		newComponent.removeAll();
		newComponent.add(getUnitValueView());
		newComponent.validate();
	}
}
