package multi_agent_painting.mas.roles;

import multi_agent_painting.mas.behaviours.Behaviours;
import tools.appControl.Logger;

import java.util.ArrayList;

/**
 * Created by Epulapp on 12/01/2017.
 */
public interface Rolable {

    public ArrayList<Behaviours> getBehaviours();

    public void addBehaviour(Behaviours behave);

    public ArrayList<Behaviours> getEnabledBehaviours();
}
