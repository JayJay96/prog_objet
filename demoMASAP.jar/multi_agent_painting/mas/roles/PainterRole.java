package multi_agent_painting.mas.roles;

import multi_agent_painting.mas.Mas;
import multi_agent_painting.mas.behaviours.lib.BehavioursFactory;
import multi_agent_painting.mas.exceptions.RoleInitException;
import multi_agent_painting.physics.laws.*;

/**
 * Created by Epulapp on 12/01/2017.
 */
public class PainterRole extends AbstractRole {

    public PainterRole(String name) {
        super(name);
    }

    public  PainterRole() throws RoleInitException {}

    public PainterRole(Boolean b) throws RoleInitException {
        super(b);
        this.addBehaviour(BehavioursFactory.getInstance().createBehaviours(
                AgentsInteractionFactory.getInstance().createAgentInteraction("Gravity",Mas.config)));

        this.addBehaviour(BehavioursFactory.getInstance().createBehaviours(
                AgentsInteractionFactory.getInstance().createAgentInteraction("Radiation", Mas.config)));

        this.addBehaviour(BehavioursFactory.getInstance().createBehaviours(
                AgentsInteractionFactory.getInstance().createAgentInteraction("AgentsCollision", Mas.config)));

        this.addBehaviour(BehavioursFactory.getInstance().createBehaviours(
                AgentsInteractionFactory.getInstance().createAgentInteraction("ListenToMusic",Mas.config)));

        this.addBehaviour(BehavioursFactory.getInstance().createBehaviours(
                AgentsInteractionFactory.getInstance().createAgentInteraction("SpeedDown",Mas.config)));
    }
}
