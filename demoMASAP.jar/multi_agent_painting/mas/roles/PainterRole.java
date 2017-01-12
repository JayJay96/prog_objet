package multi_agent_painting.mas.roles;

import multi_agent_painting.mas.Mas;
import multi_agent_painting.mas.behaviours.Behaviours;
import multi_agent_painting.mas.behaviours.lib.InteractBehaviour;
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
        this.addBehaviour(new InteractBehaviour(new Gravity(Mas.config)));
        this.addBehaviour(new InteractBehaviour(new Radiation(Mas.config)));
        this.addBehaviour(new InteractBehaviour(new AgentsCollision(Mas.config)));
        this.addBehaviour(new InteractBehaviour(new ListenToMusic(Mas.config)));
        this.addBehaviour(new InteractBehaviour(new speedDown(Mas.config)));
    }
}
