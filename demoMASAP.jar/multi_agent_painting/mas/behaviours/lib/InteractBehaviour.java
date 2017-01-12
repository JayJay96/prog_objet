package multi_agent_painting.mas.behaviours.lib;

import multi_agent_painting.mas.agents.AbstractAgent;
import multi_agent_painting.mas.agents.Agent;
import multi_agent_painting.mas.agents.PhysicalForces;
import multi_agent_painting.mas.behaviours.Behaviour;
import multi_agent_painting.mas.behaviours.Behaviours;
import multi_agent_painting.mas.behaviours.InitAgent;
import multi_agent_painting.mas.exceptions.AgentConfigurationError;
import multi_agent_painting.mas.exceptions.AgentRuntimeException;
import multi_agent_painting.mas.exceptions.RoleInitException;
import multi_agent_painting.physics.PhysicsVector;
import multi_agent_painting.physics.Space;
import multi_agent_painting.physics.laws.AgentsInteraction;
import tools.drawing.PhysicalInfo;

public class InteractBehaviour extends Behaviours {

	private final AgentsInteraction	accelInteraction;

	public InteractBehaviour(final AgentsInteraction accelInteraction)
			throws RoleInitException {
		super();
		this.accelInteraction = accelInteraction;
	}

	@InitAgent
	public void addPhysicsValues(final AbstractAgent hostingAgent)
			throws AgentConfigurationError {
		this.accelInteraction.addPhysicsValues(hostingAgent);
	}

	@Behaviour
	public PhysicalForces gravite(
			final AbstractAgent hostingAgent,
			final PhysicsVector vector,
			final PhysicalInfo bodyPhysicalInfo,
			final Space space) throws AgentRuntimeException {
		return this.accelInteraction.interact(hostingAgent, vector,
				bodyPhysicalInfo,space);
	}

}
