package multi_agent_painting.physics.laws;

import multi_agent_painting.mas.MASConfiguration;
import multi_agent_painting.mas.Mas;

/**
 * Created by Epulapp on 17/01/2017.
 */
public class AgentsInteractionFactory {

    private static final AgentsInteractionFactory INSTANCE = new AgentsInteractionFactory();

    private AgentsInteractionFactory(){}

    public static AgentsInteractionFactory getInstance(){
        return INSTANCE;
    }

    public AgentsInteraction createAgentInteraction(String className, MASConfiguration property){
        switch (className){
            case "AgentsDodge":
                return new AgentsDodge(property);
            case "Gravity":
                return new Gravity(property);
            case "Radiation":
                return new Radiation(property);
            case "DodgePainter":
                return new DodgePainter(property);
            case "ListenToMusic":
                return new ListenToMusic(property);
            case "HotBody":
                return new HotBody(property);
            case "HeavytBody":
                return new HeavytBody(property);
            case "SpeedDown":
                return new SpeedDown(property);
            case "AgentsCollision":
                return new AgentsCollision(property);
            default:
                return null;
        }
    }
}
