package multi_agent_painting.mas.exceptions;

import java.lang.reflect.Method;


import multi_agent_painting.mas.agents.AbstractAgent;
import multi_agent_painting.mas.agents.Agent;
import tools.appControl.Logger;

public class AgentRuntimeException extends Exception {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= -7416520642567693592L;

	public AgentRuntimeException(final Exception e) {
		super(e);
	}

	public AgentRuntimeException(final Exception e, final AbstractAgent agent,
			final Method behaviour) {
		super(e);
		Logger.critical("runtime error. while " + agent.getClass()
				+ " tried to express following behaviour: "
				+ behaviour.toString());
	}

	public AgentRuntimeException(final String msg) {
		super(msg);
	}

}
