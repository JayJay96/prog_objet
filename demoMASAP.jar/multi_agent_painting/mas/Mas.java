package multi_agent_painting.mas;

import java.util.HashMap;

import multi_agent_painting.applet.panes.drawingPane.DrawPanel;
import multi_agent_painting.mas.agents.AbstractAgent;
import multi_agent_painting.mas.agents.Agent;
import multi_agent_painting.mas.agents.AgentFactory;
import multi_agent_painting.mas.behaviours.Behaviours;
import multi_agent_painting.mas.behaviours.SoundPlayer;
import multi_agent_painting.mas.behaviours.lib.InteractBehaviour;
import multi_agent_painting.mas.exceptions.AgentConfigurationError;
import multi_agent_painting.mas.exceptions.AgentInitException;
import multi_agent_painting.mas.exceptions.MasInitException;
import multi_agent_painting.mas.exceptions.RoleInitException;
import multi_agent_painting.mas.roles.Role;
import multi_agent_painting.mas.sound.FilePlayer;
import multi_agent_painting.mas.sound.LineEntryPlayer;
import multi_agent_painting.physics.Space;
import multi_agent_painting.physics.laws.AgentsCollision;
import multi_agent_painting.physics.laws.AgentsDodge;
import multi_agent_painting.physics.laws.DodgePainter;
import multi_agent_painting.physics.laws.Drag;
import multi_agent_painting.physics.laws.Gravity;
import multi_agent_painting.physics.laws.HeavytBody;
import multi_agent_painting.physics.laws.HotBody;
import multi_agent_painting.physics.laws.ListenToMusic;
import multi_agent_painting.physics.laws.Radiation;
import multi_agent_painting.physics.laws.speedDown;
import tools.appControl.Logger;

public class Mas {

	public static MASConfiguration	config	= MASConfiguration.defaultConf;
	public static Role PaintingRole;
	public static Role heavyHotBodyRole;
	public static Role Sun;
	public static Role MusicalRole;
	public static Role EaterRole;
	private static Double[] listOfFrequency;
	private static HashMap<AbstractAgent, Double> agentsFrequency;
		
	/**
	 * Method that will initiate the MAS
	 * @param kernel
	 * @param space
	 * @throws RoleInitException
	 * @throws AgentInitException
	 * @throws AgentConfigurationError
	 */
	private static void defaultInit(final Kernel kernel, final Space space)
			throws RoleInitException, AgentInitException, AgentConfigurationError {
		
		listOfFrequency = new Double[2048];
		
		for(int i = 0 ; i<2048 ; ++i){
			listOfFrequency[i] = (i*44100.0/4096);
		}
		
		final Behaviours bGravitation = new InteractBehaviour(new Gravity(
				Mas.config));

		final Behaviours bRadiation = new InteractBehaviour(new Radiation(
				Mas.config));

		final Behaviours bCollision = new InteractBehaviour(
				new AgentsCollision(Mas.config));
		final Behaviours bdodge = new InteractBehaviour(
				new AgentsDodge(Mas.config));
		final Behaviours bdodgePainter = new InteractBehaviour(
				new DodgePainter(Mas.config));
		final Behaviours bListen = new InteractBehaviour(
				new ListenToMusic(Mas.config));
		final Behaviours bhot = new InteractBehaviour(
				new HotBody(Mas.config));
		final Behaviours bheavy = new InteractBehaviour(
						new HeavytBody(Mas.config));
		final Behaviours bspeed = new InteractBehaviour(
				new speedDown(Mas.config));
		final Behaviours sPlayer = new SoundPlayer();

		/**
		 *  Definition of the "Painter" role
		 *  which has the following behaviors :
		 *  - gravitation
		 *  - collision
		 *  - radiation
		 */		
		if(PaintingRole == null){
			PaintingRole = new Role("Painter");
			PaintingRole.addBehaviour(bGravitation);
			//PaintingRole.addBehaviour(bdodgePainter);
			PaintingRole.addBehaviour(bRadiation);
			PaintingRole.addBehaviour(bListen);
			PaintingRole.addBehaviour(bspeed);
			PaintingRole.addBehaviour(bCollision);
		}
		if(MusicalRole == null){
			MusicalRole = new Role("Musical");
			MusicalRole.addBehaviour(bGravitation);
			MusicalRole.addBehaviour(bdodge);
			MusicalRole.addBehaviour(bRadiation);
			MusicalRole.addBehaviour(sPlayer);
		}
		if(EaterRole == null){
			EaterRole = new Role("Eater");
			EaterRole.addBehaviour(bGravitation);
			EaterRole.addBehaviour(bdodge);
			//EaterRole.addBehaviour(bCollision);
			EaterRole.addBehaviour(bRadiation);
		}
		if(Sun == null){
			Sun=new Role("Sun");
			Sun.addBehaviour(bhot);
			Sun.addBehaviour(bheavy);
		}
			
		for (int i = 0; i < Mas.config.agentsInitialNumber; i++) {
					
			final AbstractAgent a = AgentFactory.getInstance().createAgent(space, kernel);
			
			a.addRole(PaintingRole);
			a.getPhysicalInfo().sanityCheck();
			a.getPhysicalInfo().setInteraction(false);
			a.getPhysicalInfo().setRoleName("Painter");
			kernel.addAgent(a);
			Double value = listOfFrequency[(int)(Math.random()*listOfFrequency.length)];
			if(agentsFrequency != null)	agentsFrequency.put(a, value);
			else{
				agentsFrequency = new HashMap<AbstractAgent, Double>();
				agentsFrequency.put(a, value);
			}
			
		}
	for (int i = 0; i < Mas.config.musicalAgentsInitialNumber; i++) {
						
			final AbstractAgent a = AgentFactory.getInstance().createAgent(space, kernel);
		
			a.addRole(MusicalRole);
			a.getPhysicalInfo().sanityCheck();
			a.getPhysicalInfo().setRoleName("Musical");
			kernel.addAgent(a);
		}
	for (int i = 0; i < Mas.config.eaterAgentsInitialNumber; i++) {
		
		final AbstractAgent a = AgentFactory.getInstance().createAgent(space, kernel);
		a.getPhysicalInfo().setRoleName("Eater");
		a.addRole(EaterRole);
		a.getPhysicalInfo().sanityCheck();
		kernel.addAgent(a);
	}
	//soleil à parametrer
	for(int i = 0; i < 0 ; i++){
		final AbstractAgent a = AgentFactory.getInstance().createAgent(space, kernel);
		a.getPhysicalInfo().setRoleName("Sun");
		
		a.addRole(Sun);
		a.getPhysicalInfo().sanityCheck();
		kernel.addAgent(a);
	}
	
	kernel.setAgentsFrequency(agentsFrequency);
	}
	

	/**TODO
	 * See what exactly is the aim of this function
	 * Try to create the role "heavyHotBodyRole" more general
	 *
	 * 
	 * @param kernel
	 * @param config
	 * @param space
	 * @param temp
	 * @param bCollision
	 * @param mass
	 * @throws RoleInitException
	 * @throws AgentInitException
	 * @throws AgentConfigurationError
	 */
	@SuppressWarnings("unused")
	private void heavyHotBody(
			final Kernel kernel,
			final MASConfiguration config,
			final Space space,
			final double temp,
			final Behaviours bCollision, double mass) throws RoleInitException,
			AgentInitException, AgentConfigurationError {
		Behaviours hotRadiation = new InteractBehaviour(new Radiation(temp));
		Behaviours heavy = new InteractBehaviour(new Gravity(config, mass));
		
		if(heavyHotBodyRole == null){
			heavyHotBodyRole = new Role("heavyHotBodyRole");
			heavyHotBodyRole.addBehaviour(hotRadiation);
			heavyHotBodyRole.addBehaviour(bCollision);
			heavyHotBodyRole.addBehaviour(heavy);
		}	
		AbstractAgent a = AgentFactory.getInstance().createAgent(space, kernel);
		
		a.addRole(heavyHotBodyRole);
		
		a.getPhysicalInfo().sanityCheck();
		kernel.addAgent(a);
	}
	
	private Kernel	kernel;
	private FilePlayer filePlayer;
	private LineEntryPlayer linePlayer;
	private Space	masSpace;
	
	/**
	 * Builder for the Mas Class
	 * 
	 * @param drawPanel
	 * @throws AgentInitException
	 * @throws RoleInitException
	 * @throws MasInitException
	 * @throws AgentConfigurationError
	 */
	public Mas(final DrawPanel drawPanel) throws AgentInitException,
			RoleInitException, MasInitException, AgentConfigurationError {
	
		this.masSpace = new Space(
				Drag.getSingleton(), 
				drawPanel,
				Mas.config.insertionZoneSize, Mas.config.interactionRadius,
				Mas.config.k,
				drawPanel.getMap()
				
		);
		
		this.kernel = drawPanel.getKernel();

		this.kernel.init(this.masSpace);

		if(this.kernel.isFileOrEntry()){
			this.filePlayer = drawPanel.getFilePlayer();
		}else{
			this.linePlayer = drawPanel.getLinePlayer();
		}
		
		Mas.defaultInit(this.kernel, this.masSpace);
		
		this.kernel.start();
		this.agentsFrequency = new HashMap<AbstractAgent, Double>();
		
		Logger.logLevel = Logger.LogLevel.INFO;
		
		
		if(this.kernel.isFileOrEntry()){
			this.filePlayer.start();
		}else{
			this.linePlayer.start();
		}
		
	}

	public void shutdown() {
		this.kernel.shutdown();
		this.kernel = null;
		this.masSpace.shutdown();
		this.masSpace = null;
		if(this.kernel.isFileOrEntry()){
			this.filePlayer.shutdown();
		}else{
			this.linePlayer.shutdown();
		}
		System.gc();
	}
}
