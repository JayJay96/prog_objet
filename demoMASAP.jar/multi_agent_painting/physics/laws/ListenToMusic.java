package multi_agent_painting.physics.laws;

	 

import multi_agent_painting.mas.MASConfiguration;
import multi_agent_painting.mas.agents.AbstractAgent;
import multi_agent_painting.mas.agents.Agent;
	import multi_agent_painting.mas.agents.PhysicalForces;
	import multi_agent_painting.mas.exceptions.AgentRuntimeException;
	import multi_agent_painting.physics.PhysicsVector;
	import multi_agent_painting.physics.Space;
	import tools.appControl.Logger;
	import tools.appControl.RandomSource;
import tools.drawing.PhysicalInfo;

public class ListenToMusic  extends AgentsInteraction {

	
	


		private int distance;

		public ListenToMusic(final MASConfiguration config) {
			super(PhysicProperty.PARITY);
			this.distance=config.initialListenableDistance;
		}

		@Override
		public void addPhysicsValues(final AbstractAgent hostingAgent) {
		
		}

		@Override
		public PhysicalForces interact(
				final AbstractAgent hostingAgent,
				final PhysicsVector vector,
				final PhysicalInfo bodyPhysicalInfo,
				Space space) throws AgentRuntimeException {
		
			//Agent closerAgent = space.getAgentBis(bodyPhysicalInfo);
			
			
			
			
			if(bodyPhysicalInfo.getRoleName().equals("Musical")){
				PhysicalInfo nextAgentBody = hostingAgent.getPhysicalInfo();
				PhysicsVector distanceVector=new PhysicsVector(nextAgentBody.getCoordinates(),bodyPhysicalInfo.getCoordinates());
				
				hostingAgent.getPhysicalInfo().setInteraction(distanceVector.size <= this.distance);
			}
			
			
			return new PhysicalForces(
					PhysicsVector.nullVector,
					hostingAgent.getPhysicalInfo().getTemperature(), hostingAgent.getPhysicalInfo().getMass(), false);
		}

	

	
}
