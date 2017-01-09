package multi_agent_painting.physics.laws;

import multi_agent_painting.physics.PhysicsVector;

/**
 * Known implementations: {@link Drag}
 * 
 * @author Feth
 * 
 */
public interface Friction {

	public PhysicsVector getFrictionFromSpeed(
			final PhysicsVector speed,
			final double dragCoef,
			final double mass);

}
