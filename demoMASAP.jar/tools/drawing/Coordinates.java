package tools.drawing;

import java.awt.geom.Point2D;

import multi_agent_painting.physics.PhysicsVector;
import tools.appControl.RandomSource;

public class Coordinates extends Point2D {

	public enum Direction {
		UP(0, -1),
		DOWN(0, +1),
		RIGHT(+1, 0),
		LEFT(-1, 0),
		CENTER(0, 0),
		UP_LEFT(-1, -1),
		UP_RIGHT(1, -1),
		DOWN_LEFT(-1, 1),
		DOWN_RIGHT(1, 1);

		public static final Direction[][]	directions	= {
																{ UP_LEFT,
																		LEFT,
																		DOWN_LEFT },
																{ UP, CENTER,
																		DOWN },
																{ UP_RIGHT,
																		RIGHT,
																		DOWN_RIGHT } };

		public final int[]					coordinateSign;

		Direction(final int horizontalCoordinateIncrementSign,
				final int verticalCoordinateIncrementSign) {
			this.coordinateSign = new int[2];
			this.coordinateSign[Coordinates.X] = horizontalCoordinateIncrementSign;
			this.coordinateSign[Coordinates.Y] = verticalCoordinateIncrementSign;
		}
	}

	public final static int			X		= 0;
	public final static int			Y		= 1;
	public static final Coordinates	ORIGIN	= new Coordinates(0, 0);

	/**
	 * Is value between 0 and maxValue ?
	 * 
	 * @param d
	 * @param maxValue
	 * @return
	 *            <ul>
	 *            <li>-1 : lower</li>
	 *            <li> 0 : inside</li>
	 *            <li>1 : higher</li>
	 */
	public static int isInside(final double d, final int maxValue) {
		if (d < 0) {
			return -1;
		}
		if (d > maxValue) {
			return 1;
		}
		return 0;
	}

	public static final Direction positionToGrid(
			final Coordinates coordinates,
			final int xsize,
			final int ysize) {
		final int i = 1 + Coordinates.isInside(
				coordinates.coordinates[Coordinates.X], xsize);
		final int j = 1 + Coordinates.isInside(
				coordinates.coordinates[Coordinates.Y], ysize);

		final Direction direction = Direction.directions[i][j];
		return direction;
	}

	protected double[]	coordinates	= new double[2];

	public Coordinates(final double d, final double e) {
		this.coordinates[Coordinates.X] = d;
		this.coordinates[Coordinates.Y] = e;
	}

	/**
	 * Not an exact copy, on purpose
	 * 
	 * @return
	 */
	public Coordinates approximateCopy() {
		double x = 0;
		double y = 0;
		do {
			x = this.coordinates[Coordinates.X]
					+ RandomSource.randomizer.nextGaussian();
		} while (Math.abs(x) < 0.6);
		do {
			y = this.coordinates[Coordinates.Y]
					+ RandomSource.randomizer.nextGaussian();
		} while (Math.abs(y) < 0.6);
		return new Coordinates(x, y);
	}

	public Coordinates copy() {
		return new Coordinates(this.coordinates[Coordinates.X],
				this.coordinates[Coordinates.Y]);
	}

	/**
	 * 
	 * @param coordIndex
	 *            from Coordinates.X or Coordinates.Y
	 * @return
	 */
	public final double getCoordinates(final int coordIndex) {
		return this.coordinates[coordIndex];
	}

	@Override
	public double getX() {
		return this.coordinates[Coordinates.X];
	}

	@Override
	public double getY() {
		return this.coordinates[Coordinates.Y];
	}

	@Override
	public void setLocation(final double x, final double y) {
		this.coordinates[Coordinates.X] = x;
		this.coordinates[Coordinates.Y] = y;

	}

	@Override
	public String toString() {
		return "(" + String.valueOf(this.coordinates[Coordinates.X]) + ", "
				+ String.valueOf(this.coordinates[Coordinates.Y]) + ")";
	}

	public void translate(final double xcomponent, final double ycomponent) {
		this.coordinates[Coordinates.X] += xcomponent;
		this.coordinates[Coordinates.Y] += ycomponent;
	}

	public void translate(final PhysicsVector vector) {
		translate(vector.getXComponent(), vector.getYComponent());
	}

	public Coordinates addY(double y) {
		return new Coordinates(this.coordinates[Coordinates.X],
				this.coordinates[Coordinates.Y] + y);
	}
	public Coordinates addX(double x) {
		return new Coordinates(this.coordinates[Coordinates.X] + x,
				this.coordinates[Coordinates.Y]);
	}
}
