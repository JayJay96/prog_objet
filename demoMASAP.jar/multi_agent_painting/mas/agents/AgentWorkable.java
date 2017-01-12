package multi_agent_painting.mas.agents;

import multi_agent_painting.mas.exceptions.AgentInitException;
import multi_agent_painting.mas.exceptions.AgentRuntimeException;
import multi_agent_painting.mas.exceptions.MasException;
import multi_agent_painting.mas.roles.AbstractRole;
import tools.drawing.PhysicalInfo;

import java.util.ArrayList;

public interface AgentWorkable {
    public void addRole(AbstractRole role) throws AgentInitException;

    public void addRoles(ArrayList<AbstractRole> role) throws AgentInitException;

    public void applyForces(final PhysicalForces forces);

    public void die() throws MasException;

    public void doDie();

    public void doExplode();

    public PhysicalInfo getPhysicalInfo();

    public void init() throws AgentInitException;

    public void live() throws AgentInitException, AgentRuntimeException, MasException;

    public boolean isMoving();

    public ArrayList<AbstractRole> getRoles();

    public void setMusicalValue(Double value);

    public ArrayList<Double> getMusicalValues();
}
