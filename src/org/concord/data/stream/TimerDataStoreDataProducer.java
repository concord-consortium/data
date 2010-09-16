package org.concord.data.stream;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

import org.concord.data.state.DefaultDataStoreFilterDescription;
import org.concord.data.state.filter.DataStrictXStepFilter;
import org.concord.data.state.filter.DataStrictXStepMultiStoreFilter;
import org.concord.framework.data.stream.DataStore;
import org.concord.framework.data.stream.DefaultMultipleDataProducer;
import org.concord.framework.startable.StartableInfo;

public class TimerDataStoreDataProducer extends DefaultMultipleDataProducer implements ActionListener
{
	private float lastT = Float.MIN_VALUE;
	private int currentIndex = 0;
	private float currentT;
	private float[] currentValue;
	protected Timer timer = null;
	private DataStrictXStepMultiStoreFilter dataStoreFilter;
	
	private DataStore filteredDataStore;
    private boolean endOfStream;
    private StartableInfo info;
    
    private boolean inInitialState = true;
	
	public void addDataStore(DataStore dataStore){
	    DataStrictXStepMultiStoreFilter strictXFilter = getStrictXFilter();
	    strictXFilter.addInputDataStore(dataStore);
	    filteredDataStore = strictXFilter.getResultDataStore();
	}
	
	private DataStrictXStepMultiStoreFilter getStrictXFilter() {
	    if (dataStoreFilter == null) {
    	    dataStoreFilter = new DataStrictXStepMultiStoreFilter();
            DefaultDataStoreFilterDescription desc = new DefaultDataStoreFilterDescription("");
            desc.setProperty(DataStrictXStepFilter.PROP_X_STEP, Float.toString(getDataDescription().getDt()));
            dataStoreFilter.setFilterDescription(desc);
	    }
	    return dataStoreFilter;
	}
	
	public void recalculateDataStores() {
	    DataStrictXStepMultiStoreFilter strictXFilter = getStrictXFilter();
	    strictXFilter.recalculate();
	    filteredDataStore = dataStoreFilter.getResultDataStore();
	}
	
	@Override
	public boolean isInInitialState() {
	    return inInitialState;
	}
	
	@Override
    public void start()
	{
	    // recalculate the filtered data in case the original datastore changed
	    recalculateDataStores();
	    
		if(timer == null) {
	//		float dt = getDataDescription().getDt();
	//		int tt = (int)((1000f * dt) * getTimeScale());	
		    calculateCurrentValues();
			timer = new Timer((int)(1000*currentT), this);
		}
		timer.start();
		inInitialState = false;
		super.start();
	}
	
	@Override
    public void reset()
	{
		if (isRunning()) {
		    stop();
		}
		timer = null;
		currentIndex = 0;
		endOfStream = false;
		inInitialState = true;
		super.reset();
	}
	
	@Override
    public void stop()
	{
		if(timer == null) return;
		timer.stop();
		super.stop();
	}
	
	private float getFloatValue(int sample, int channel) {
        Float possVal = (Float)filteredDataStore.getValueAt(sample, channel);
        if (possVal == null) {
            return Float.NaN;
        }
        float val = possVal.floatValue();
        return val;
    }
	
	private void calculateCurrentValues() {
	    currentT = getFloatValue(currentIndex, 0);
	    currentValue = new float[filteredDataStore.getTotalNumChannels()];
	    for (int i = 0; i < filteredDataStore.getTotalNumChannels(); i++) {
	        currentValue[i] = getFloatValue(currentIndex, i);
	    }
	}
	
	public void actionPerformed(ActionEvent e)
	{		
		addValues(currentValue);
		lastT = currentT;
		currentIndex++;
		if (currentIndex > filteredDataStore.getTotalNumSamples()-1){
		    endOfStream = true;
			stop();
			return;
		}
		calculateCurrentValues();
		timer.setDelay((int) (1000*(currentT-lastT)));
	}

	@Override
    public boolean isAtEndOfStream() {
        return this.endOfStream;
    }
    
    @Override
    public StartableInfo getStartableInfo() {
        if (info == null) {
            info = new StartableInfo();
            info.sendsEvents = true;
            info.canResetWhileRunning = false;
            info.canRestartWithoutReset = true;
            
            info.startVerb = "Play";
            info.stopVerb = "Pause";
            info.resetVerb = "Reset";
        }
        return info;
    }

}
