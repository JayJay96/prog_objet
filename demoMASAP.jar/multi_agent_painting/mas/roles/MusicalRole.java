package multi_agent_painting.mas.roles;

import multi_agent_painting.mas.Mas;
import multi_agent_painting.mas.behaviours.Behaviours;
import multi_agent_painting.mas.behaviours.lib.BehavioursFactory;
import multi_agent_painting.mas.behaviours.lib.InteractBehaviour;
import multi_agent_painting.mas.exceptions.RoleInitException;
import multi_agent_painting.physics.laws.AgentsDodge;
import multi_agent_painting.physics.laws.AgentsInteractionFactory;
import multi_agent_painting.physics.laws.Gravity;
import multi_agent_painting.physics.laws.Radiation;

/**
 * Created by Epulapp on 12/01/2017.
 */
public class MusicalRole extends AbstractRole{
    public MusicalRole(String name) {
        super(name);
    }

    public MusicalRole() {
    }

    public MusicalRole(Boolean b) throws RoleInitException {
        super(b);
        this.addBehaviour(BehavioursFactory.getInstance().createBehaviours(
                AgentsInteractionFactory.getInstance().createAgentInteraction("Gravity",Mas.config)));

        this.addBehaviour(BehavioursFactory.getInstance().createBehaviours(
                AgentsInteractionFactory.getInstance().createAgentInteraction("Radiation",Mas.config)));

        this.addBehaviour(BehavioursFactory.getInstance().createBehaviours(
                AgentsInteractionFactory.getInstance().createAgentInteraction("AgentsDodge",Mas.config)));
    }
}
