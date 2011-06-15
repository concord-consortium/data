package org.concord.data.ui;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.util.logging.Logger;

import javax.swing.AbstractAction;
import javax.swing.Action;

import org.concord.framework.startable.Startable;
import org.concord.framework.startable.StartableEvent;
import org.concord.framework.startable.StartableEvent.StartableEventType;
import org.concord.framework.startable.StartableInfo;
import org.concord.framework.startable.StartableListener;

public class StartableActionProvider 
{
	private static final Logger logger = Logger
			.getLogger(StartableActionProvider.class.getCanonicalName());
	
	private static final StartableInfo defaultInfo = new StartableInfo();
	
	protected Startable startable;

	protected StartableListener listener = new StartableListener(){
		public void startableEvent(StartableEvent event) {
			logger.finest("type: " + event.getType() + ", startable: " + event.getStartable());
			updateActions(event);				
		}			
	};

	protected Action startAction = new AbstractAction(StartableInfo.DEFAULT_START_VERB){		
		private static final long serialVersionUID = 1L;

		public void actionPerformed(ActionEvent e) {
			startable.start();			
		}
	};

	protected Action stopAction= new AbstractAction(StartableInfo.DEFAULT_STOP_VERB){
		private static final long serialVersionUID = 1L;

		public void actionPerformed(ActionEvent e) {
			startable.stop();
		}
	};
	
	protected Action resetAction= new AbstractAction(StartableInfo.DEFAULT_RESET_VERB){
		private static final long serialVersionUID = 1L;

		public void actionPerformed(ActionEvent e) {
			startable.reset();
		}
	};
	
	protected Action startStopAction= new AbstractAction(StartableInfo.DEFAULT_START_STOP_VERB){
		private static final long serialVersionUID = 1L;

		public void actionPerformed(ActionEvent e) {
			if(startable.isRunning()){
				startable.stop();
			} else {
				startable.start();
			}
		}
	};

	public Action getStartAction() {
		return startAction;
	}

	public Action getStartStopAction() {
		return startStopAction;
	}

	public Action getStopAction() {
		return stopAction;
	}

	public Action getResetAction() {
		return resetAction;
	}	
	
	public void setStartable(Startable startable) {
		if(this.startable != null){
			this.startable.removeStartableListener(listener);
		}
		this.startable = startable;
		updateActions(null);
		startable.addStartableListener(listener);
	}	
	
	class ActionUpdater implements Runnable
	{
		Boolean running;
		Boolean inInitialState;
		StartableInfo info;
		
		public void run() {
			// TODO Auto-generated method stub
		    if (startable.isAtEndOfStream()) {
		        startAction.setEnabled(false);
		    }
		    else if(info.canRestartWithoutReset){
				startAction.setEnabled(!isRunning());
			} else {
				startAction.setEnabled(!isRunning() && isInIntialState());
			}
			stopAction.setEnabled(isRunning());

			if(info.canResetWhileRunning){
				resetAction.setEnabled(!isInIntialState());
			} else {
				resetAction.setEnabled(!isInIntialState() && !isRunning());
			}

			// enable this incase it was disabled before
			startStopAction.setEnabled(true);					
		}		
		
		public boolean isRunning()
		{
			if(running != null){
				return running;
			}
			
			return startable.isRunning();
		}
		
		public boolean isInIntialState()
		{
			if(inInitialState != null){
				return inInitialState;
			}
			
			return startable.isInInitialState();
		}
	}
	
	/**
	 * FIXME this could be simplified a lot if the timing of the events and the availability of
	 * isRunning() and isInInitialState() were consistent
	 * 
	 * @param evt
	 */
	protected void updateActions(StartableEvent event)
	{
		StartableInfo info;
		if(startable.getStartableInfo() != null){
			info = startable.getStartableInfo();
		} else {
			info = defaultInfo;
		}

		ActionUpdater updater = new ActionUpdater();
		updater.info = info;
		
		if(event != null){
			switch(event.getType()){
				case RESET:
					// override this because we know we are in the initial state
					updater.inInitialState = true;

					// FIXME we don't know about running in this case so we have to rely on
					// isRunning which might not return the correct value when it is called
					break;
				case STARTED:
					// override this because we know we are running
					updater.running = true;
					
					// override this because we know we are not in the initial state if we are started
					updater.inInitialState = false;
					break;
				case STOPPED:
					// override this because we know we are running
					updater.running = false;

					// FIXME we don't know if we are in the initial state so we have to rely on
					// isInitialState which might not return the correct value when it is called
					break;	
			}
		} 
		

		if(event == null || event.getType() == StartableEventType.UPDATED) {
			updateStrings(info);
		} 
			
		if (!info.enabled){
			startAction.setEnabled(false);
			stopAction.setEnabled(false);
			resetAction.setEnabled(false);
			startStopAction.setEnabled(false);
		} else if(!info.sendsEvents){
			startAction.setEnabled(true);
			stopAction.setEnabled(true);
			resetAction.setEnabled(true);
			startStopAction.setEnabled(true);				
		} else {
			// Invoke later is used to increase the chance that the startable will
			// have correctly updated its internal state.
			EventQueue.invokeLater(updater);
		}
	}

	protected String getStartVerb(StartableInfo info) {
		if(info.startVerb == null){
			return StartableInfo.DEFAULT_START_VERB;
		} else {
			return info.startVerb;
		}
	}
	
	protected String getStopVerb(StartableInfo info) {
		if(info.stopVerb == null){
			return StartableInfo.DEFAULT_STOP_VERB;
		} else {
			return info.stopVerb;
		}
	}

	protected String getResetVerb(StartableInfo info) {
		if(info.resetVerb == null){
			return StartableInfo.DEFAULT_RESET_VERB;
		} else {
			return info.resetVerb;
		}
	}
	
	protected String getStartStopVerb(StartableInfo info){
		if(info.resetVerb == null){
			return StartableInfo.DEFAULT_START_STOP_VERB;
		} else {
			return info.startStopVerb;
		}		
	}
	
	protected void updateStrings(StartableInfo info){
		if(info.startStopLabel != null){
			startAction.putValue(Action.SHORT_DESCRIPTION, 
					getStartVerb(info) + " " + info.startStopLabel);
			stopAction.putValue(Action.SHORT_DESCRIPTION, 
					getStopVerb(info) + " " + info.startStopLabel);
			startStopAction.putValue(Action.SHORT_DESCRIPTION, 
					getStartStopVerb(info) + " " + info.startStopLabel);
		}

		if(info.resetLabel != null){
			resetAction.putValue(Action.SHORT_DESCRIPTION, getResetVerb(info) + " " + info.resetLabel);
		}

		startAction.putValue(Action.NAME, getStartVerb(info));
		stopAction.putValue(Action.NAME, getStopVerb(info));
		resetAction.putValue(Action.NAME, getResetVerb(info));		
		startStopAction.putValue(Action.NAME, getStartStopVerb(info));		
	}

}
