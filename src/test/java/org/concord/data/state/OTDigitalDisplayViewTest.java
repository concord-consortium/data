package org.concord.data.state;

import junit.framework.TestCase;

import org.concord.otrunk.test.OtmlTestHelper;


public class OTDigitalDisplayViewTest extends TestCase 
{
	public void testDigitalDisplay() throws Exception
	{
		OtmlTestHelper helper = new OtmlTestHelper();
		helper.initOtrunk(getClass().getResource("digital-display.otml"));
		OTDigitalDisplay otDigitalDisplay = (OTDigitalDisplay) helper.getObject("digital_display");
		OTDigitalDisplayView view = (OTDigitalDisplayView) helper.getView(otDigitalDisplay);
		
		assertEquals("label starts out with no value string", 
				"-----", view.dataStoreLabel.getText());

		view.startable.start();
		Thread.sleep(500);
		view.startable.stop();
		assertTrue("label has units at the end after starting and stoping", 
				view.dataStoreLabel.getText().endsWith("m"));
		
		view.startable.reset();
		assertEquals("label shows no value string after reset", 
				"-----", view.dataStoreLabel.getText());
	}
	
	public void testDigitalDisplayChangingDataTypes() throws Exception
	{
		{
			OtmlTestHelper helper = new OtmlTestHelper();
			helper.initOtrunk(getClass().getResource("digital-display-changing-data-types.otml"));
			OTDigitalDisplay otDigitalDisplay = (OTDigitalDisplay) helper.getObject("digital_display");
			OTDigitalDisplayView view = (OTDigitalDisplayView) helper.getView(otDigitalDisplay);
			
			assertEquals("label starts out with last value", 
					"29.0 degC", view.dataStoreLabel.getText());

			view.startable.start();
			Thread.sleep(500);
			view.startable.stop();
			assertTrue("label has units at the end after starting and stoping", 
					view.dataStoreLabel.getText().endsWith("m"));
			
			view.startable.reset();
			assertEquals("label shows no value string after reset", 
					"-----", view.dataStoreLabel.getText());
		}
		
	}
}
