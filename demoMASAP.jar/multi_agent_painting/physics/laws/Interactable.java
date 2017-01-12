package multi_agent_painting.physics.laws;

import multi_agent_painting.mas.agents.Agent;
import multi_agent_painting.mas.agents.PhysicalForces;
import multi_agent_painting.mas.exceptions.AgentConfigurationError;
import multi_agent_painting.mas.exceptions.AgentRuntimeException;
import multi_agent_painting.physics.PhysicsVector;
import multi_agent_painting.physics.Space;
import tools.drawing.PhysicalInfo;

/**
 * Created by Sebastien on 11/01/2017.
 */
public interface Interactable {


    public PhysicalForces interact(
            Agent hostingAgent,
            PhysicsVector vector,
            PhysicalInfo bodyPhysicalInfo,
            Space space) throws AgentRuntimeException;

    public void addPhysicsValues(Agent hostingAgent)
            throws AgentConfigurationError;

}
