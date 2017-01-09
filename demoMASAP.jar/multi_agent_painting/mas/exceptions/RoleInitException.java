package multi_agent_painting.mas.exceptions;

public class RoleInitException extends Exception {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= -5395521471996376342L;

	public RoleInitException(final Exception e) {
		super(e);
	}

	public RoleInitException(final String string) {
		super(string);
	}

}
