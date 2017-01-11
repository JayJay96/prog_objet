package multi_agent_painting.physics;

import java.awt.Dimension;
import java.io.File;
import java.util.HashMap;
import java.util.NoSuchElementException;

import multi_agent_painting.applet.panes.drawingPane.DrawPanel;
import multi_agent_painting.mas.agents.Agent;
import multi_agent_painting.mas.exceptions.MasInitException;
import multi_agent_painting.physics.laws.Friction;
import tools.appControl.RandomSource;
import tools.drawing.Coordinates;
import tools.drawing.PhysicalInfo;

public class Space {

	public final double						xsize, ysize;
	protected HashMap<Agent, PhysicalInfo>	agentsPositions	= new HashMap<Agent, PhysicalInfo>();
	protected HashMap<Agent, PhysicalInfo>	newAgents		= new HashMap<Agent, PhysicalInfo>();
	private Friction						friction;

	private DrawPanel						representation;
	final double							halfInsertionSize;
	private final double					interactionRadius;
	private boolean							shutdown;
	private final double					k;
	private final HashMap<Double, Double> map;
	
	public Space(final Friction friction, final DrawPanel drawPanel,
			final int insertionZoneSize, final double interactionRadius,
			final double k, final HashMap<Double, Double> map) throws MasInitException {
		final Dimension dimension = drawPanel.getSize();
		this.xsize = dimension.getWidth();
		this.ysize = dimension.getHeight();
		// safety tests
		if ((this.xsize < 1) || (this.ysize < 1)) {
			throw new MasInitException("Space dimensions must be > 0");
		}
		this.friction = friction;
		this.representation = drawPanel;
		synchronized (this.agentsPositions) {
			this.representation.setSpace(this);
		}
		this.interactionRadius = interactionRadius;
		this.halfInsertionSize = insertionZoneSize / 2;
		this.k = k;
		this.map = map;
	}

	private PhysicalInfo addAgent(final Agent agent) {
		synchronized (this.agentsPositions) {
			synchronized (this.newAgents) {
				assert this.newAgents.get(agent) == null;
				assert this.agentsPositions.get(agent) == null;

				double x, y;

//				x = RandomSource.randomGaussianDoubleBetween(0, this.xsize);
//				y = RandomSource.randomGaussianDoubleBetween(0, this.ysize);
//				
				do {
					x = RandomSource.randomGaussianDoubleBetween(0, this.xsize);
					y = RandomSource.randomGaussianDoubleBetween(0, this.ysize);
				} while (this.representation.centerPoint.distance(x, y) >= this.halfInsertionSize);
				final double formFactor = Math.log10(RandomSource
						.randomGaussianDoubleBetween(10, 1000)) / 18;

				final PhysicalInfo physicalInfo = new PhysicalInfo(x, y,
						this.friction, formFactor, this.k);
				this.newAgents.put(agent, physicalInfo);

				return physicalInfo;
			}
		}
	}

	@Override
	public void finalize() {
		System.out.println("Space garbage collected.");
	}

	public Agent getAgent(final PhysicalInfo bodyPhysicalInfo) {
		
		final int fixedAgentRef = bodyPhysicalInfo.getAgentRef();
		synchronized (this.agentsPositions) {
			for (final Agent agent : this.agentsPositions.keySet()) {
				final PhysicalInfo physicalInfo = this.agentsPositions
						.get(agent);
				if (physicalInfo.getAgentRef() == fixedAgentRef) {
					return agent;
				}
			}
		}
		throw new NoSuchElementException();
		
	}

	public Double getAgentMass(final Agent hostingAgent) {
		return getPhysicsInfo(hostingAgent).getMass();
	}

	public HashMap<PhysicsVector, PhysicalInfo> getAgents(final Agent requester) {
		final PhysicalInfo requesterPhysicsInfo = getPhysicsInfo(requester);
		final Coordinates requesterCoords = requesterPhysicsInfo
				.getCoordinates();

		final HashMap<PhysicsVector, PhysicalInfo> result = new HashMap<PhysicsVector, PhysicalInfo>();
		synchronized (this.agentsPositions) {
			for (final PhysicalInfo physicalInfo : this.agentsPositions.values()) {
				if (requesterPhysicsInfo != physicalInfo) {
					final Coordinates otherAgentCoordinates = physicalInfo
							.getCoordinates();
					final double distance = otherAgentCoordinates
							.distance(requesterCoords);
					if ((distance < this.interactionRadius)) {
						result.put(new PhysicsVector(requesterCoords,
								otherAgentCoordinates), physicalInfo);
					}
				}
			}
		}
		return result;
	}

	public Object[] getPhysicalInfos() {
		synchronized (this.agentsPositions) {
			return this.agentsPositions.values().toArray();
		}
	}

	public PhysicalInfo getPhysicsInfo(final Agent agent) {
		synchronized (this.agentsPositions) {
			PhysicalInfo p = this.agentsPositions.get(agent);
			if (p == null) {
				synchronized (this.newAgents) {
					p = this.newAgents.get(agent);
					if (p == null) {
						p = this.addAgent(agent);
					}
				}
			}
			return p;
		}
	}

	public void nextStep() {
		synchronized (this.agentsPositions) {
			for (final PhysicalInfo physicalInfo : this.agentsPositions
					.values()) {
//				if(this.getAgentBis(physicalInfo).getRoles().get(0).getName().equals("Painter"))
					physicalInfo.nextStep(this.representation,physicalInfo,this);
			}
			synchronized (this.newAgents) {
				if (!this.newAgents.isEmpty()) {
					this.agentsPositions.putAll(this.newAgents);
				}
				this.newAgents.clear();
			}
		}
	}

	//@SuppressWarnings("unchecked")
	public void remove(final Agent agent) {
		synchronized (this.agentsPositions) {
			this.agentsPositions.remove(agent);
		}
	}

	public void setAgentTemperature(final Agent agent, final double temperature) {
		this.getPhysicsInfo(agent).setTemperature(temperature);
	}

	public void setRadiationAbsorption(final Agent agent, final double temp) {
		this.getPhysicsInfo(agent).setRadiationAbsorption(temp);
	}

	public void shutdown() {

		if (!this.shutdown) {
			synchronized (this.agentsPositions) {
				this.agentsPositions = null;
				this.newAgents = null;
				this.representation.shutdown();
				this.representation = null;
				this.friction = null;
				this.shutdown = true;
			}
		}
	}
	
	public Agent getAgentBis(PhysicalInfo pI){
		synchronized (this.agentsPositions) {
		for(Agent a : this.agentsPositions.keySet()){
			if(a.getPhysicalInfo().equals(pI))
				return a;
		}
		}
		
		return null;
	}
	
	public File getFileToPlay(){
		return this.representation.getFileToPlay();
	}
	
	public boolean getFileOrEntry(){
		return this.representation.getFileOrEntry();
	}
	
	public HashMap<Double, Double> getMap(){
		return this.map;
	}
	
}
