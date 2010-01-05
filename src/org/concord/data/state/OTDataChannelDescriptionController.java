package org.concord.data.state;

import java.awt.Color;

import org.concord.data.Unit;
import org.concord.framework.data.stream.DataChannelDescription;
import org.concord.framework.otrunk.DefaultOTController;
import org.concord.framework.otrunk.OTObject;

public class OTDataChannelDescriptionController extends DefaultOTController {

	public static Class[] realObjectClasses = { DataChannelDescription.class };
	public static Class otObjectClass = OTDataChannelDescription.class;

	public void loadRealObject(Object realObject) {
		OTDataChannelDescription otdcd = (OTDataChannelDescription) otObject;
		DataChannelDescription dcd = (DataChannelDescription) realObject;
		
		dcd.setAbsoluteMax(otdcd.getAbsoluteMax());
		dcd.setAbsoluteMin(otdcd.getAbsoluteMin());
		dcd.setNumericData(otdcd.getNumericData());
		dcd.setLocked(otdcd.getLocked());
		dcd.setName(otdcd.getName());
		dcd.setColor(new Color(otdcd.getColor()));
		int precision = otdcd.getPrecision();
		if(precision != Integer.MAX_VALUE) {
			dcd.setPrecision(precision);
		}
		dcd.setRecommendMax(otdcd.getRecommendMax());
		dcd.setRecommendMin(otdcd.getRecommendMin());
		String unitStr = otdcd.getUnit();
		Unit unit = Unit.findUnit(unitStr);
		dcd.setUnit(unit);
		
		dcd.getPossibleValues().addAll(otdcd.getPossibleValues());
	}

	public void registerRealObject(Object realObject) {
	}

	public void saveRealObject(Object realObject) {
	}
	
	public boolean isRealObjectSharable(OTObject otObject, Object realObject) 
	{
		return true;
	}

}
