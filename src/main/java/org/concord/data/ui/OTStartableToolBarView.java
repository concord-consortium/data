package org.concord.data.ui;

import javax.swing.JComponent;

import org.concord.framework.otrunk.OTControllerService;
import org.concord.framework.otrunk.OTObject;
import org.concord.framework.otrunk.view.AbstractOTJComponentView;
import org.concord.framework.startable.Startable;

public class OTStartableToolBarView extends AbstractOTJComponentView {

	
	private OTStartableToolBar otToolBar;
	private OTControllerService controllerService;

	public JComponent getComponent(OTObject otObject) {
		otToolBar = (OTStartableToolBar) otObject;
		controllerService = createControllerService(otObject);
		Startable startable = 
			(Startable) controllerService.getRealObject(otToolBar.getStartable());
		StartableToolBar toolBar = new StartableToolBar();
		toolBar.setStartable(startable);
		return toolBar;
	}

}
