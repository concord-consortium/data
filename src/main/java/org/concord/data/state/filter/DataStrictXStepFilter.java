package org.concord.data.state.filter;

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
 * high-density areas, so may, depending on the data, act as a
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

	@Override
    protected void calculateResults()
        throws IllegalArgumentException
    {
	    try {
            processData();

    	    if (getFilterDescription().getProperty(PROP_X_STEP) != null){
    	    	xStep = Float.parseFloat(getFilterDescription().getProperty(PROP_X_STEP));
    	    	numSamples = (int) (range / xStep)+1;
    	    } else {
    	    	xStep = range / (numSamples-1);
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
        } catch (UnexpectedNullValueException e) {
            // data set is in the middle of a change and has null x or y values
            return;
        }
    }
	
	private Point2D.Float getPointOnLine(float xValue, Point2D.Float before, Point2D.Float after)
    {
	    float dx = after.x - before.x;
	    float dy = after.y - before.y;
	    float slope = dy/dx;
	    // using point-slope form: y = m(x - x1) + y1, we can calculate y
	    float yValue = slope * (xValue - before.x) + before.y;
	    return new Point2D.Float(xValue, yValue);
    }

	private Point2D.Float[] getSurroundingPoints(float xValue) throws UnexpectedNullValueException {
		int index = (lastLowX < xValue) ? lastIndex : 0;
		for (int i = index; i < numSamples; i++) {
			float lowX = getFloatValue(i, 0);
	        if (lowX <= xValue && !(i == numSamples-1)){
	        	float highX = getFloatValue(i+1, 0);
	        	if (highX > xValue){
	        		float lowY = getFloatValue(i, 1);
	        		Point2D.Float lowP = new Point2D.Float(lowX, lowY);
	        		float highY = getFloatValue(i+1, 1);
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
	
	private float getFloatValue(int sample, int channel) throws UnexpectedNullValueException {
	    Float possVal = (Float)inputDataStore.getValueAt(sample, channel);
	    if (possVal == null) {
	        throw new UnexpectedNullValueException();
	    }
	    float val = possVal.floatValue();
	    return val;
	}
	
	private void processData() throws UnexpectedNullValueException {
		numSamples = inputDataStore.getTotalNumSamples();
		smallestX = Float.POSITIVE_INFINITY;
		float largestX = Float.NEGATIVE_INFINITY;
		
		for (int i = 0; i < numSamples; i++) {
	        float xValue = getFloatValue(i, 0);
	        if (xValue < smallestX){
	        	smallestX = xValue;
	        }
	        if (xValue > largestX){
	        	largestX = xValue;
	        }
        }
		range = largestX - smallestX;
	}
	
	class UnexpectedNullValueException extends Exception {
        private static final long serialVersionUID = 1L;
    }
}
