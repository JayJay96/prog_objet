package multi_agent_painting.mas;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.NoSuchElementException;

import multi_agent_painting.applet.panes.drawingPane.Spinner;
import multi_agent_painting.mas.agents.AbstractAgent;
import multi_agent_painting.mas.agents.AgentFactory;
import multi_agent_painting.mas.behaviours.Behaviours;
import multi_agent_painting.mas.behaviours.lib.InteractBehaviour;
import multi_agent_painting.mas.exceptions.AgentConfigurationError;
import multi_agent_painting.mas.exceptions.AgentInitException;
import multi_agent_painting.mas.exceptions.AgentRuntimeException;
import multi_agent_painting.mas.exceptions.KernelException;
import multi_agent_painting.mas.exceptions.MasException;
import multi_agent_painting.mas.exceptions.MasInitException;
import multi_agent_painting.mas.exceptions.RoleInitException;
import multi_agent_painting.mas.roles.*;
import multi_agent_painting.physics.Space;
import multi_agent_painting.physics.laws.AgentsCollision;
import multi_agent_painting.physics.laws.AgentsDodge;
import multi_agent_painting.physics.laws.Gravity;
import multi_agent_painting.physics.laws.ListenToMusic;
import multi_agent_painting.physics.laws.Radiation;
import multi_agent_painting.physics.laws.speedDown;
import tools.appControl.Logger;
import tools.appControl.StoppableRunnable;

public class Kernel implements StoppableRunnable {

	private volatile KernelState	state			= KernelState.PLAY;
	private Messenger				messenger;
	protected Scheduler				sched;
	private boolean					shutdown		= false;

	private final ArrayList<AbstractAgent>	agents			= new ArrayList<AbstractAgent>();
	private Space					space;
	private boolean					ready;
	private Spinner					spinner;
	private final LinkedList<AbstractAgent>	agentsToRemove	= new LinkedList<AbstractAgent>();

	private String					lastCyclesNb	= " ";
	private long					lastCycleDate;
	private long					callNb;
	private long					previousCallNb;
	private HashMap<AbstractAgent, Double>	agentsFrequency;

	public Kernel() {
	}

	public boolean addAgent(final AbstractAgent a) {
		try {
			a.init();
		} catch (final AgentInitException e) {
			e.printStackTrace();
			return false;
		}
		return this.agents.add(a);
	}

	private void cyclesPerSecond() {
		this.callNb++;
		final long now = System.currentTimeMillis();
		if (now - this.lastCycleDate > 1000) {
			this.lastCyclesNb = ((this.callNb - this.previousCallNb) + " cps");
			this.lastCycleDate = now;
			this.previousCallNb = this.callNb;
		}
	}

	/**
	 * FIXME: dirty.<br>
	 * Usage: if called with null argument, will clean up the list to be
	 * cleaned. Otherwise will add an agent to the agentsToRemove list.
	 * 
	 * @param agent
	 */
	private synchronized void delAgentListManagement(final AbstractAgent agent) {
		if (agent == null) {
			AbstractAgent nextAgent;
			try {
				do {
					nextAgent = this.agentsToRemove.pop();
					this.space.remove(nextAgent);
					this.agents.remove(nextAgent);
				} while (true);
			} catch (final NoSuchElementException e) {
			}
		} else {
			this.agentsToRemove.add(agent);
		}
	}

	private void detectEnd() throws KernelException {
			final int agentsNb = this.agents.size();
			if (agentsNb == 0 || !atLeastOneMovingAgent()) {
				throw new KernelException(
						"0 moving agent left, stopping kernel.");
			}
	}

	private boolean atLeastOneMovingAgent() {
		for (AbstractAgent a : this.agents) {
			if (a.isMoving()) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void finalize() {
		// because we want to know if this MAS is actually deleted.
		Logger.info("kernel " + this.hashCode() + "deleted.");
	}

	public String getLastCyclesNb() {
		return this.lastCyclesNb;
	}

	public KernelState getState() {
		return this.state;
	}

	public void init(final Space space) throws MasInitException {
		if (this.ready) {
			throw new MasInitException("init to be called only once.");
		}
		this.space = space;
		this.ready = true;
		this.sched = new Scheduler();
		if(this.messenger == null){
			this.messenger = new Messenger(
					this.space.getMap(),
					agentsFrequency
				);
		}else{
		}
		this.sched.setMessenger(messenger);
	}

	public void nextStep() throws AgentInitException, AgentRuntimeException,
			MasException {
		this.sched.schedule(new ArrayList<AbstractAgent>(this.agents));
	}

	public void removeAgent(final AbstractAgent agent) {
		delAgentListManagement(agent);
	}

	@Override
	public void run() {
		Logger.critical("starting kernel");
		while (!this.shutdown) {
			
			while (this.state != KernelState.PLAY) {
				Thread.yield();
			}
			delAgentListManagement(null);
			try {
				this.sched.schedule(new ArrayList<AbstractAgent>(this.agents));
				this.space.nextStep();
				if (this.spinner != null) {
					this.spinner.rotate();
				}
				if (this.callNb % AgentsCollision.collisionsNbResetFreq == 0) {
					AgentsCollision.collisionsSpawned = 0;
				}
				cyclesPerSecond();
				detectEnd();

			} catch (final Exception e) {
				shutdown();
				Logger.critical("schedule "+e.getMessage());
				if (!(e instanceof KernelException)) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public void addPainterAgent() throws RoleInitException, AgentInitException, AgentConfigurationError, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
	  	Double[] listOfFrequency = new Double[2048];
		for(int i = 0 ; i<2048 ; ++i){
			listOfFrequency[i] = (i*44100.0/4096);
		}


		AbstractRole paintingRole = RoleFactory.getInstance().createRoleInit(PainterRole.class);
		final AbstractAgent a = AgentFactory.getInstance().createAgent(space, this, paintingRole);
		a.getPhysicalInfo().setInteraction(false);
		a.getPhysicalInfo().setRoleName("Painter");
		this.addAgent(a);
		Double value = listOfFrequency[(int)(Math.random()*listOfFrequency.length)];
		if(agentsFrequency != null)	agentsFrequency.put(a, value);
		else{
			agentsFrequency = new HashMap<AbstractAgent, Double>();
			agentsFrequency.put(a, value);
		}
	}

	public void addMusicalAgent() throws RoleInitException, AgentInitException, AgentConfigurationError, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
		AbstractRole musicalRole = RoleFactory.getInstance().createRoleInit(MusicalRole.class);
        final AbstractAgent a = AgentFactory.getInstance().createAgent(space, this, musicalRole);
        a.getPhysicalInfo().setRoleName("Musical");
        this.addAgent(a);
	}
	public void addEaterAgent() throws RoleInitException, AgentInitException, AgentConfigurationError, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
		AbstractRole eaterRole = RoleFactory.getInstance().createRoleInit(EaterRole.class);
		final AbstractAgent a = AgentFactory.getInstance().createAgent(space, this, eaterRole);
		a.getPhysicalInfo().setRoleName("Eater");
		this.addAgent(a);
	}
	
	public void setState(final KernelState state) {
		this.state = state;
	}

	@Override
	public void shutdown() {
		if (!this.shutdown) {
			this.shutdown = true;
			// this.spinner.shutdown();
			this.spinner = null;
			for (final AbstractAgent agent : this.agents) {
				try {
					agent.die();
				} catch (final MasException e) {
					e.printStackTrace();
				}
			}
			this.space.shutdown();
			this.space = null;
		}
	}

	public void start() {
		if (this.ready) {
			Logger.critical("Starting with " + this.agents.size() + " agents");
			new Thread(this).start();
		}
	}

	public String getCallNb() {
		return String.valueOf(callNb);
	}

	
	public boolean isFileOrEntry(){
		return this.space.getFileOrEntry();
	}
	
	public File getFile(){
		return this.space.getFileToPlay();
	}

	public void setAgentsFrequency(HashMap<AbstractAgent, Double> agentsFrequency) {
		this.agentsFrequency = agentsFrequency;		
		this.messenger.setAgentsFrequency(agentsFrequency);
		this.messenger.init();
	}
	
}
