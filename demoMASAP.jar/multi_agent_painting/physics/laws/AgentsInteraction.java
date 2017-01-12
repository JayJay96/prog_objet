package multi_agent_painting.physics.laws;

import multi_agent_painting.mas.agents.AbstractAgent;
import multi_agent_painting.mas.agents.Agent;
import multi_agent_painting.mas.agents.PhysicalForces;
import multi_agent_painting.mas.exceptions.AgentConfigurationError;
import multi_agent_painting.mas.exceptions.AgentRuntimeException;
import multi_agent_painting.physics.PhysicsVector;
import multi_agent_painting.physics.Space;
import tools.drawing.PhysicalInfo;

/**
 * Implemented by {@link Radiation} and {@link Gravity}
 * 
 * @author Feth
 * 
 */
public abstract class AgentsInteraction implements Interactable {
	final PhysicProperty	physicsProperty;

	public AgentsInteraction(final PhysicProperty physicsProperty) {
		super();
		this.physicsProperty = physicsProperty;
	}

	public abstract void addPhysicsValues(AbstractAgent hostingAgent)
			throws AgentConfigurationError;

	public PhysicProperty getProperty() {
		return this.physicsProperty;
	}

	/**
	 * attraction/repulsion
	 * 
	 * @throws AgentRuntimeException
	 */

	public abstract PhysicalForces interact(
			AbstractAgent hostingAgent,
			PhysicsVector vector,
			PhysicalInfo bodyPhysicalInfo,
			Space space) throws AgentRuntimeException;
}
