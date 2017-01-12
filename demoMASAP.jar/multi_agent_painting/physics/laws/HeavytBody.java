package multi_agent_painting.physics.laws;

import multi_agent_painting.mas.MASConfiguration;
import multi_agent_painting.mas.agents.AbstractAgent;
import multi_agent_painting.mas.agents.Agent;
import multi_agent_painting.mas.agents.PhysicalForces;
import multi_agent_painting.mas.exceptions.AgentConfigurationError;
import multi_agent_painting.physics.PhysicsVector;
import multi_agent_painting.physics.Space;
import tools.appControl.Logger;
import tools.appControl.RandomSource;
import tools.drawing.PhysicalInfo;

public class HeavytBody extends AgentsInteraction{

	private final double	G;
	private final double	minMass;
	private final double	maxMass;
	private final boolean	randomMass;

	public HeavytBody(final MASConfiguration config) {
		super(PhysicProperty.MASS);
		this.G = config.maxMass/50000;
		this.maxMass = config.maxMass;
		this.minMass = config.maxMass;
		this.randomMass = false;
	}



	@Override
	public void addPhysicsValues(final AbstractAgent hostingAgent)
			throws AgentConfigurationError {
		double mass;	
			mass = this.minMass;		
		hostingAgent.getPhysicalInfo().setMass(mass);
		
	}

	@Override
	public PhysicalForces interact(
			final AbstractAgent hostingAgent,
			final PhysicsVector vector,
			final PhysicalInfo bodyPhysicalInfo,
			Space space) {

		return new PhysicalForces(
				PhysicsVector.nullVector,
				hostingAgent.getPhysicalInfo().getTemperature(), hostingAgent.getPhysicalInfo().getMass(), false);
	}
}
