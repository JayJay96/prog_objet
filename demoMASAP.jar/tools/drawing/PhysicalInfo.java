package tools.drawing;

import java.awt.Color;

import multi_agent_painting.applet.panes.drawingPane.DrawPanel;
import multi_agent_painting.mas.agents.PhysicalForces;
import multi_agent_painting.mas.exceptions.AgentConfigurationError;
import multi_agent_painting.physics.PhysicsVector;
import multi_agent_painting.physics.Space;
import multi_agent_painting.physics.laws.Friction;
import tools.appControl.Logger;

public class PhysicalInfo {

	public static final int		MAX_SPEED				= 20;

	public static final Color	DEFAULT_INITIAL_COLOR	= new Color(0);
	final Friction				friction;

	private Coordinates lastCoordinates			= Coordinates.ORIGIN;
	private String				roleName = new String();
	private Coordinates			coordinates;
	private PhysicsVector		speed					= new PhysicsVector(0,
																0);
	/**
	 * In dragging ('frottement fluide', this is the b in f = -b*v. Please
	 * guaranty 0 <= dragCoef < 1.
	 */
	private double				formFactor;

	private PhysicsVector		accel					= PhysicsVector.nullVector;

	private double				mass;
	private double				temperature;
	private double				radiationAbsorption;

	private double				blackBodyRadiation;

	private int					parity;

	private int					agentRef;

	private boolean				shouldPrint;

	private final double		k;
	private  boolean 			canInteract				= false;	

	private boolean				shouldDrawCircle;
	private boolean				speedDown				=false;
	private boolean				moving					= true;

	public PhysicalInfo(final double d, final double e,
			final Friction friction, final double frictionCoef, final double k) {
		this.coordinates = new Coordinates(d, e);
		this.friction = friction;
		this.formFactor = frictionCoef;
		this.k = k;
	}

	private void accel(final PhysicsVector accel) {
		this.accel = this.accel.add(accel);
	}

	public void applyForces(final PhysicalForces forces) {
		accel(forces.getAcceleration());
		this.mass += forces.getMass();
		this.temperature += forces.getTemperature();
	}

	public void fusion(final PhysicalInfo otherPhysicalInfo) {
		this.mass += otherPhysicalInfo.mass;
		this.temperature = (this.temperature + otherPhysicalInfo.temperature) / 2;
		this.formFactor = (this.formFactor + otherPhysicalInfo.formFactor) / 2;
		this.setMoving(false);
		updateAbsoluteBlackBodyRadiation();
	}

	private void setMoving(boolean b) {
		this.moving = b;
		this.speed = PhysicsVector.nullVector;
	}

	public int getAgentRef() {
		return this.agentRef;
	}
	public void setRoleName (String n){
		this.roleName=n;
	}
	public String getRoleName() {
		return this.roleName;
	}
	
	public boolean getSpeedDonw() {
		return this.speedDown;
	}
	
	public void setSpeedDown(boolean s){
		this.speedDown=s;
	}

	public double getBlackBodyRadiation() {
		return this.blackBodyRadiation;
	}

	public Coordinates getCoordinates() {
		return this.coordinates;
	}
		
	public Coordinates getLastCoordinates(){
		return this.lastCoordinates;
	}

	public double getFormFactor() {
		return this.formFactor;
	}

	public double getMass() {
		return this.mass;
	}

	public int getParity() {
		return this.parity;
	}

	public double getRadiationAbsorption() {
		return this.radiationAbsorption;
	}

	public double getSpeed() {
		return this.speed.size;
	}

	public double getTemperature() {
		return this.temperature;
	}

	/**
	 * Compute new coordinates.
	 */
	public synchronized void nextStep(final DrawPanel representation,final PhysicalInfo pI, final Space s) {

		final boolean shouldTrace = accel.size > 0;

		if (this.moving) {
			updateSpeed();
		}

		if (this.shouldDrawCircle) {
			representation.drawCircle(this.getMass(), this.coordinates);
			this.shouldDrawCircle = false;
		} else if (this.shouldPrint) {
			representation.leavePrint(this.getMass(), this.coordinates,
					this.speed);
			this.shouldPrint = false;
		} else {
			if (moving) {
				boolean nullSpeed = this.speed.size > 0;
				if (nullSpeed) {
					updatePosition();
				}
				if (shouldTrace) {
					// paint according to acceleration
					Logger.debug("painting");
					
					if(s.getAgentBis(pI).getRoles().get(0).getName().equals("Painter")&& s.getAgentBis(pI).getPhysicalInfo().getInteraction())
					representation.trace(this.getMass(), this.accel.size,
							this.lastCoordinates, this.coordinates,
							s.getAgentBis(pI).getMusicalValues() );

					if(s.getAgentBis(pI).getRoles().get(0).getName().equals("Eater"))
						representation.traceEater(this.getMass(), this.accel.size,
								this.lastCoordinates, this.coordinates);
					// reset acceleration for next step.
					this.accel = PhysicsVector.nullVector;
				}
				if (!nullSpeed) {
					final PhysicsVector frictionForce = this.friction
							.getFrictionFromSpeed(this.speed, this.formFactor,
									this.mass);
					accel(frictionForce);
					this.temperature += frictionForce.size;
				}
			}
			updateAbsoluteBlackBodyRadiation();
		}
	}

	public void print() {
		this.shouldPrint = true;
	}

	public void sanityCheck() throws AgentConfigurationError {
		if (this.getMass() <= 0) {
			throw new AgentConfigurationError("mass must be > zero");
		}
		final double f = this.getFormFactor();
		if ((f <= 0) || (f >= 1)) {
			throw new AgentConfigurationError(
					"formFactor must belong to ]0, 1[");
		}
		if (this.getTemperature() <= 0) {
			throw new AgentConfigurationError("temperature must be > zero");
		}
	}

	public void setAgentRef(final int index) {
		this.agentRef = index;
	}

	public void setCoordinatesFrom(final PhysicalInfo physicalInfo) {
		this.coordinates = physicalInfo.coordinates.approximateCopy();
	}

	public void setMass(final double mass) throws AgentConfigurationError {
		this.mass = mass;
		if (mass == 0) {
			throw new AgentConfigurationError("mass 0");
		}
	}
	
	public void setParity(final int parity) {
		this.parity = parity;
	}

	public void setRadiationAbsorption(final double radiationAbsorption) {
		this.radiationAbsorption = radiationAbsorption;
	}

	public void setTemperature(final double temperature) {
		this.temperature = temperature;
		updateAbsoluteBlackBodyRadiation();
	}

	@Override
	public String toString() {
		return super.toString() + " at " + this.speed;
	}

	private void updateAbsoluteBlackBodyRadiation() {
		final double radTemp = this.k * this.temperature * this.temperature
				* this.temperature * this.temperature;
		this.blackBodyRadiation = radTemp * this.formFactor;
	}

	private void updatePosition() {
		this.lastCoordinates = this.coordinates.copy();
		this.coordinates.translate(this.speed);
	}

	private void updateSpeed() {
		if (this.accel.size > 0) {
			this.speed = this.speed.add(this.accel);
		}
	}

	public void drawCircle() {
		this.shouldDrawCircle = true;
	}

	public boolean isMoving() {
		return moving;
	}
	public void setInteraction(boolean b){
		this.canInteract=b;
	}
	public boolean getInteraction(){
		return this.canInteract;
	}
	
	//ajout de ma part
	public PhysicsVector getSpeedVector(){
		return this.speed;
	}
	
	//ajout de ma part
	public PhysicsVector getAccelerationVector(){
		return this.accel;
	}
}
