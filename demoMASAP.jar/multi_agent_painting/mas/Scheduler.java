package multi_agent_painting.mas;

import java.util.ArrayList;
import java.util.Collections;

import multi_agent_painting.mas.agents.AbstractAgent;
import tools.appControl.Logger;

import multi_agent_painting.mas.agents.Agent;
import multi_agent_painting.mas.exceptions.AgentInitException;
import multi_agent_painting.mas.exceptions.AgentRuntimeException;
import multi_agent_painting.mas.exceptions.MasException;

public class Scheduler {

	private Messenger messenger;
	private boolean ready;
	
	public Scheduler(){
		this.ready = false;
	}
	
	public synchronized void setMessenger(Messenger messenger) {
		this.messenger = messenger;
		if(this.messenger != null){
			this.ready = true;
		}
	}
	
	public void schedule(final ArrayList<AbstractAgent> c) throws AgentInitException,
		AgentRuntimeException, MasException {
		
			if(this.messenger.isReady() && this.ready){
				messenger.sendValue();
				// Allow the Scheduler to shuffle the list of agent
				// in order to be more coherent with theory
				ArrayList<AbstractAgent> temp = c;
				Collections.shuffle(temp);		
				for (final AbstractAgent agent : temp) {
					agent.live();
				}
			}else Logger.critical("#### Messenger or Scheduler not ready");
	}		
	
}
