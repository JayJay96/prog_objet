package multi_agent_painting.physics;

import java.awt.geom.Point2D;
import java.util.Objects;

import tools.drawing.Coordinates;

public class PhysicsVector implements Vectorable {
	public static final PhysicsVector	nullVector	= new PhysicsVector(0, 0);
	public /*final*/ double					size;	//modification de ma part
	private final Point2D				end;

	public PhysicsVector(final Coordinates origin, final Coordinates end) {
		super();
		this.end = new Coordinates(end.getCoordinates(Coordinates.X)
				- origin.getCoordinates(Coordinates.X), end
				.getCoordinates(Coordinates.Y)
				- origin.getCoordinates(Coordinates.Y));
		this.size = end.distance(origin);
	}

	public PhysicsVector(final double xcomponent, final double ycomponent) {
		super();
		this.end = new Coordinates(xcomponent, ycomponent);
		this.size = this.end.distance(0, 0);
	}

	@Override
	public final PhysicsVector add(final PhysicsVector otherVector) {
		return new PhysicsVector(this.getXComponent()
				+ otherVector.getXComponent(), this.getYComponent()
				+ otherVector.getYComponent());
	}

	public double getXComponent() {
		return this.end.getX();
	}

	//ajout de ma part
	public void setXComponent(double newX){
		this.end.setLocation(newX, this.end.getY());
		this.size = this.end.distance(0, 0);
	}
	
	public double getYComponent() {
		return this.end.getY();
	}
	
	//ajout de ma part
	public void setYComponent(double newY){
		this.end.setLocation(this.end.getX(),newY);
		this.size = this.end.distance(0, 0);
	}

	@Override
	public PhysicsVector multiplyBy(final double... doubleArray) {
		PhysicsVector result = this;
		for (final double d : doubleArray) {
			result = new PhysicsVector(result.getXComponent() * d, result
					.getYComponent()
					* d);
		}

		return result;
	}

	@Override
	public PhysicsVector substract(final PhysicsVector otherVector) {
		return new PhysicsVector(this.getXComponent()
				- otherVector.getXComponent(), this.getYComponent()
				- otherVector.getYComponent());
	}

	//ajout de ma part
	public final double getSize(){
		return this.size;
	}
	@Override
	public String toString() {
		return String.valueOf(this.size + " (" + this.getXComponent() + ", "
				+ this.getYComponent() + ")");
	}
}
