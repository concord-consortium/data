package org.concord.data.ui;

import javax.swing.JButton;
import javax.swing.JToolBar;

import org.concord.framework.startable.Startable;

public class StartableToolBar extends JToolBar {
	private static final long serialVersionUID = 1L;
	
	StartableActionProvider actionProvider;
	
	public StartableToolBar() {
		actionProvider = new StartableActionProvider();
		
		add(new JButton(actionProvider.getStartAction()));
		add(new JButton(actionProvider.getStopAction()));
		add(new JButton(actionProvider.getResetAction()));
	}

	public void setStartable(Startable startable) {
		actionProvider.setStartable(startable);
	}
	
	
}
