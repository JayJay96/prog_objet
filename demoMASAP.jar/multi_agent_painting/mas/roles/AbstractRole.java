package multi_agent_painting.mas.roles;

import java.util.ArrayList;
import java.util.function.BooleanSupplier;

import tools.appControl.Logger;

import multi_agent_painting.mas.behaviours.Behaviours;


/** 
 * @author Sylvain Le Leuch
 * 
 * 
 * This class will implement roles in the MAS painting platform.
 *
 * Each role will have a name, assort to a set of behaviors.
 * Agent must have a role, and not directly a behavior.
 * 
 * The agent will request a role to the kernel.
 * 
 *  
 *
 */
public abstract class AbstractRole implements Rolable{


	protected String name;
	protected ArrayList<Behaviours> behaviours;
	protected ArrayList<Behaviours>	enabledBehaviours;
	
	/**
	 * Constructor with name
	 * @param name : String - Name of the role
	 * 
	 */
	public AbstractRole(String name){
		this.name = name;
		this.behaviours = new ArrayList<Behaviours>();
		this.enabledBehaviours = new ArrayList<Behaviours>();
	}

	public AbstractRole(){
		this.name = "";
		this.behaviours = new ArrayList<Behaviours>();
		this.enabledBehaviours = new ArrayList<Behaviours>();
	}

	/**
	 * Use to init role with Behavior
	 * @param b
	 */
	public AbstractRole(Boolean b){
		this.name = "";
		this.behaviours = new ArrayList<Behaviours>();
		this.enabledBehaviours = new ArrayList<Behaviours>();
	}

	public String getName(){
		return this.name;
	}

	/**
	 * Method to get the list of all current behaviors attached
	 * to the role.
	 * @return
	 */
	public ArrayList<Behaviours> getBehaviours(){
		return this.behaviours;
	}

	/**
	 * Method to add a behavior to the current list
	 * @param behave
	 */
	public void addBehaviour(Behaviours behave){
		if(this.behaviours.contains(behave)){
			Logger.info("Behaviour already in the list : " + behave.getClass().toString());
		}else{
			this.behaviours.add(behave);
			this.enabledBehaviours.add(behave);
		}
	}

	/**
	 * Method to get all enable behaviors
	 * @return
	 */
	public ArrayList<Behaviours> getEnabledBehaviours(){
		return this.enabledBehaviours;
	}
}
