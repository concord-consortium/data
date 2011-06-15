package org.concord.data;

import java.util.ArrayList;

import org.concord.framework.startable.AbstractStartable;
import org.concord.framework.startable.Startable;

public class StartableMultiplexer extends AbstractStartable
{
	ArrayList<Startable> startables = new ArrayList<Startable>();
	
	/**
	 * This does a logical OR of the startables:
	 *  if any of them are running it returns true
	 *  otherwise it returns false. 
	 */
	public boolean isRunning() {
		for (Startable startable : startables) {
			if(startable.isRunning()){
				return true;
			}
		}
		
		return false;
	}

	/**
	 * This does a logical AND of the startables:
	 *   if all of the startables are in the initial state it returns true,
	 *   otherwise it returns false.
	 */
	@Override
	public boolean isInInitialState() {
		for (Startable startable : startables) {
			if(!startable.isInInitialState()){
				return false;
			}
		}
		
		return true;
	}
	
	/**
	 * This calls reset on all of the startables even if they are
	 * already in their initial state.
	 */
	public void reset() {
		for (Startable startable : startables) {
			startable.reset();
		}
	}

	public void start() {
		for (Startable startable : startables) {
			startable.start();
		}
	}

	public void stop() {
		for (Startable startable : startables) {
			startable.stop();
		}
	}

	public void addStartable(Startable startable){
		if(startables.contains(startable)){
			return;
		}
		startables.add(startable);
	}
	
	public void removeStartable(Startable startable){
		startables.remove(startable);
	}
	
	public void clearStartables(){
		startables.clear();
	}
	
}
