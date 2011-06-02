package org.concord.data.state.filter;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.concord.data.state.AbstractDataStoreFilter;
import org.concord.framework.data.stream.DataStore;
import org.concord.framework.data.stream.DefaultDataStore;
/**
 * This filter takes multiple x-y datasets with non-constant x-steps
 * (such as ones drawn by hand) and makes the x-steps constant.
 * 
 * It does this by taking the two original points surrounding
 * each new data point and calculating where the new data point
 * should lie.
 * 
 * If keepExistingXValues is false, then this will decrease the density of data in previously
 * high-density areas, so may, depending on the data, act as a
 * slight low-pass filter. It can also act as a slight smoothing filter, when peaks or valleys
 * fall between 2 calculated x-values.
 * 
 * This filter is based on the DataStrictXStepFilter, modified to
 * support combining N data stores of 2 channels into one
 * datastore of N+1 channels.
 * 
 * @author aunger
 *
 */
public class DataStrictXStepMultiStoreFilter extends AbstractDataStoreFilter {
    private static final Logger logger = Logger.getLogger(DataStrictXStepMultiStoreFilter.class.getName());
    private ArrayList<DataStore> inputDataStores = new ArrayList<DataStore>();
    
    private float xStep;
    
    private boolean startAtZero = false;
    private boolean keepExistingXValues = false;
    
    private HashMap<DataStore, Integer> lastIndexes = new HashMap<DataStore, Integer>();
    private HashMap<DataStore, Float> lastLowXs = new HashMap<DataStore, Float>();

    private boolean calculateResultsNotificationsOn = true;
    
    public static String PROP_X_STEP = "set x step";

    @Override
    protected void calculateResults() throws IllegalArgumentException {
        try {
            ArrayList<Float> xValues = findXValues();

            outputDataStore = new DefaultDataStore();
            
            for (int sampleNum = 0; sampleNum < xValues.size(); sampleNum++) {
                // String formatStr = "Set: %1.4f";
                // ArrayList<Object> args = new ArrayList<Object>();
                // args.add(xValue);
                float xValue = xValues.get(sampleNum);
                
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
            }
            if (calculateResultsNotificationsOn) {
                notifyCalculateResultsFired();
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
        for (int i = index; i < store.getTotalNumSamples(); i++) {
            try {
                float lowX = getFloatValue(store, i, 0);
                if (lowX <= xValue && !(i == store.getTotalNumSamples()-1)){
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
    
    private ArrayList<Float> findXValues() throws UnexpectedNullValueException {
        ArrayList<Float> actualXValues = new ArrayList<Float>();
        
        for (DataStore dataStore : inputDataStores) {
            int samples = dataStore.getTotalNumSamples();

            for (int i = 0; i < samples; i++) {
                float xValue = getFloatValue(dataStore, i, 0);
                actualXValues.add(xValue);
            }
        }
        Collections.sort(actualXValues);
        if (actualXValues.size() == 0) {
            return new ArrayList<Float>();
        }
        float smallestX = actualXValues.get(0).floatValue();
        if (startAtZero && smallestX > 0) {
            smallestX = 0;
        }
        return fillXValues(smallestX, actualXValues);
    }
    
    private ArrayList<Float> fillXValues(float startX, ArrayList<Float> originalPoints) {
        ArrayList<Float> finalXValues = new ArrayList<Float>();
        float lastX = originalPoints.get(originalPoints.size()-1);
        if (getFilterDescription().getProperty(PROP_X_STEP) != null){
            xStep = Float.parseFloat(getFilterDescription().getProperty(PROP_X_STEP));
        } else {
            xStep = 0.1f;
        }
        
        if (keepExistingXValues) {
            float currentX = startX;
            for (float nextX : originalPoints) {
                while (currentX < nextX) {
                    finalXValues.add(currentX);
                    currentX += xStep;
                }
                currentX = nextX;
            }
        } else {
            float currentX = startX;
            while (currentX < lastX) {
                finalXValues.add(currentX);
                currentX += xStep;
            }
        }
        
        return finalXValues;
    }
    
    class UnexpectedNullValueException extends Exception {
        private static final long serialVersionUID = 1L;
    }
    
    @Override
    public void setInputDataStore(DataStore ds) {
        addInputDataStore(ds);
    }
    
    public void addInputDataStore(DataStore ds) {
        if (ds == null) { return; }
        this.inputDataStores.add(ds);
        ds.addDataStoreListener(this);
        calculateResults();
    }

    public void recalculate() {
        calculateResultsNotificationsOn = false;
        calculateResults();
        calculateResultsNotificationsOn = true;
    }

    public void setStartAtZero(boolean startAtZero) {
        this.startAtZero = startAtZero;
    }

    public void setKeepExistingXValues(boolean keepExistingXValues) {
        this.keepExistingXValues = keepExistingXValues;
    }
    
    // calculate results notification listeners
    private ArrayList<DataStrictXStepFilterListener> listeners = new ArrayList<DataStrictXStepFilterListener>();
    
    private void notifyCalculateResultsFired() {
        for (DataStrictXStepFilterListener listener : listeners) {
            try {
                listener.calculateResultsFired();
            } catch (Exception e) {
                logger.log(Level.SEVERE, "Failed to notify listener.", e);
            }
        }
    }

    public void addDataStrictXStepFilterListener(DataStrictXStepFilterListener listener) {
        if (! listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    public void removeDataStrictXStepFilterListener(DataStrictXStepFilterListener listener) {
        listeners.remove(listener);
    }
}
