package org.concord.data.state.filter;

import java.awt.Point;
import java.awt.geom.Point2D;

import org.concord.data.state.AbstractDataStoreFilter;
import org.concord.framework.data.stream.DefaultDataStore;

/**
 * This filter takes an x-y dataset with non-constant x-steps
 * (such as one drawn by hand) and makes the x-steps constant.
 * 
 * It does this by taking the two original points surrounding
 * each new data point and calculating where the new data point
 * should lie. This will decrease the density of data in previously
 * high-densitiy areas, so may, depending on the data, act as a
 * slight low-pass filter.
 * 
 * @author sfentress
 *
 */
public class DataStrictXStepFilter extends AbstractDataStoreFilter
{
	
	private int numSamples;
	private float range;
	private float xStep;
	private int lastIndex = 0;
	private float lastLowX = Float.NEGATIVE_INFINITY;
	private float smallestX;
	private float xValue;
	
	public static String PROP_X_STEP = "set x step";

	public DataStrictXStepFilter()
	{
		super();
	}

	protected void calculateResults()
        throws IllegalArgumentException
    {
	    processData();
	    if (getFilterDescription().getProperty(PROP_X_STEP) != null){
	    	xStep = Float.parseFloat(getFilterDescription().getProperty(PROP_X_STEP));
	    	numSamples = (int) (range / xStep);
	    } else {
	    	xStep = range / numSamples;
	    }
	    
	    outputDataStore = new DefaultDataStore();
	    
	    xValue = smallestX;
	    
	    for (int i = 0; i < numSamples; i++) {
	    	Point2D.Float[] surroundingPoints = getSurroundingPoints(xValue);
	    	Point2D.Float pBefore = surroundingPoints[0];
	    	Point2D.Float pAfter = surroundingPoints[1];
	    	Point2D.Float newValue = getPointOnLine(xValue, pBefore, pAfter);
	        ((DefaultDataStore)outputDataStore).setValueAt(i, 0, new Float(newValue.x));
	        ((DefaultDataStore)outputDataStore).setValueAt(i, 1, new Float(newValue.y));
	        
	        xValue = xValue + xStep;
        }
    }
	
	private Point2D.Float getPointOnLine(float xValue, Point2D.Float before, Point2D.Float after)
    {
	    float xStep = after.x - before.x;
	    float yStep = after.y - before.y;
	    float percentAlong = (xValue - before.x) / xStep;
	    float yValue = before.y + (yStep * percentAlong); 
	    return new Point2D.Float(xValue, yValue);
    }

	private Point2D.Float[] getSurroundingPoints(float xValue){
		Point2D.Float[] p;
		int index = (lastLowX < xValue) ? lastIndex : 0;
		for (int i = index; i < numSamples; i++) {
			float lowX = ((Float)inputDataStore.getValueAt(i, 0)).floatValue();
	        if (lowX <= xValue && !(i == numSamples-1)){
	        	float highX = ((Float)inputDataStore.getValueAt(i+1, 0)).floatValue();
	        	if (highX > xValue){
	        		float lowY = ((Float)inputDataStore.getValueAt(i, 1)).floatValue();
	        		Point2D.Float lowP = new Point2D.Float(lowX, lowY);
	        		float highY = ((Float)inputDataStore.getValueAt(i+1, 1)).floatValue();
	        		Point2D.Float highP = new Point2D.Float(highX, highY);
	        		lastIndex = i;
	        		lastLowX = lowX;
	        		Point2D.Float[] surround = {lowP, highP};
	        		return surround;
	        	}
	        } else {
	        	return null;
	        }
        }
		return null;
	}
	
	private void processData(){
		numSamples = inputDataStore.getTotalNumSamples();
		smallestX = Float.POSITIVE_INFINITY;
		float largestX = Float.NEGATIVE_INFINITY;
		
		for (int i = 0; i < numSamples; i++) {
	        float xValue = ((Float)inputDataStore.getValueAt(i, 0)).floatValue();
	        if (xValue < smallestX){
	        	smallestX = xValue;
	        }
	        if (xValue > largestX){
	        	largestX = xValue;
	        }
        }
		range = largestX - smallestX;
	}
}
