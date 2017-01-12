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

public class Gravity extends AgentsInteraction {

	private final double	G;
	private final double	minMass;
	private final double	maxMass;
	private final boolean	randomMass;

	public Gravity(final MASConfiguration config) {
		super(PhysicProperty.MASS);
		this.G = config.G;
		this.maxMass = config.maxMass;
		this.minMass = config.minMass;
		this.randomMass = true;
	}

	public Gravity(final MASConfiguration config, double mass) {
		super(PhysicProperty.MASS);
		this.G = config.G;
		this.randomMass = false;
		this.maxMass = this.minMass = mass;
	}

	@Override
	public void addPhysicsValues(final AbstractAgent hostingAgent)
			throws AgentConfigurationError {
		double mass;
		if (randomMass) {
			do {
				mass = RandomSource.randomUpperHalfGaussianDoubleBetween(0,
						this.maxMass);
			} while (mass > this.minMass);
		} else {
			mass = this.minMass;
		}
		hostingAgent.getPhysicalInfo().setMass(mass);
		Logger.info(this.getClass().getSimpleName() + " set mass of agent "
				+ hostingAgent.index + " to: " + mass);
	}

	@Override
	public PhysicalForces interact(
			final AbstractAgent hostingAgent,
			final PhysicsVector vector,
			final PhysicalInfo bodyPhysicalInfo,
			Space space) {
		final PhysicalForces result;
		if (vector.size == 0) {
			result = PhysicalForces.NONE;
		} else {
			final double otherMass = bodyPhysicalInfo.getMass();
			// TODO check this : dividing by squared distance, norming by
			// dividing
			// again by distance, to keep direction only
			final double force = this.G * otherMass
					/ (vector.size * vector.size * vector.size);
			final PhysicsVector accel = vector.multiplyBy(force);
			result = new PhysicalForces(accel, 0, 0);
			// System.out.println("agent " + hostingAgent.index + " - d: "
			// + vector.size + ", mass: " + otherMass + ", accel: "
			// + accel);
		}
		return result;
	}
}
