package multi_agent_painting.mas;

import multi_agent_painting.mas.agents.AbstractAgent;

import java.util.HashMap;

/**
 * Created by Epulapp on 16/01/2017.
 */
public interface Messengerable {

    public boolean isReady();


    public void sendValue();

    public void setAgentsFrequency(HashMap<AbstractAgent, Double> agentsFrequency);

}
