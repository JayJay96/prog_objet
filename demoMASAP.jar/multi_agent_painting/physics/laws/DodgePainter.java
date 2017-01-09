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

public class DodgePainter extends AgentsInteraction {

	private final double	dodgeDistance;
	private final double	dodgeFactor=20;
	private final double	troubleDistance;

	public DodgePainter(final MASConfiguration config) {
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
			PhysicalInfo hostinfInfo=hostingAgent.getPhysicalInfo();
			double minMass,maxMass,massEcart, massMoy,mass;
			double minTemp,maxTemp,tempEcart,tempMoy,temp;
			Coordinates oldC= new Coordinates(0,0), newC= new Coordinates(0,0);
			change=PhysicsVector.nullVector;
			//Agent closerAgent = space.getAgentBis(bodyPhysicalInfo);
			
	/*		if(hostingAgent.getPhysicalInfo().getCoordinates().getX()>space.getX() ){	
				change=new PhysicsVector(hostingAgent.getPhysicalInfo().getLastCoordinates(),hostingAgent.getPhysicalInfo().getCoordinates());			
				change= new PhysicsVector((change.getXComponent()*-1)/8, change.getYComponent()/8);				
			}
			if(hostingAgent.getPhysicalInfo().getCoordinates().getX()<0){
				change=new PhysicsVector(hostingAgent.getPhysicalInfo().getLastCoordinates(),hostingAgent.getPhysicalInfo().getCoordinates());						
				change= new PhysicsVector((change.getXComponent()*-1)/8, change.getYComponent()/8);
				}
			if(hostingAgent.getPhysicalInfo().getCoordinates().getY()>space.getY() ){		
				change=new PhysicsVector(hostingAgent.getPhysicalInfo().getLastCoordinates(),hostingAgent.getPhysicalInfo().getCoordinates());				
				change= new PhysicsVector(change.getXComponent()/8, (change.getYComponent()*-1)/8);				
			}
			if(hostingAgent.getPhysicalInfo().getCoordinates().getY()<0){	
				change=new PhysicsVector(hostingAgent.getPhysicalInfo().getLastCoordinates(),hostingAgent.getPhysicalInfo().getCoordinates());					
				change= new PhysicsVector(change.getXComponent()/8, (change.getYComponent()*-1)/8);				
			}
			if(hostingAgent.getPhysicalInfo().getCoordinates().getX()>space.getX() || hostingAgent.getPhysicalInfo().getCoordinates().getX()<0){	
						
			}
			
			if(hostingAgent.getPhysicalInfo().getCoordinates().getY()>space.getY() || hostingAgent.getPhysicalInfo().getCoordinates().getY()<0){		
				
			}*/
			
			
			
			if (vector.size < this.dodgeDistance) {

				if(hostingAgent.getPhysicalInfo().getRoleName().equals(bodyPhysicalInfo.getRoleName())){
					result=new PhysicsVector(hostingAgent.getPhysicalInfo().getLastCoordinates(),hostingAgent.getPhysicalInfo().getCoordinates());
					result= new PhysicsVector(result.getXComponent()*-1, result.getYComponent()*-1);
					result=result.multiplyBy(2);
					result.substract(vector);
					if(hostinfInfo.getMass()>bodyPhysicalInfo.getMass()){
						minMass=bodyPhysicalInfo.getMass();
						maxMass=hostinfInfo.getMass();
					}else{
						maxMass=bodyPhysicalInfo.getMass();
						minMass=hostinfInfo.getMass();
					}
					if(hostinfInfo.getTemperature()>bodyPhysicalInfo.getTemperature()){
						minTemp=bodyPhysicalInfo.getTemperature();
						maxTemp=hostinfInfo.getTemperature();
					}else{
						maxTemp=bodyPhysicalInfo.getTemperature();
						minTemp=hostinfInfo.getTemperature();
					}
					tempEcart=maxTemp-minTemp;
					massEcart=maxMass-minMass;
					massMoy=(minMass+maxMass)/2;
					tempMoy=(minTemp+maxTemp)/2;
					
					mass=RandomSource.randomGaussianDoubleBetween(massMoy-massEcart,massMoy+massEcart);
					temp=RandomSource.randomGaussianDoubleBetween(tempMoy-tempEcart,tempMoy+tempEcart);
					result=result.add(change);
					return new PhysicalForces(result, temp, mass);
				}else{
					if(((int)(Math.random()*10000)%9999)==0){
						return new PhysicalForces(PhysicsVector.nullVector.add(change), 0, 0);
					}else{
						return new PhysicalForces(PhysicsVector.nullVector, 0, 0);
					}
				}
				
			}else{
				
				return new PhysicalForces(PhysicsVector.nullVector.add(change), 0, 0);
				
			}
	}

}
