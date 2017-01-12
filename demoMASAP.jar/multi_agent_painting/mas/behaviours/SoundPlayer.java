package multi_agent_painting.mas.behaviours;


import java.util.HashMap;

import multi_agent_painting.mas.agents.AbstractAgent;
import tools.drawing.PhysicalInfo;
import multi_agent_painting.mas.agents.Agent;
import multi_agent_painting.mas.agents.PhysicalForces;
import multi_agent_painting.mas.exceptions.AgentRuntimeException;
import multi_agent_painting.mas.exceptions.RoleInitException;
import multi_agent_painting.physics.PhysicsVector;
import multi_agent_painting.physics.Space;

public class SoundPlayer extends Behaviours {

	public SoundPlayer() throws RoleInitException {
		super();
		
	}
	
	public synchronized PhysicalForces react(
			final AbstractAgent hostingAgent,
			final PhysicsVector vector,
			final PhysicalInfo bodyPhysicalInfo,
			final Space space) throws AgentRuntimeException {
		final PhysicalForces result = PhysicalForces.NONE.modifiableCopy();
		return result;
	}

}
