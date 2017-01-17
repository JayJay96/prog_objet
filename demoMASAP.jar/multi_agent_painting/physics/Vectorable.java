package multi_agent_painting.physics;

/**
 * Created by Epulapp on 16/01/2017.
 */
public interface Vectorable {

    public PhysicsVector add(final PhysicsVector otherVector);

    public PhysicsVector multiplyBy(final double... doubleArray);

    public PhysicsVector substract(final PhysicsVector otherVector);


}
