package org.concord.data.state;

import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JPanel;

import org.concord.data.stream.LazySyncProducerDataStore;
import org.concord.data.stream.LazySyncProducerDataStore.DataProducerAndDataStoreProvider;
import org.concord.data.ui.DataStoreLabel;
import org.concord.data.ui.StartableToolBar;
import org.concord.framework.data.stream.DataProducer;
import org.concord.framework.data.stream.DataStore;
import org.concord.framework.otrunk.OTControllerService;
import org.concord.framework.otrunk.OTObject;
import org.concord.framework.otrunk.view.AbstractOTJComponentView;
import org.concord.framework.startable.AbstractStartable;
import org.concord.framework.startable.Startable;

public class OTDigitalDisplayView extends AbstractOTJComponentView {
	LazySyncProducerDataStore syncer = new LazySyncProducerDataStore(
			new DataProducerAndDataStoreProvider() {
		
		public DataStore getDataStore() {
			return OTDigitalDisplayView.this.getDataStore();
		}
		
		public DataProducer getDataProducer() {
			return OTDigitalDisplayView.this.getDataProducer();
		}
	});
	
	Startable startable = new AbstractStartable() {
		
		public void stop() {
			getDataProducer().stop();
		}
		
		public void start() {
			getDataProducer().start();
		}
		
		public void reset() {
			getDataStore().clearValues();
			getDataProducer().reset();
		}
		
		public boolean isRunning() {
			return getDataProducer().isRunning();
		}
	};
	
	private OTControllerService controllerService;
	private OTDigitalDisplay otDigitalDisplay;
	
	public JComponent getComponent(OTObject otObject) {
		otDigitalDisplay = (OTDigitalDisplay)otObject;
    	controllerService = createControllerService(otObject);
    	
		OTDataStore otDataStore = otDigitalDisplay.getDataStore();
		DataStore dataStore = (DataStore) controllerService.getRealObject(otDataStore);
		
		DataStoreLabel dataStoreLabel = new DataStoreLabel(dataStore, getRealChannel());
		
		dataStoreLabel.setFontSize(otDigitalDisplay.getFontSize());
		dataStoreLabel.setMaximumSize(dataStoreLabel.getPreferredSize());
		
		syncer.init();
		
		JPanel digitalDisplayPanel = new JPanel();
		digitalDisplayPanel.setLayout(new BoxLayout(digitalDisplayPanel, BoxLayout.Y_AXIS));
		digitalDisplayPanel.add(dataStoreLabel);

		StartableToolBar toolBar = new StartableToolBar();
		toolBar.setStartable(startable);
		toolBar.setFloatable(false);
		digitalDisplayPanel.add(toolBar);

		return digitalDisplayPanel;
	}

	private int getRealChannel() {
		// TODO this should figure out the correct channel using the usual (complicated)
		// virtual channel logic
		return 0;
	}
	
	private DataProducer getDataProducer(){
		return (DataProducer)controllerService.getRealObject(otDigitalDisplay.getDataProducer());
	}
	
	private DataStore getDataStore(){
		return (DataStore)controllerService.getRealObject(otDigitalDisplay.getDataStore());
	}
	
	@Override
	public void viewClosed() {
		super.viewClosed();
		syncer.dispose();
	}

}
