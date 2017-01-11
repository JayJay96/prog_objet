package multi_agent_painting.mas.agents;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import multi_agent_painting.mas.Kernel;
import multi_agent_painting.mas.behaviours.Behaviours;
import multi_agent_painting.mas.exceptions.AgentConfigurationError;
import multi_agent_painting.mas.exceptions.AgentInitException;
import multi_agent_painting.mas.exceptions.AgentRuntimeException;
import multi_agent_painting.mas.exceptions.ImmutableException;
import multi_agent_painting.mas.exceptions.MasException;
import multi_agent_painting.mas.exceptions.RoleInitException;
import multi_agent_painting.mas.roles.Role;
import multi_agent_painting.physics.PhysicsVector;
import multi_agent_painting.physics.Space;
//import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import tools.appControl.Logger;
import tools.appControl.RandomSource;
import tools.drawing.PhysicalInfo;

public class Agent extends AbstractAgent{

	private static int	nextIndex	= 0;

	private static int getNextIndex() {
		Agent.nextIndex++;
		return Agent.nextIndex;
	}

	private volatile boolean			ready		= false;
	public final Space					space;

	private final Kernel				kernel;

	/**
	 * This is the new role implentation
	 */
	private ArrayList<Role> 			roles;

	private boolean						dead		= false;

	private Double 						musicalValue = 0.0;
	private Double						oldMusicalValue = musicalValue;

	final private PhysicalInfo			physicalInfo;

	public final int					index;
	private boolean						explodable	= false;
	private boolean						willDie;
	private boolean						willExplode;

	public Agent(final Space space, final Kernel kernel) {
		this.roles = new ArrayList<Role>();		
		this.space = space;
		this.kernel = kernel;
		this.physicalInfo = this.space.getPhysicsInfo(this);
		this.index = Agent.getNextIndex();
		this.physicalInfo.setAgentRef(this.index);
		this.musicalValue = 0.0;
		Logger.info("New agent " + this.index);
	}

	
	public synchronized void addRole(Role role) throws AgentInitException {
		this.roles.add(role);
		for(Behaviours b : role.getBehaviours() ){
			//this.enabledBehaviours.add(b);
			try {
				b.init(this);
			} catch (final RoleInitException e) {
				throw new AgentInitException(e);
			}
			
		}
	}
	
	public synchronized void addRoles(ArrayList<Role> role) throws AgentInitException{
		this.roles.addAll(role);
		for(Role r : role){
			for(Behaviours b : r.getBehaviours()){
				//this.enabledBehaviours.add(b);
				try {
					b.init(this);
				} catch (final RoleInitException e) {
					throw new AgentInitException(e);
				}
			}
			
		}
		
	}

	public void applyForces(final PhysicalForces forces) {
		this.getPhysicalInfo().applyForces(forces);
	}

	
	/** 
	 * Adapt to the new role implantation
	 * 
	 * @return
	 * @throws AgentRuntimeException
	 */
	private Agent copy() throws AgentRuntimeException {
		final Agent newAgent = new Agent(this.space, this.kernel);

		newAgent.getPhysicalInfo().setCoordinatesFrom(this.getPhysicalInfo());
	
		try {
			newAgent.addRoles(this.roles);
		} catch (final AgentInitException e) {
			throw new AgentRuntimeException(e);
		}
		
		return newAgent;
	}

	public synchronized void die() throws MasException {
		if (!this.dead) {
			this.dead = true;
			this.space.remove(this);
			this.kernel.removeAgent(this);
		}
	}

	public void doDie() {
		// si l'agent n'explose pas il ne meurt pas => supposition
		this.willDie = true;
	}

	public void doExplode() {
		this.willExplode = true;
	}

	private void explode() throws AgentRuntimeException, MasException {
		if (this.explodable) {
			final int clonesNb = RandomSource.randomIntBetween0And(2) + 2;
			final double mass = this.physicalInfo.getMass() / (clonesNb + 2);
			double speed = this.physicalInfo.getSpeed();
			double temperature = this.physicalInfo.getTemperature() + 2;
			for (int i = 0; i < clonesNb; i++) {
				final Agent newAgent = copy();
				final PhysicalInfo body = newAgent.getPhysicalInfo();
				try {
					body.setMass(mass);
				} catch (final AgentConfigurationError e) {
					throw new AgentRuntimeException(e);
				}

				body.applyForces(new PhysicalForces(new PhysicsVector(
						RandomSource.randomizer.nextGaussian() * speed,
						RandomSource.randomizer.nextGaussian() * speed),
						temperature, 0));
				this.kernel.addAgent(newAgent);
			}
			Logger.info("Agent " + this.index + " just exploded into "
					+ clonesNb + " parts.");
			die();
		}
	}

	@Override
	public void finalize() {
		Logger.info("agent " + this.index + " garbage collected.");
	}

	public PhysicalInfo getPhysicalInfo() {
		return this.physicalInfo;
	}

	public synchronized final void init() throws AgentInitException {

		final StackTraceElement stackTraceElement = Thread.currentThread()
				.getStackTrace()[2];
		final String callerName = stackTraceElement.getClassName();
		if (!callerName.equals(multi_agent_painting.mas.Kernel.class.getName())) {
			throw new AgentInitException(
					"Init must be called by kernel. (caller name: "
							+ callerName + ")");
		}
		// safety tests
		if (this.ready) {
			throw new AgentInitException("Agent " + this.index
					+ ": init() should be called only once.");
		}
		try {
			this.physicalInfo.sanityCheck();
		} catch (final AgentConfigurationError e) {
			throw new AgentInitException(e);
		}

		// initialization
		this.ready = true;
	}

	//ajout de ma part - phenomene que je ne m'explique pas à partir de la décelleration d'une certaine vitesse
	//les agents restent sur le bord puis repartent
	private void bounceWhenLimitReached(){
		PhysicalInfo myPhysicalInfo = this.space.getPhysicsInfo(this);
		if(this.limitReach() && this.getRoles().get(0).getName().equals("Painter")){
			if((this.space.xsize - 1) < myPhysicalInfo.getCoordinates().getX() || myPhysicalInfo.getCoordinates().getX() <= 1 ){
				//changement de la vitesse
				if(myPhysicalInfo.getSpeedVector().getSize() > 2 && Math.abs(myPhysicalInfo.getSpeedVector().getSize() - 2) > 0.5){
					myPhysicalInfo.getSpeedVector().setXComponent(-0 * myPhysicalInfo.getSpeedVector().getXComponent());
					myPhysicalInfo.getSpeedVector().setYComponent(0 * myPhysicalInfo.getSpeedVector().getYComponent());
					
					myPhysicalInfo.getAccelerationVector().setXComponent(-0 *  myPhysicalInfo.getAccelerationVector().getXComponent());
					myPhysicalInfo.getAccelerationVector().setYComponent(0 * myPhysicalInfo.getAccelerationVector().getYComponent());
					

				}
				else{				
					myPhysicalInfo.getSpeedVector().setXComponent(-myPhysicalInfo.getSpeedVector().getXComponent());
					myPhysicalInfo.getAccelerationVector().setXComponent(-myPhysicalInfo.getAccelerationVector().getXComponent());
					
				}
			}
			
			if((this.space.ysize - 1) < myPhysicalInfo.getCoordinates().getY() || myPhysicalInfo.getCoordinates().getY() <= 1 ){
				
				//changement de la vitesse
				if(myPhysicalInfo.getSpeedVector().getSize() > 2 && Math.abs(myPhysicalInfo.getSpeedVector().getSize() - 2) > 0.5){
					myPhysicalInfo.getSpeedVector().setYComponent(-0 * myPhysicalInfo.getSpeedVector().getYComponent());
					myPhysicalInfo.getSpeedVector().setXComponent(0 * myPhysicalInfo.getSpeedVector().getXComponent());
					
					myPhysicalInfo.getAccelerationVector().setYComponent(-0  * myPhysicalInfo.getAccelerationVector().getYComponent());
					myPhysicalInfo.getAccelerationVector().setXComponent( 0 * myPhysicalInfo.getAccelerationVector().getXComponent());					
					

				}
				else{				
					myPhysicalInfo.getSpeedVector().setYComponent(-myPhysicalInfo.getSpeedVector().getYComponent());
					myPhysicalInfo.getAccelerationVector().setYComponent(-myPhysicalInfo.getAccelerationVector().getYComponent());
					
				}
			}
			
		}
	}
	
	private boolean limitReach(){
		PhysicalInfo myPhysicalInfo = this.space.getPhysicsInfo(this);
		return (((this.space.xsize - 5) < myPhysicalInfo.getCoordinates().getX() || (this.space.ysize - 5) < myPhysicalInfo.getCoordinates().getY())
				||(myPhysicalInfo.getCoordinates().getX() <= 5 || myPhysicalInfo.getCoordinates().getY() <= 5));
	}
	
	public void live() throws AgentInitException, AgentRuntimeException,
			MasException {
		// safety tests
		if (!this.ready) {
			throw new AgentInitException(
					"init() should be called before live()");
		}
		final StackTraceElement stackTraceElement = Thread.currentThread()
				.getStackTrace()[2];
		final String callerName = stackTraceElement.getClassName();
		if (!callerName.equals(multi_agent_painting.mas.Scheduler.class
				.getName())) {
			throw new AgentInitException(
					"live must be called by scheduler. (caller name: "
							+ callerName + ")");
		}
				
		if (!this.dead) {
			// fetch data
			final HashMap<PhysicsVector, PhysicalInfo> bodies = this.space
					.getAgents(this);
			
			//bornes du monde
			
			this.bounceWhenLimitReached();
			if (bodies.size() > 0) {
				final PhysicalForces reaction = PhysicalForces.NONE
						.modifiableCopy();				

				for (final PhysicsVector vector : bodies.keySet()) {
					
					for(Role r : this.roles){
						for (final Behaviours behaviour : r.getEnabledBehaviours()) {					
							// never called
							final PhysicalInfo bodyPhysicalInfo = bodies
									.get(vector);	
																	
							assert bodyPhysicalInfo.getMass() != 0;
							final PhysicalForces partialReact = behaviour.react(
									this, vector, bodyPhysicalInfo,space);
							try {
								reaction.add(partialReact);
							} catch (final ImmutableException e) {
								throw new AgentRuntimeException(e);
							}
						}
					}
				}
				// conclude
				applyForces(reaction);
				
				
			}
		}
		this.explodable = true;
		if (this.willExplode) {
			explode();
		}
		if (this.willDie) {
			die();
		}
	}

	public boolean isMoving() {
		return this.physicalInfo.isMoving();
	}
	
	/**
	 * Method to get the current roles
	 * assigned to this agent
	 * @return
	 */
	public ArrayList<Role> getRoles(){
		return this.roles;
	}


	public void setMusicalValue(Double value) {
		this.oldMusicalValue = this.musicalValue;
		this.musicalValue = value;		
	}
	
	public ArrayList<Double> getMusicalValues(){
		ArrayList<Double> musicalValues = new ArrayList<Double>();
		
		musicalValues.add(0,this.oldMusicalValue);
		musicalValues.add(1,this.musicalValue);
		
		return musicalValues;
	}


}
