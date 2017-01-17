package multi_agent_painting.mas.agents;

import multi_agent_painting.mas.Kernel;
import multi_agent_painting.mas.roles.AbstractRole;
import multi_agent_painting.physics.Space;
import tools.appControl.Logger;
import tools.drawing.PhysicalInfo;

import java.util.ArrayList;

public abstract class AbstractAgent implements AgentWorkable{
    protected static int	nextIndex	= 0;

    protected static int getNextIndex() {
        AbstractAgent.nextIndex++;
        return AbstractAgent.nextIndex;
    }

    protected volatile boolean			ready		= false;
    public final Space					space;

    protected final Kernel				kernel;

    /**
     * This is the new role implentation
     */
    protected ArrayList<AbstractRole> roles;

    protected boolean						dead		= false;

    protected Double 						musicalValue = 0.0;
    protected Double						oldMusicalValue = musicalValue;

    final protected PhysicalInfo physicalInfo;

    public final int					index;
    protected boolean						explodable	= false;
    protected boolean						willDie;
    protected boolean						willExplode;

    public AbstractAgent(final Space space, final Kernel kernel) {
        this.roles = new ArrayList<AbstractRole>();
        this.space = space;
        this.kernel = kernel;
        this.physicalInfo = this.space.getPhysicsInfo(this);
        this.index = Agent.getNextIndex();
        this.physicalInfo.setAgentRef(this.index);
        this.musicalValue = 0.0;
        Logger.info("New agent " + this.index);
    }

    public void setMusicalValue(Double value){musicalValue = value;}
}
