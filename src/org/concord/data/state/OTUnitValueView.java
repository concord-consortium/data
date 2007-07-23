package org.concord.data.state;

import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import javax.swing.JComponent;
import javax.swing.JLabel;
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

/*
 * Last modification information:
 * $Revision: 1.9 $
 * $Date: 2007-07-23 15:24:03 $
 * $Author: scytacki $
 *
 * Licence Information
 * Copyright 2004 The Concord Consortium 
*/

public class OTUnitValueView implements OTViewEntryAware, OTJComponentView {
	private OTUnitValue otObject;
	private OTUnitValueViewConfig viewConfig;
	private JComponent newComponent;
	private boolean editable;
	
	// This looks like it was used before but not anymore
	// I'm keeping it here because it probably took quite a bit of twiddling to get right.
	// private String unitValueFormat = "\\d+(\\.?\\d*){0,1}( +\\w*){0,1}";
	private DecimalFormat nf;
	
	private String originalText;

	public JComponent getComponent(OTObject otObject, boolean editable) {
		this.otObject = (OTUnitValue)otObject;
		this.editable = editable;
		
		newComponent = new JPanel();
		
		this.otObject.addOTChangeListener(otChangeListener);
	    
		newComponent.add(getUnitValueView());
		newComponent.setOpaque(false);
	    
	    return newComponent;
	}
	
	private JComponent getUnitValueView() {
		final JComponent tf;

		int precision = 2;
		if(viewConfig != null) {
			precision = viewConfig.getPrecision();
			editable = viewConfig.getEditable();
		}
		nf = (DecimalFormat)NumberFormat.getNumberInstance();
	    nf.setMinimumFractionDigits(precision);
	    nf.setMaximumFractionDigits(precision);
	    
	    if(Float.isNaN(otObject.getValue())) {
	    	if(editable) {
	    		tf = new JTextField("NaN");
	    		addListeners((JTextField)tf);
	    	}
	    	else
	    		tf = new JLabel("NaN");
	    } else {
	    	if(editable) {
		    	tf = new JTextField(nf.format(otObject.getValue()) + 
						" " + otObject.getUnit());
		    	((JTextField)tf).setColumns(((JTextField)tf).getText().length());
		    	((JTextField)tf).setHorizontalAlignment(JTextField.CENTER);
	    		addListeners((JTextField)tf);
	    	}
	    	else
		    	tf = new JLabel(nf.format(otObject.getValue()) + 
						" " + otObject.getUnit());
	    }
	    
	    if(tf instanceof JTextField) originalText = ((JTextField)tf).getText();
	    
    	return tf;
	}
	
	private void addListeners(final JTextField tf) {
		//mouselistener: listen to mouse click
    	tf.addMouseListener(new MouseAdapter() {
    		public void mouseClicked(MouseEvent e) {
    			tf.requestFocusInWindow();
    		}
    		
    	});

    	//focus listener: listen to focus lost
    	tf.addFocusListener(new FocusAdapter() {

			public void focusLost(FocusEvent e) {
				updateFieldAndValue(tf);
			}
    		
    	});
    	
    	//key listener: listen to Enter press
    	tf.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode()==KeyEvent.VK_ENTER) {
					updateFieldAndValue(tf);
				}
			}

    	});
	}
	
	private void updateFieldAndValue(Object obj) {
		JTextField tf = (JTextField) obj;
		String text = tf.getText().trim();

		UnitValueEvaluator uve = new UnitValueEvaluator(text);
		if(uve.isValidUnitValue()) {
			otObject.setValue(uve.getValue());
			otObject.setUnit(uve.getUnit());
			
			// This shouldn't be necessary because just setting values in the otObject will through
			// an event
			otObject.notifyOTChange(null, null, null);
		} else {
			tf.setText(originalText);
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
