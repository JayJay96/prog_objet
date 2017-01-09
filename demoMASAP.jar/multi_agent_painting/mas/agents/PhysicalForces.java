package multi_agent_painting.mas.agents;

import multi_agent_painting.mas.exceptions.ImmutableException;
import multi_agent_painting.physics.PhysicsVector;

public class PhysicalForces {

	public static final PhysicalForces	NONE	= new PhysicalForces(
														PhysicsVector.nullVector,
														0, 0, false);

	PhysicsVector						acceleration;
	double								temperature;
	double								mass;

	private final boolean				modifiable;

	public PhysicalForces(final PhysicsVector acceleration,
			final double temperature, final double mass) {
		super();
		this.acceleration = acceleration;
		this.temperature = temperature;
		this.mass = mass;
		this.modifiable = true;
	}

	public PhysicalForces(final PhysicsVector acceleration,
			final double temperature, final double mass,
			final boolean modifiable) {
		this.acceleration = acceleration;
		this.temperature = temperature;
		this.mass = mass;
		this.modifiable = false;
	}

	public final void add(final PhysicalForces react) throws ImmutableException {
		if (this.modifiable) {
			this.acceleration = this.acceleration.add(react.acceleration);
			this.temperature += this.temperature;
			this.mass += this.mass;
		} else {
			throw new ImmutableException("this object is not modifiable");
		}
	}

	public final void addAcceleration(final PhysicsVector acceleration)
			throws ImmutableException {
		if (this.modifiable) {
			this.acceleration.add(acceleration);
		} else {
			throw new ImmutableException("this object is not modifiable");
		}
	}

	public final void addMass(final double mass) throws ImmutableException {
		if (this.modifiable) {
			this.mass += mass;
		} else {
			throw new ImmutableException("this object is not modifiable");
		}
	}

	public final void addTemperature(final double temperature)
			throws ImmutableException {
		if (this.modifiable) {
			this.temperature += temperature;
		} else {
			throw new ImmutableException("this object is not modifiable");
		}
	}

	public final PhysicsVector getAcceleration() {
		return this.acceleration;
	}

	public final double getMass() {
		return this.mass;
	}

	public final double getTemperature() {
		return this.temperature;
	}

	public boolean isModifiable() {
		return this.modifiable;
	}

	public PhysicalForces modifiableCopy() {
		return new PhysicalForces(this.acceleration, this.temperature,
				this.mass);
	}

	@Override
	public String toString() {
		return "accel: " + this.acceleration + " temp variation: "
				+ this.temperature + " mass variation: " + this.mass;
	}

}
