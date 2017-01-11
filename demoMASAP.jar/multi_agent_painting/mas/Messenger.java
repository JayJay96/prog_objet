package multi_agent_painting.mas;


import java.util.HashMap;

import multi_agent_painting.mas.agents.AbstractAgent;
import tools.appControl.Logger;
import multi_agent_painting.mas.agents.Agent;

public class Messenger {

	
	private HashMap<Double, Double> map;
	private HashMap<AbstractAgent, Double> agentsFrequencies;
	private boolean ready;
	
	public Messenger(
			HashMap<Double, Double> map,
			HashMap<AbstractAgent, Double> agentsFrequencies
	){
		this.map = map;
		this.agentsFrequencies = agentsFrequencies;
		this.ready = false;
	}
	
	public void init(){
		if(this.map != null && 
		   this.agentsFrequencies != null
		){
			this.ready = true;
			Logger.critical("Messenger initialization ok ");
		}else{
			Logger.critical("## Messenger initialization failed");
		}
	}
	
	public boolean isReady(){
		return this.ready;
	}
	
	
	public void sendValue(){
		// For all agents in the map:
		for(AbstractAgent a : agentsFrequencies.keySet()){
			// Set the musical value of the agent, 
			// according to it's frequency sensibilities
			a.setMusicalValue(map.get(agentsFrequencies.get(a)));
		}		
	}

	public void setAgentsFrequency(HashMap<AbstractAgent, Double> agentsFrequency) {
		this.agentsFrequencies = agentsFrequency;		
	}
	
}
