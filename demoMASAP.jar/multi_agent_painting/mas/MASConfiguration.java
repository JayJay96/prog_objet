package multi_agent_painting.mas;

public class MASConfiguration {
	private static final int				defaultAgentsInitialNumber		= 55;
	private static final int				defaultAgentsMusicalInitialNumber		= 15;
	private static final int				defaultAgentsEaterInitialNumber		= 10;
	private static final int				defaultListenableDistance		= 50;
	/**
	 * from 0 to ... 10 and more ?
	 */
	private static final double				defaultG						= 0.001;

	private static final int				defaultInteractionRadius		= 2000;
	private static final double				defaultMaxMass					= 100000;

	private static final double				defaultMinMass					= 10;
	private static final double				defaultMaxTemp					= 5;
	private static final double				defaultMinTemp					= 0;
	/**
	 * from 10 to..
	 */
	private static final int				defaultInsertionZoneSize		= 500;

	private static final double				defaultCollisionDistance		= 0.5;

	private static final int				defaultCollisionsSpawnableNb	= 300;
	private static final int				defaultCollisionsNbResetFreq	= 1;
	private static final double				defaultK						= 0.01;

	public static final MASConfiguration	defaultConf						= new MASConfiguration(
																					MASConfiguration.defaultAgentsInitialNumber,
																					MASConfiguration.defaultG,
																					MASConfiguration.defaultInteractionRadius,
																					MASConfiguration.defaultMaxMass,
																					MASConfiguration.defaultMinMass,
																					MASConfiguration.defaultMaxTemp,
																					MASConfiguration.defaultMinTemp,
																					MASConfiguration.defaultInsertionZoneSize,
																					MASConfiguration.defaultCollisionDistance,
																					MASConfiguration.defaultCollisionsSpawnableNb,
																					MASConfiguration.defaultCollisionsNbResetFreq,
																					MASConfiguration.defaultK);
	public final int						agentsInitialNumber;
	public final int						musicalAgentsInitialNumber;
	public final int						eaterAgentsInitialNumber;	
	public final int						initialListenableDistance;

	public final double						G;
	public final int						interactionRadius;
	public final double						maxMass;
	public final double						minMass;
	public final double						maxTemp;
	public final double						minTemp;

	public final int						insertionZoneSize;
	public final double						collisionDistance;
	public final int						collisionsSpawnableNb;
	public final int						collisionsNbResetFreq;
	public final double						k;

	public MASConfiguration(final int agentsInitialNumber, final double g,
			final int interactionRadius, final double maxMass,
			final double minMass, final double maxTemp, final double minTemp,
			final int insertionZoneSize, final double collisionDistance,
			final int collisionsSpawnableNb, final int collisionsNbResetFreq,
			final double k) {
		super();
		this.eaterAgentsInitialNumber=this.defaultAgentsEaterInitialNumber;
		this.musicalAgentsInitialNumber=defaultAgentsMusicalInitialNumber;
		this.initialListenableDistance=this.defaultListenableDistance;
		this.agentsInitialNumber = agentsInitialNumber;
		this.G = g;
		this.interactionRadius = interactionRadius;
		this.maxMass = maxMass;
		this.minMass = minMass;
		this.maxTemp = maxTemp;
		this.minTemp = minTemp;
		this.insertionZoneSize = insertionZoneSize;
		this.collisionDistance = collisionDistance;
		this.collisionsSpawnableNb = collisionsSpawnableNb;
		this.collisionsNbResetFreq = collisionsNbResetFreq;
		this.k = k;
	}

}
