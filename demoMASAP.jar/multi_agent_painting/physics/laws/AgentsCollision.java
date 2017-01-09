package multi_agent_painting.physics.laws;

import multi_agent_painting.mas.MASConfiguration;
import multi_agent_painting.mas.agents.Agent;
import multi_agent_painting.mas.agents.PhysicalForces;
import multi_agent_painting.mas.exceptions.AgentRuntimeException;
import multi_agent_painting.physics.PhysicsVector;
import multi_agent_painting.physics.Space;
import tools.appControl.Logger;
import tools.appControl.RandomSource;
import tools.drawing.PhysicalInfo;

public class AgentsCollision extends AgentsInteraction {

	public static int		collisionsSpawned	= 0;
	public static int		collisionsNbResetFreq;
	private final int		collisionsSpawnableNb;
	private final double	triggerDistance;
	private final double	minMass;

	public AgentsCollision(final MASConfiguration config) {
		super(PhysicProperty.PARITY);
		this.triggerDistance = config.collisionDistance;
		this.collisionsSpawnableNb = config.collisionsSpawnableNb;
		AgentsCollision.collisionsNbResetFreq = config.collisionsNbResetFreq;
		this.minMass = config.minMass/2;
	}

	@Override
	public void addPhysicsValues(final Agent hostingAgent) {
		final int parity = RandomSource.randomPlusOrMinus();
		hostingAgent.getPhysicalInfo().setParity(parity);
		Logger.info(this.getClass().getSimpleName() + " set parity of agent "
				+ hostingAgent.index + " to: " + parity);
	}

	@Override
	public PhysicalForces interact(
			final Agent hostingAgent,
			final PhysicsVector vector,
			final PhysicalInfo bodyPhysicalInfo,
			Space space) throws AgentRuntimeException {

		//Agent closerAgent = space.getAgentBis(bodyPhysicalInfo);
		if(hostingAgent.getPhysicalInfo().getRoleName().equals(bodyPhysicalInfo.getRoleName())){
			if (vector.size < this.triggerDistance) {
				final int hostingAgentParity = hostingAgent.getPhysicalInfo()
						.getParity();
				double mass = bodyPhysicalInfo.getMass();
				if ((bodyPhysicalInfo.getParity() == hostingAgentParity)
						&& (AgentsCollision.collisionsSpawned < this.collisionsSpawnableNb) && mass > this.minMass) {
					hostingAgent.doExplode();
					bodyPhysicalInfo.print();
					AgentsCollision.collisionsSpawned++;
	
				} else {
					if ((hostingAgentParity == 1)) {
						bodyPhysicalInfo.fusion(hostingAgent.getPhysicalInfo());
						bodyPhysicalInfo.drawCircle();
						hostingAgent.doDie();
					}
				}
	
			}
		}
		return new PhysicalForces(PhysicsVector.nullVector, 0, 0);
	}

}
