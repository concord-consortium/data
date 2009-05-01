package org.concord.data.ui;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.util.logging.Logger;

import javax.swing.AbstractAction;
import javax.swing.Action;

import org.concord.framework.startable.Startable;
import org.concord.framework.startable.StartableEvent;
import org.concord.framework.startable.StartableInfo;
import org.concord.framework.startable.StartableListener;
import org.concord.framework.startable.StartableEvent.StartableEventType;

public class StartableActionProvider 
{
	private static final Logger logger = Logger
			.getLogger(StartableActionProvider.class.getCanonicalName());
	
	protected Startable startable;

	protected StartableListener listener = new StartableListener(){
		public void startableEvent(StartableEvent event) {
			logger.finest("type: " + event.getType() + ", startable: " + event.getStartable());
			updateActions(event);				
		}			
	};

	protected Action startAction = new AbstractAction("Start"){		
		private static final long serialVersionUID = 1L;

		public void actionPerformed(ActionEvent e) {
			startable.start();			
		}
	};

	protected Action stopAction= new AbstractAction("Stop"){
		private static final long serialVersionUID = 1L;

		public void actionPerformed(ActionEvent e) {
			startable.stop();
		}
	};
	
	protected Action resetAction= new AbstractAction("Reset"){
		private static final long serialVersionUID = 1L;

		public void actionPerformed(ActionEvent e) {
			startable.reset();
		}
	};
	
	protected Action startStopAction= new AbstractAction("Start/Stop"){
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
	
	/**
	 * During the time of the event the state of the simulation has not been updated yet
	 * so we maintain our own version.
	 * 
	 * @param evt
	 */
	protected void updateActions(StartableEvent event)
	{				
		if(event != null && event.getType() != StartableEventType.UPDATED){
			switch(event.getType()){
				case RESET:
					
					// FIXME invoke later is used incase the startable doesnt' update isRunning 
					// right away.  It would be better if the startable either:
					//   - sent a boolean with the event about its running state
					//   - made sure isRunning was correctly updated before notifying the listeners
					//   - sent a stop event along with reset, if the reset calls stop
					EventQueue.invokeLater(new Runnable(){
						public void run() {
							// TODO need to handle the info conditions for the reset button.
							// there is a condition in the info to tweak this
							resetAction.setEnabled(!startable.isInInitialState());

							startAction.setEnabled(!startable.isRunning());
							stopAction.setEnabled(startable.isRunning());						
						}
					});
					
					break;
				case STARTED:
					startAction.setEnabled(false);
					stopAction.setEnabled(true);
					// TODO need to handle the info conditions for the reset button.
					resetAction.setEnabled(true);
					break;
				case STOPPED:
					startAction.setEnabled(true);
					stopAction.setEnabled(false);
					// TODO need to handle the info conditions for the reset button.
					// Also there is an issue of timing here. Sometimes stop is called
					// when reset is called so a stop event might come in after a reset
					// event.  The order of these should be clarified.  There is a condition
					// in the info to clarify this.
					resetAction.setEnabled(true);					
					break;	
			}
		} else {
			StartableInfo info = startable.getStartableInfo();
			boolean enabled = true;
			if(info != null) {
				enabled = info.enabled;
				if (info.resetVerb != null){
					resetAction.putValue(Action.NAME, info.resetVerb);
				}
			} 
			
			if (!enabled){
				startAction.setEnabled(false);
				stopAction.setEnabled(false);
				resetAction.setEnabled(false);
				startStopAction.setEnabled(false);
			} else {
				startAction.setEnabled(!startable.isRunning());
				stopAction.setEnabled(startable.isRunning());

				// TODO need to handle the startable info conditions for the 
				// reset button.
				resetAction.setEnabled(!startable.isInInitialState());
				
				// set this to true in case it was changed before.
				startStopAction.setEnabled(true);

			}
		}
	}

}
