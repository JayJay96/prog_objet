package multi_agent_painting.mas.agents;

import multi_agent_painting.mas.Kernel;
import multi_agent_painting.mas.exceptions.AgentConfigurationError;
import multi_agent_painting.mas.exceptions.AgentInitException;
import multi_agent_painting.mas.roles.AbstractRole;
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

    public AbstractAgent createAgent(Space space, Kernel kernel){
        return new Agent(space, kernel);
    }

    public AbstractAgent createAgent(Space space, Kernel kernel, AbstractRole role) throws AgentInitException, AgentConfigurationError {
        AbstractAgent a = new Agent(space, kernel);
        a.addRole(role);
        a.getPhysicalInfo().sanityCheck();
        return a;
    }
}
