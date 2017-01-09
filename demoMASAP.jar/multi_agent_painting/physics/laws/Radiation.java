package multi_agent_painting.physics.laws;

import multi_agent_painting.mas.MASConfiguration;
import multi_agent_painting.mas.agents.Agent;
import multi_agent_painting.mas.agents.PhysicalForces;
import multi_agent_painting.physics.PhysicsVector;
import multi_agent_painting.physics.Space;
import tools.appControl.Logger;
import tools.appControl.RandomSource;
import tools.drawing.PhysicalInfo;

public class Radiation extends AgentsInteraction {

	private final double	minTemp;
	private final double	maxTemp;
	private final double	temp;
	private final boolean	randomtemp;

	public Radiation(final MASConfiguration config) {
		super(PhysicProperty.TEMPERATURE);
		this.maxTemp = config.maxTemp;
		this.minTemp = config.minTemp;
		this.temp = 0;
		this.randomtemp = true;
	}

	public Radiation(double temp) {
		super(PhysicProperty.TEMPERATURE);
		this.temp = temp;
		this.maxTemp = this.minTemp = 0;
		this.randomtemp = false;
	}

	@Override
	public void addPhysicsValues(final Agent hostingAgent) {
		final PhysicalInfo physicalInfo = hostingAgent.getPhysicalInfo();
		final double temperature;
		if (this.randomtemp) {
			temperature = RandomSource.randomGaussianDoubleBetween(
					this.minTemp, this.maxTemp);
		} else {
			temperature = this.temp;
		}
		physicalInfo.setTemperature(temperature);
		final double radAbsorb = RandomSource.randomizer.nextDouble();
		physicalInfo.setRadiationAbsorption(radAbsorb);
		Logger.info(this.getClass().getSimpleName()
				+ " set temperature of agent " + hostingAgent.index + " to: "
				+ temperature + " and radiation absorbtion to " + radAbsorb);
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
			final Agent hostingAgent,
			final PhysicsVector vector,
			final PhysicalInfo bodyPhysicalInfo,
			Space space) {
		final PhysicalForces result;
		if (vector.size == 0) {
			result = PhysicalForces.NONE;
		} else {
			final PhysicalInfo hostingAgentInfo = hostingAgent
					.getPhysicalInfo();

			final double otherBodyRadiation = bodyPhysicalInfo
					.getBlackBodyRadiation();
			final double receiverFormFactor = hostingAgentInfo.getFormFactor();
			final double force = otherBodyRadiation * receiverFormFactor
					/ (vector.size * vector.size * vector.size);
			final double radiationAbsorption = hostingAgentInfo
					.getRadiationAbsorption();
			final double radiationPressureCoefficient = force
					* (2 - radiationAbsorption);
			final PhysicsVector accel = vector
					.multiplyBy(radiationPressureCoefficient);
			final double tempIncrease = force * radiationAbsorption;
			result = new PhysicalForces(accel, tempIncrease, 0);
		}

		return result;
	}

}
