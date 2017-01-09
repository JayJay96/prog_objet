package multi_agent_painting.mas;

import java.util.ArrayList;
import java.util.Collections;

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
	
	public void schedule(final ArrayList<Agent> c) throws AgentInitException,
		AgentRuntimeException, MasException {
		
			if(this.messenger.isReady() && this.ready){
				messenger.sendValue();
				// Allow the Scheduler to shuffle the list of agent
				// in order to be more coherent with theory
				ArrayList<Agent> temp = c;
				Collections.shuffle(temp);		
				for (final Agent agent : temp) {
					agent.live();
				}
			}else Logger.critical("#### Messenger or Scheduler not ready");
	}		
	
}
