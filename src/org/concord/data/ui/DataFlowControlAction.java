

/*
 *  Copyright (C) 2004  The Concord Consortium, Inc.,
 *  10 Concord Crossing, Concord, MA 01741
 *
 *  Web Site: http://www.concord.org
 *  Email: info@concord.org
 *
 *  This library is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public
 *  License as published by the Free Software Foundation; either
 *  version 2.1 of the License, or (at your option) any later version.
 *
 *  This library is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *  Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public
 *  License along with this library; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 */
/*
 * Last modification information:
 * $Revision: 1.5 $
 * $Date: 2005-01-15 15:58:18 $
 * $Author: scytacki $
 *
 * Licence Information
 * Copyright 2004 The Concord Consortium 
*/
package org.concord.data.ui;

import java.awt.event.ActionEvent;
import java.util.Vector;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;

import org.concord.framework.data.DataFlow;
import org.concord.framework.data.DataFlowCapabilities;
import org.concord.framework.simulation.Simulation;
import org.concord.framework.simulation.SimulationEvent;
import org.concord.framework.simulation.SimulationListener;


/**
 * DataFlowControlAction
 * Action that controls one or more data flow or
 * simulation objects
 * It can start, stop or reset the data flow or
 * simulation objects
 * It gets enabled and disabled automatically 
 * if desired.
 *
 * Date created: Sep 20, 2004
 *
 * @author imoncada<p>
 *
 */
public class DataFlowControlAction extends AbstractAction
	implements SimulationListener
{
	protected Vector objsFlow;				//DataFlow objects
	protected int flowControlType = -1;
	protected boolean autoEnable = true;
	
	/**
	 * Doesn't do anything to the data flow or simulation objects
	 */
	public final static int FLOW_CONTROL_NONE = 0;
	
	/**
	 * Indicates to START the data flow or simulation objects
	 */	
	public final static int FLOW_CONTROL_START = 1;

	/**
	 * Indicates to STOP the data flow or simulation objects
	 */	
	public final static int FLOW_CONTROL_STOP = 2;

	/**
	 * Indicates to RESET the data flow or simulation objects
	 */	
	public final static int FLOW_CONTROL_RESET = 3;
	
	/**
	 * Indicates to start or stop the simulation objects,
	 * depending on the current simulation state (Starts them if they 
	 * are stopped or reset, and stops them if they are already running)
	 * This type works for Simulation objects only
	 */		
	public final static int FLOW_CONTROL_START_STOP = 4;

	/**
	 * Creates an action that controls DataFlow objects,
	 * according to the type specified.
	 * @param type 	One of the following types:
	 * 	FLOW_CONTROL_START		to start the data flow or the simulation 
	 * 	FLOW_CONTROL_STOP		to stop the data flow or the simulation 
	 * 	FLOW_CONTROL_RESET		to reset the data flow or the simulation 
	 * 	FLOW_CONTROL_START_STOP	to start or stop the data flow or the simulation 
	 */	
	public DataFlowControlAction(int type)
	{
		super();
		setEnabled(false);
		objsFlow = new Vector();
		setFlowControlType(type);
		setDefaultProperties(type);
	}
	
	/**
	 * Creates an action that controls the object flowObject,
	 * according to the type specified.
	 * @param flowObject	The data flow or simulation object to control
	 * @param type 	One of the following types:
	 * 	FLOW_CONTROL_START		to start the data flow or the simulation 
	 * 	FLOW_CONTROL_STOP		to stop the data flow or the simulation 
	 * 	FLOW_CONTROL_RESET		to reset the data flow or the simulation 
	 * 	FLOW_CONTROL_START_STOP	to start or stop the data flow or the simulation 
	 */
	public DataFlowControlAction(DataFlow flowObject, int type)
	{
		this(type);
		addDataFlowObject(flowObject);
	}
	
	protected void setDefaultProperties(int type)
	{
		if (type == FLOW_CONTROL_NONE){
			setName("None");
		}
		else if (type == FLOW_CONTROL_START){
			setName("Start");
			setDescription("Start the simulation");
		}
		else if (type == FLOW_CONTROL_STOP){
			setName("Stop");
			setDescription("Stop the simulation");
		}
		else if (type == FLOW_CONTROL_RESET){
			setName("Reset");
			setDescription("Reset the simulation");
		}
		else if (type == FLOW_CONTROL_START_STOP){
			setName("Start/Stop");
			setDescription("Start or Stop the simulation");
		}
		putValue(Action.ACTION_COMMAND_KEY, String.valueOf(type));
	}
	
	/**
	 * Sets the long description of the action
	 * (Equivalent to the Action.SHORT_DESCRIPTION property)
	 * @param desc 	long description for the action
	 */
	public void setDescription(String desc)
	{
		putValue(Action.SHORT_DESCRIPTION, desc);
	}
	
	/**
	 * Sets the name of the action
	 * (Equivalent to the Action.NAME property)
	 * @param name	name of the action
	 */
	public void setName(String name)
	{
		putValue(Action.NAME, name);
	}
	
	/**
	 * Sets the icon of the action
	 * (Equivalent to the Action.SMALL_ICON property)
	 * @param icon	icon of the action
	 */
	public void setIcon(Icon icon)
	{
		putValue(Action.SMALL_ICON, icon);
	}
	
	/**
	 * Returns the data flow or simulation object at the index specified
	 * @return data flow object
	 */
	public DataFlow getDataFlowObject(int index)
	{
		return (DataFlow)objsFlow.elementAt(index);
	}
	
	/**
	 * Adds a DataFlow object that will be controlled by this action
	 * @param objFlow The data flow or simulation object to control
	 */
	public void addDataFlowObject(DataFlow objFlow)
	{
		if (objFlow == null) return;
		if (objsFlow.contains(objFlow)) return;
		
		objsFlow.addElement(objFlow);
		if (objFlow instanceof Simulation){
			Simulation objSim = (Simulation)objFlow;
			objSim.addSimulationListener(this);
		}
		
		enableAction(flowControlType, getSimulationState());		
		
		//Check
		checkFlowObjectValid(flowControlType, objFlow);
	}

	private void checkFlowObjectValid(int type, DataFlow objFlow)
	{
		//If the type is Start/Stop, the object has to be a Simulation object
		if (type == FLOW_CONTROL_START_STOP){
			if (!(objFlow instanceof Simulation)){
				System.err.println("Warning: Class " + objFlow.getClass().getName() + 
						" does not extend Simulation. Start/Stop is only valid for Simulation objects.");
			}
		}
	}
	
	private void checkFlowObjectsValid(int type)
	{
		for (int i=0; i<objsFlow.size(); i++){
			checkFlowObjectValid(type, (DataFlow)objsFlow.elementAt(i));
		}
	}
	
	/**
	 * Removes a DataFlow object that was previously controlled by this action
	 * @param objFlow The data flow or simulation object to remove and not be controlled anymore
	 */
	public void removeDataFlowObject(DataFlow objFlow)
	{
		if (objFlow instanceof Simulation){
			Simulation objSim = (Simulation)objFlow;
			objSim.removeSimulationListener(this);
		}
		objsFlow.removeElement(objFlow);
	}
	
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e)
	{
		if (objsFlow == null) return;
		
		controlFlow(flowControlType);
	}	
	/**
	 * @param flowControlType2
	 */
	private void controlFlow(int type)
	{
		if (type == FLOW_CONTROL_NONE) return;
		
		DataFlow objFlow;
		for (int i=0; i<objsFlow.size(); i++){
			objFlow = (DataFlow)objsFlow.elementAt(i);
			if (type == FLOW_CONTROL_START){
				objFlow.start();
			}
			else if (type == FLOW_CONTROL_STOP){
				objFlow.stop();
			}
			else if (type == FLOW_CONTROL_RESET){
				objFlow.reset();
			}
			else if (type == FLOW_CONTROL_START_STOP){
				
				if (objFlow instanceof Simulation){
					
					Simulation objSim = (Simulation)objFlow;
					if (objSim.getSimulationState() == Simulation.SIM_RUN_STATE){
						objSim.stop();
					}
					else{
						objSim.start();
					}
				}
			}
		}
	}

	private boolean checkCapabilities(int type)
	{
		// Go through each object in the data flow list
		// report capabilities then we must assume the button
		// should be enabled.
		// if the object reports
		// capabilites, check which ones are available
		// if the object is a simulation and is reporing
		// a simulation state then that will take precendence
		// over this.
		for(int i=0; i<objsFlow.size(); i++) {
			Object flowObject = objsFlow.get(i);
			if(!(flowObject instanceof DataFlowCapabilities)) {
				return true;
			}
			DataFlowCapabilities.Capabilities capabilities = ((DataFlowCapabilities)flowObject).getDataFlowCapabilities();
			switch (type) {
			case FLOW_CONTROL_NONE:
				return true;
			case FLOW_CONTROL_START:
				if(capabilities.canStart()) {
					return true;
				}
				break;
			case FLOW_CONTROL_STOP:
				if(capabilities.canStop()) {
					return true;
				}
				break;
			case FLOW_CONTROL_RESET:
				if(capabilities.canReset()) {
					return true;
				}
				break;
			}			
		}
		return false;
	}
	
	private void enableAction(int type, int simState)
	{
		if (!autoEnable) return;
		if (type == FLOW_CONTROL_NONE) return;
		
		//If there is no info about simulation state, 
		// check the capabilities of the data flow objects
		if (simState == Simulation.SIM_UNDEF_STATE){
			setEnabled(checkCapabilities(type));
			return;
		}
		
		if (type == FLOW_CONTROL_START){
		//If the simulation state is in RUN state, it should be disabled
			if (simState == Simulation.SIM_RUN_STATE){
				setEnabled(false);
			}
			else{
				setEnabled(true);
			}
		}
		else if (type == FLOW_CONTROL_STOP){
		//Only if the simulation state is in RUN state, it should be enabled
			if (simState == Simulation.SIM_RUN_STATE){
				setEnabled(true);
			}
			else{
				setEnabled(false);
			}
		}
		else if (type == FLOW_CONTROL_RESET){
		//If the simulation state is in RESET state, it should be disabled
			if (simState == Simulation.SIM_RESET_STATE){
				setEnabled(false);
			}
			else{
				setEnabled(true);
			}
		}
		else if (type == FLOW_CONTROL_START_STOP){
		//Always enabled
			setEnabled(true);
		}
	}
	
	private int getSimulationState()
	{
		int sim = Simulation.SIM_UNDEF_STATE;
		int s = Simulation.SIM_UNDEF_STATE;
		for (int i=0; i<objsFlow.size(); i++){
			if (objsFlow.elementAt(i) instanceof Simulation){
				Simulation objSim = (Simulation)objsFlow.elementAt(i);
				s = objSim.getSimulationState();
				if (sim == Simulation.SIM_UNDEF_STATE){
					sim = s;
				}
				else{
					if (s != sim){
						return Simulation.SIM_UNDEF_STATE;
					}
				}
			}
		}
		return sim;
	}
	
	/**
	 * Gets the type of action (start, stop, run)
	 * @return Returns the flowControlType.
	 */
	public int getFlowControlType()
	{
		return flowControlType;
	}
	/**
	 * Sets the type of action (start, stop, run)
	 * @param flowControlType The flowControlType to set.
	 * @throws IllegalArgumentException if the type is not one of the
	 * valid types defined in DataFlowControlAction
	 */
	public void setFlowControlType(int type)
	{
		//Checking type is valid
		if (type != FLOW_CONTROL_NONE && type != FLOW_CONTROL_START &&
				type != FLOW_CONTROL_STOP && type != FLOW_CONTROL_RESET &&
				type != FLOW_CONTROL_START_STOP){
			throw new IllegalArgumentException("Type "+type+" not valid.");
		}
		
		this.flowControlType = type;	
	}

	/* (non-Javadoc)
	 * @see org.concord.framework.simulation.SimulationListener#simulationStarted(org.concord.framework.simulation.SimulationEvent)
	 */
	public void simulationStarted(SimulationEvent evt)
	{
		enableAction(flowControlType, evt.getSimulationState());		
	}

	/* (non-Javadoc)
	 * @see org.concord.framework.simulation.SimulationListener#simulationStopped(org.concord.framework.simulation.SimulationEvent)
	 */
	public void simulationStopped(SimulationEvent evt)
	{
		enableAction(flowControlType, evt.getSimulationState());		
	}

	/* (non-Javadoc)
	 * @see org.concord.framework.simulation.SimulationListener#simulationReset(org.concord.framework.simulation.SimulationEvent)
	 */
	public void simulationReset(SimulationEvent evt)
	{
		enableAction(flowControlType, evt.getSimulationState());		
	}
	
	/**
	 * @return Returns the autoEnable.
	 */
	public boolean isAutoEnable()
	{
		return autoEnable;
	}
	
	/**
	 * Sets if the action will be enabled/disabled automatically
	 * depending on the simulation state of the simulation objects
	 * If the auto enable is set to false, the action will
	 * be enabled by default.
	 * @param autoEnable The autoEnable to set.
	 */
	public void setAutoEnable(boolean autoEnable)
	{
		this.autoEnable = autoEnable;
		if (autoEnable){
			enableAction(flowControlType, getSimulationState());
		}
		else{
			setEnabled(true);
		}
	}
}
