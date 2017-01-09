package multi_agent_painting.mas.exceptions;

public class AgentInitException extends Exception {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= -2645071517060866985L;

	public AgentInitException(final Exception e) {
		super(e);
	}

	public AgentInitException(final String msg) {
		super(msg);
	}

}
