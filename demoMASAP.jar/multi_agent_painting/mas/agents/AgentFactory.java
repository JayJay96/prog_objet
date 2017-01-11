package multi_agent_painting.mas.agents;

import multi_agent_painting.mas.Kernel;
import multi_agent_painting.physics.Space;

/**
 * Created by Epulapp on 11/01/2017.
 */
public class AgentFactory {
    private static final AgentFactory INSTANCE = new AgentFactory();

    private AgentFactory(){}

    public static AgentFactory getInstance(){
        return INSTANCE;
    }

    public Agent createAgent(Class type, Space space, Kernel kernel){
        if(type == Agent.class)
            ;
        return new Agent(space, kernel);
    }
}
