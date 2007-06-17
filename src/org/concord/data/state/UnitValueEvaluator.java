package org.concord.data.state;

/**
 * UnitValueEvaluator
 * Class name and description
 *
 * Date created: Jun 17, 2007
 *
 * @author Shengyao Wang<p>
 */
/*
 * Last modification information:
 * $Revision: 1.1 $
 * $Date: 2007-06-17 11:56:12 $
 * $Author: swang $
 *
 * Licence Information
 * Copyright 2004 The Concord Consortium 
 */

public class UnitValueEvaluator {
	private float value;
	private String unit;
	private String unitValue;
	boolean isValidUnitValue = false;
	
	private String unitValueFormat = "\\d+(\\.?\\d*){0,1}(\\s+\\D(\\w*[/^-]*\\w*)*){0,1}";
	
	public UnitValueEvaluator(String unitValue) {
		if(unitValue == null || unitValue.trim().length() == 0) {
			return;
		}
		this.unitValue = unitValue.trim();
		evaluate();
	}
	
	private void evaluate() {
		if(unitValue.matches(unitValueFormat)) {
			isValidUnitValue = true;

			int index = unitValue.indexOf(' ');
			if(index == -1) {
				value = Float.parseFloat(unitValue);
				unit = "";
			} else {
				String flt = unitValue.substring(0, index);
				unit = unitValue.substring(unitValue.lastIndexOf(' ')+1);
				value = (Float.parseFloat(flt));
			}
		}
	}
	
	public boolean isValidUnitValue() {
		return isValidUnitValue;
	}
	
	public float getValue() {
		if(!isValidUnitValue) throw new NumberFormatException();
		return value;
	}
	
	public String getUnit() {
		if(!isValidUnitValue) throw new NumberFormatException();
		return unit;
	}
	
	public static void main(String[] args) {
		UnitValueEvaluator uve = new UnitValueEvaluator("122ee  ");
		System.out.println(uve.getValue() + ":" + uve.getUnit());
	}
}
