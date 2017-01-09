package multi_agent_painting.physics.laws;

import multi_agent_painting.physics.PhysicsVector;

public class Drag implements Friction {

	private static Drag	instance;

	public static Friction getSingleton() {
		if (Drag.instance == null) {
			Drag.instance = new Drag();
		}
		return Drag.instance;
	}

	private Drag() {

	}

	/**
	 * Dragging ('frottement fluide')
	 * 
	 * @param speed
	 * @return
	 */
	@Override
	public PhysicsVector getFrictionFromSpeed(
			final PhysicsVector speed,
			final double dragCoef,
			final double mass) {
		if (mass == 0) {
			return PhysicsVector.nullVector;
		}
		final double f = -dragCoef / mass;
		return speed.multiplyBy(f);
	}

}
