package org.concord.data.state.filter;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;

import org.concord.data.state.AbstractDataStoreFilter;
import org.concord.framework.data.stream.DataStore;
import org.concord.framework.data.stream.DefaultDataStore;
/**
 * This filter takes multiple x-y datasets with non-constant x-steps
 * (such as ones drawn by hand) and makes the x-steps constant.
 * 
 * It does this by taking the two original points surrounding
 * each new data point and calculating where the new data point
 * should lie. This will decrease the density of data in previously
 * high-density areas, so may, depending on the data, act as a
 * slight low-pass filter.
 * 
 * This filter is based on the DataStrictXStepFilter, modified to
 * support combining N data stores of 2 channels into one
 * datastore of N+1 channels.
 * 
 * @author aunger
 *
 */
public class DataStrictXStepMultiStoreFilter extends AbstractDataStoreFilter {
    private ArrayList<DataStore> inputDataStores = new ArrayList<DataStore>();
    private float smallestX;
    private float largestX;
    
    private int numSamples;
    private float range;
    private float xStep;
    
    // private int lastIndex = 0;
    // private float lastLowX = Float.NEGATIVE_INFINITY;
    private HashMap<DataStore, Integer> lastIndexes = new HashMap<DataStore, Integer>();
    private HashMap<DataStore, Float> lastLowXs = new HashMap<DataStore, Float>();
    
    public static String PROP_X_STEP = "set x step";

    @Override
    protected void calculateResults() throws IllegalArgumentException {
        // System.out.println("Recalculating results...");
        try {
            findRange();

            if (getFilterDescription().getProperty(PROP_X_STEP) != null){
                xStep = Float.parseFloat(getFilterDescription().getProperty(PROP_X_STEP));
                numSamples = (int) (range / xStep)+1;
            } else {
                xStep = range / (numSamples-1);
            }
            
            outputDataStore = new DefaultDataStore();
            
            float xValue = smallestX;
            
            for (int sampleNum = 0; sampleNum < numSamples; sampleNum++) {
                // String formatStr = "Set: %1.4f";
                // ArrayList<Object> args = new ArrayList<Object>();
                // args.add(xValue);
                
                ((DefaultDataStore)outputDataStore).setValueAt(sampleNum, 0, xValue);
                
                for (int dataStoreNum = 0; dataStoreNum < inputDataStores.size(); dataStoreNum++) {
                    DataStore store = inputDataStores.get(dataStoreNum);
                    Point2D.Float[] surroundingPoints = getSurroundingPoints(store, xValue);
                    Float newY;
                    if (surroundingPoints == null || surroundingPoints[0] == null || surroundingPoints[1] == null) {
                        newY = null;
                    } else {
                        Point2D.Float pBefore = surroundingPoints[0];
                        Point2D.Float pAfter = surroundingPoints[1];
                        Point2D.Float newValue = getPointOnLine(xValue, pBefore, pAfter);
                        newY = new Float(newValue.y);
                    }
                    // formatStr += " %1.4f";
                    // args.add(newY);
                    ((DefaultDataStore)outputDataStore).setValueAt(sampleNum, dataStoreNum+1, newY);
                }
                // System.out.println(String.format(formatStr, args.toArray()));
                
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

    private Point2D.Float[] getSurroundingPoints(DataStore store, float xValue) {
        int lastIndex = lastIndexes.containsKey(store) ? lastIndexes.get(store) : 0;
        float lastLowX = lastLowXs.containsKey(store) ? lastLowXs.get(store) : Float.NEGATIVE_INFINITY;
        int index = (lastLowX < xValue) ? lastIndex : 0;
        for (int i = index; i < numSamples; i++) {
            try {
                float lowX = getFloatValue(store, i, 0);
                if (lowX <= xValue && !(i == numSamples-1)){
                    float highX = getFloatValue(store, i+1, 0);
                    if (highX > xValue){
                        float lowY = getFloatValue(store, i, 1);
                        Point2D.Float lowP = new Point2D.Float(lowX, lowY);
                        float highY = getFloatValue(store, i+1, 1);
                        Point2D.Float highP = new Point2D.Float(highX, highY);
                        lastIndexes.put(store, i);
                        lastLowXs.put(store, lowX);
                        Point2D.Float[] surround = {lowP, highP};
                        return surround;
                    }
                } else {
                    return null;
                }
            } catch (UnexpectedNullValueException e) {
                return null;
            }
        }
        return null;
    }
    
    private float getFloatValue(DataStore store, int sample, int channel) throws UnexpectedNullValueException {
        Float possVal = (Float)store.getValueAt(sample, channel);
        if (possVal == null) {
            throw new UnexpectedNullValueException();
        }
        float val = possVal.floatValue();
        return val;
    }
    
    private void findRange() throws UnexpectedNullValueException {
        smallestX = Float.POSITIVE_INFINITY;
        largestX = Float.NEGATIVE_INFINITY;
        for (DataStore dataStore : inputDataStores) {
            int samples = dataStore.getTotalNumSamples();

            for (int i = 0; i < samples; i++) {
                float xValue = getFloatValue(dataStore, i, 0);
                if (xValue < smallestX){
                    smallestX = xValue;
                }
                if (xValue > largestX){
                    largestX = xValue;
                }
            }
        }
        range = largestX - smallestX;
    }
    
    class UnexpectedNullValueException extends Exception {
        private static final long serialVersionUID = 1L;
    }
    
    @Override
    public void setInputDataStore(DataStore ds) {
        addInputDataStore(ds);
    }
    
    public void addInputDataStore(DataStore ds) {
        this.inputDataStores.add(ds);
        calculateResults();
    }

    public void recalculate() {
        calculateResults();
    }

}
