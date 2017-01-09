package multi_agent_painting.mas.roles;

import java.util.ArrayList;

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
public class Role {
	private String name;
	private ArrayList<Behaviours> behaviours;
	private ArrayList<Behaviours>	enabledBehaviours;
	
	/**
	 * Default builder without parameters
	 */
	@Deprecated
	public Role(){
		this.name= "";
		this.behaviours = new ArrayList<Behaviours>();
		this.enabledBehaviours = new ArrayList<Behaviours>();
	}
	
	/**
	 * Constructor with name
	 * @param name : String - Name of the role
	 * 
	 */
	public Role(String name){
		this.name = name;
		this.behaviours = new ArrayList<Behaviours>();
		this.enabledBehaviours = new ArrayList<Behaviours>();
	}
	
	/**
	 * Constructor by copying
	 * @param name : String - Name of the role
	 * @param roles : ArrayList<Behaviours> - List of behaviors attached to the role
	 */
	public Role(String name, ArrayList<Behaviours> roles){
		this.name = name;
		this.behaviours = new ArrayList<Behaviours>();
		this.enabledBehaviours = new ArrayList<Behaviours>();
		
		this.behaviours.addAll(roles);
		this.enabledBehaviours.addAll(roles);
	}
	
	/**
	 * Method providing the name of the role
	 * @return this.name;
	 */
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
	 * Method to remove a behavior form the current list.
	 * @param behave
	 */
	public void delBehaviour(Behaviours behave){
		if(this.behaviours.contains(behave)){
			this.behaviours.remove(behave);
			this.enabledBehaviours.remove(behave);
		}else{
			Logger.info("This behavior isn't in the current list : " + behave.getClass().toString());
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
