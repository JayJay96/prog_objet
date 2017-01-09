package multi_agent_painting.physics.laws;

import multi_agent_painting.mas.MASConfiguration;
import multi_agent_painting.mas.agents.Agent;
import multi_agent_painting.mas.agents.PhysicalForces;
import multi_agent_painting.mas.exceptions.AgentConfigurationError;
import multi_agent_painting.mas.exceptions.AgentRuntimeException;
import multi_agent_painting.physics.PhysicsVector;
import multi_agent_painting.physics.Space;
import tools.appControl.Logger;
import tools.appControl.RandomSource;
import tools.drawing.Coordinates;
import tools.drawing.PhysicalInfo;

public class speedDown extends AgentsInteraction {

	private final double	dodgeDistance;
	private final double	dodgeFactor=10;
	private final double	troubleDistance;

	public speedDown(final MASConfiguration config) {
		super(PhysicProperty.PARITY);
		this.dodgeDistance = config.collisionDistance*dodgeFactor;
		this.troubleDistance=dodgeFactor*dodgeDistance*10;
	}

	@Override
	public void addPhysicsValues(final Agent hostingAgent)
	throws AgentConfigurationError {
	
	}		
	/**
	 * Behaviour between 2 musical agent
	 * @param hostingAgent
	 * @param vector
	 * @param bodyPhysicalInfo
	 * @param space
	 * @return
	 * @throws AgentRuntimeException
	 */
	/* (non-Javadoc)
	 * @see multi_agent_painting.physics.laws.AgentsInteraction#interact(multi_agent_painting.mas.agents.Agent, multi_agent_painting.physics.PhysicsVector, tools.drawing.PhysicalInfo, multi_agent_painting.physics.Space)
	 */
	@Override
	public PhysicalForces interact(
			final Agent hostingAgent,
			final PhysicsVector vector,
			final PhysicalInfo bodyPhysicalInfo,
			final Space space) throws AgentRuntimeException {
			PhysicsVector result, change;
			double mass;
			double temp;
			change=PhysicsVector.nullVector;
			
	
			
			
			if (vector.size < this.dodgeDistance) {
				if(!bodyPhysicalInfo.getRoleName().equals("Painter")){
					result=new PhysicsVector(hostingAgent.getPhysicalInfo().getLastCoordinates(),hostingAgent.getPhysicalInfo().getCoordinates());
					result= new PhysicsVector(result.getXComponent()*-1, result.getYComponent()*-1);
					result=result.multiplyBy(0.05);
					result.substract(vector);			
					
					mass=hostingAgent.getPhysicalInfo().getMass();
					temp=hostingAgent.getPhysicalInfo().getTemperature();
					result=result.add(change);
					return new PhysicalForces(result, temp, mass);
				}
				
				
			}
				
				return new PhysicalForces(PhysicsVector.nullVector.add(change), 0, 0);
				
			
	}

}
