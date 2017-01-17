package multi_agent_painting.mas.behaviours.lib;

import multi_agent_painting.mas.behaviours.Behaviours;
import multi_agent_painting.mas.behaviours.SoundPlayer;
import multi_agent_painting.mas.exceptions.RoleInitException;
import multi_agent_painting.physics.laws.AgentsInteraction;

/**
 * Created by Epulapp on 17/01/2017.
 */
public class BehavioursFactory {

    private static final BehavioursFactory INSTANCE = new BehavioursFactory();

    private BehavioursFactory(){}

    public static BehavioursFactory getInstance(){
        return INSTANCE;
    }

    public Behaviours createBehaviours(AgentsInteraction agent) throws RoleInitException {
        if(agent == null)
            return new SoundPlayer();
        else
            return new InteractBehaviour(agent);
    }
}
