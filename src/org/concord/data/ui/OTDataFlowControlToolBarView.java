package org.concord.data.ui;

import javax.swing.JComponent;

import org.concord.framework.data.DataFlow;
import org.concord.framework.otrunk.OTControllerService;
import org.concord.framework.otrunk.OTObject;
import org.concord.framework.otrunk.view.OTControllerServiceFactory;
import org.concord.framework.otrunk.view.OTJComponentView;
import org.concord.framework.otrunk.view.OTViewContext;
import org.concord.framework.otrunk.view.OTViewContextAware;

public class OTDataFlowControlToolBarView extends DataFlowControlToolBar
    implements OTJComponentView, OTViewContextAware
{

	private OTViewContext viewContext;
	private OTDataFlowControlToolBar otToolBar;

	public JComponent getComponent(OTObject otObject)
	{
		otToolBar = (OTDataFlowControlToolBar) otObject;
		OTControllerService cs = createControllerService();
		DataFlow dataFlowObj = (DataFlow) cs.getRealObject(otToolBar.getDataProducer());
		addDataFlowObject(dataFlowObj);
		return this;
	}

	public void viewClosed()
	{
		// TODO Auto-generated method stub

	}
	
	public OTControllerService createControllerService()
	{
		OTControllerServiceFactory controllerServiceFactory = 
			(OTControllerServiceFactory) viewContext.getViewService(OTControllerServiceFactory.class);
		
    	OTControllerService controllerService = 
    		controllerServiceFactory.createControllerService(otToolBar.getOTObjectService());
    	controllerService.addService(OTViewContext.class, viewContext);

    	return controllerService;
	}

	public void setViewContext(OTViewContext viewContext)
    {
	    this.viewContext = viewContext;
    }

}
