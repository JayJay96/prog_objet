package multi_agent_painting.physics.laws;

import multi_agent_painting.mas.MASConfiguration;
import multi_agent_painting.mas.agents.AbstractAgent;
import multi_agent_painting.mas.agents.Agent;
import multi_agent_painting.mas.agents.PhysicalForces;
import multi_agent_painting.physics.PhysicsVector;
import multi_agent_painting.physics.Space;
import tools.appControl.RandomSource;
import tools.drawing.PhysicalInfo;

public class HotBody extends AgentsInteraction{
	private final double	minTemp;
	private final double	maxTemp;
	private final double	temp;
	private final boolean	randomtemp;

	public HotBody(final MASConfiguration config) {
		super(PhysicProperty.TEMPERATURE);
		this.maxTemp = config.maxTemp;
		this.minTemp = config.maxTemp;
		this.temp = config.maxTemp/1000;
		this.randomtemp = false;
	}


	@Override
	public void addPhysicsValues(final AbstractAgent hostingAgent) {
		final PhysicalInfo physicalInfo = hostingAgent.getPhysicalInfo();
		final double temperature;		
			temperature = this.temp;		
		physicalInfo.setTemperature(temperature);
		final double radAbsorb = RandomSource.randomizer.nextDouble();
		physicalInfo.setRadiationAbsorption(radAbsorb);
		
	}

	/**
	 * The transmitted force is k*(surface of emitter(formFactor))*(surface of
	 * the receiver)*(temperature of emitter)^4/distance^2. Motion is increased
	 * by (2 - absorptionRatio of receiver)*force The receiver's temperature is
	 * increased by (absorptionRatio of receiver)*force (reflection doubles the
	 * force) <br>
	 * <h2>Note: we're on the receiver's point of view here</h2>
	 */
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
