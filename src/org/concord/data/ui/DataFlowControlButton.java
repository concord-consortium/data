/*
 * Last modification information:
 * $Revision: 1.2 $
 * $Date: 2004-09-22 17:53:41 $
 * $Author: imoncada $
 *
 * Licence Information
 * Copyright 2004 The Concord Consortium 
*/
package org.concord.data.ui;

import javax.swing.Action;
import javax.swing.JButton;

import org.concord.framework.data.DataFlow;


/**
 * DataFlowControlButton
 * Button that controls one or more data flow or
 * simulation objects
 * It can start, stop or reset the data flow or
 * simulation objects
 *
 * Date created: Sep 20, 2004
 *
 * @author imoncada<p>
 *
 */
public class DataFlowControlButton extends JButton
{
	/**
	 * Default constructor
	 * Creates an empty button that doesn't do anything
	 */
	public DataFlowControlButton()
	{
		//Empty button
		this(DataFlowControlAction.FLOW_CONTROL_NONE);
	}
	
	/**
	 * Creates a button that controls DataFlow objects
	 * The type specified corresponds to the action that the button
	 * will have.
	 * For example, a button with a FLOW_CONTROL_START type,
	 * will start the data flow objects or simulation objects 
	 * @param type One of the types defined in DataFlowControlAction
	 */
	public DataFlowControlButton(int type)
	{
		super();
		DataFlowControlAction action = new DataFlowControlAction(type);
		setAction(action);
	}
	
	/**
	 * Creates a button that will control the object objFlow.
	 * The type specified corresponds to the action that the button
	 * will have.
	 * For example, a button with a FLOW_CONTROL_START type,
	 * will start the object objFlow. 
	 * @param objFlow	The data flow or simulation object to control
	 * @param type 		One of the types defined in DataFlowControlAction
	 */
	public DataFlowControlButton(DataFlow objFlow, int type)
	{
		this(type);
		addDataFlowObject(objFlow);
	}
	
	/**
	 * Creates a button based on an existing DataFlowControlAction action
	 * @param action
	 */
	public DataFlowControlButton(DataFlowControlAction action)
	{
		super(action);
	}
	
	/**
	 * Adds a DataFlow object that will be controlled by this button
	 * @param objFlow The data flow or simulation object to control
	 */
	public void addDataFlowObject(DataFlow objFlow)
	{
		getDataFlowControlAction().addDataFlowObject(objFlow);
	}

	/**
	 * Removes a DataFlow object that was previously controlled by this button
	 * @param objFlow The data flow or simulation object to remove and not be controlled anymore
	 */
	public void removeDataFlowObject(DataFlow objFlow)
	{
		getDataFlowControlAction().removeDataFlowObject(objFlow);
	}

	/**
	 * Gets the type of action (start, stop, run)
	 * @return Returns the flowControlType.
	 */
	public int getFlowControlType()
	{
		return getDataFlowControlAction().getFlowControlType();
	}
	/**
	 * Sets the type of action (start, stop, run)
	 * @param flowControlType The flowControlType to set.
	 * @throws IllegalArgumentException if the type is not one of the
	 * valid types defined in DataFlowControlAction
	 */
	public void setFlowControlType(int type)
	{
		getDataFlowControlAction().setFlowControlType(type);
	}

	/**
	 * @return Returns the autoEnable.
	 */
	public boolean isAutoEnable()
	{
		return getDataFlowControlAction().isAutoEnable();
	}
	
	/**
	 * Sets if the action will be enabled/disabled automatically
	 * depending on the simulation state of the simulation objects
	 * @param autoEnable The autoEnable to set.
	 */
	public void setAutoEnable(boolean autoEnable)
	{
		getDataFlowControlAction().setAutoEnable(autoEnable);
	}
	
	public void addAction(DataFlowControlAction action)
	{
		
	}
	
	/**
	 * Only DataFlowControlAction objects can be actions for this button
	 * @see javax.swing.AbstractButton#setAction(javax.swing.Action)
	 * @throws IllegalArgumentException if the action is not a DataFlowControlAction object
	 */
	public void setAction(Action a)
	{
		if (a == null){
			throw new IllegalArgumentException("Action cannot be set to null.");
		}
		
		//Only allow DataFlowControlAction objects
		if (!(a instanceof DataFlowControlAction)){
			throw new IllegalArgumentException("Wrong Action class:" + a.getClass().getName() +
					".The action for a DataFlowControlButton has to be a DataFlowControlAction.");
		}
		
		setDataFlowControlAction((DataFlowControlAction)a);
	}
	
	/**
	 * Returns the DataFlowControlAction associated with this button
	 * Guaranteed to be non-null
	 */
	public DataFlowControlAction getDataFlowControlAction()
	{
		return (DataFlowControlAction)getAction();
	}
	
	/**
	 * Returns the DataFlowControlAction associated with this button
	 * Guaranteed to be non-null
	 */
	public void setDataFlowControlAction(DataFlowControlAction action)
	{
		super.setAction(action);
	}
}
