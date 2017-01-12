package multi_agent_painting.applet.panes.drawingPane;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.RadialGradientPaint;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.JPanel;


import multi_agent_painting.mas.Kernel;
import multi_agent_painting.mas.Mas;
import multi_agent_painting.mas.agents.AbstractAgent;
import multi_agent_painting.mas.exceptions.AgentConfigurationError;
import multi_agent_painting.mas.exceptions.AgentInitException;
import multi_agent_painting.mas.exceptions.MasException;
import multi_agent_painting.mas.exceptions.RoleInitException;
import multi_agent_painting.physics.PhysicsVector;
import multi_agent_painting.physics.Space;
import tools.drawing.Coordinates;
import tools.drawing.PhysicalInfo;
import multi_agent_painting.mas.sound.FilePlayer;
import multi_agent_painting.mas.sound.LineEntryPlayer;

 
@SuppressWarnings("serial")
public class DrawPanel extends JPanel {

	private static final int	max_color_int	= 33554432;
	private final double		accel2LineWidth	= 5;

	protected final Dimension	dimension		= new Dimension(1000, 800);

	protected BasicStroke		stroke			= new BasicStroke(1.5f,
														BasicStroke.CAP_ROUND,
														BasicStroke.JOIN_ROUND,
														10.0f);
	protected BufferedImage		bimage;

	public final int			imageHeight;
	public final int			imageWidth;

	private Kernel				kernel;

	private volatile boolean	asyncWhiteningRequired;

	private final Graphics2D	imageGraphics;
	final int					max_distance_allowed;
	public final Point2D		centerPoint;
	private Space				space;
	private boolean				shutdown;
	private int					agentsNb;
	private int					infolabel_index;
	// Mp3 file
	private File				fileToPlay;
	// Boolean to indicate is it's file or entry to use
	private boolean				isFileOrEntry;
	private FilePlayer filePlayer;
	private LineEntryPlayer linePlayer;
	private HashMap<Double, Double> map;

	public DrawPanel(final Kernel masKernel, final int max_distance_allowed,boolean _playingOrRecording,File _song, LineEntryPlayer lineEntryPlayer, FilePlayer filePlayer, HashMap<Double, Double> map) {
		this.kernel = masKernel;
		this.max_distance_allowed = max_distance_allowed;
		this.imageWidth = this.dimension.getSize().width;
		this.imageHeight = this.dimension.getSize().height;
		setBounds(0, 0, this.imageWidth, this.imageHeight);
		setBorder(BorderFactory.createLineBorder(Color.black));

		setupMouseListeners();

		this.bimage = new BufferedImage(this.imageWidth, this.imageHeight,
				BufferedImage.TYPE_INT_RGB);
		this.imageGraphics = this.bimage.createGraphics();
		this.asyncWhiteningRequired = true;
		this.imageGraphics.setStroke(this.stroke);
		this.centerPoint = new Coordinates(this.imageWidth / 2,
				this.imageHeight / 2);
		
		this.isFileOrEntry = _playingOrRecording; 
		this.fileToPlay = _song;
		this.filePlayer = filePlayer;
		this.linePlayer = lineEntryPlayer;
		this.map = map;
	}

	private void addlabel(
			final Graphics g,
			final String message,
			final int rectXOrigin) {
		this.infolabel_index++;
		final int rectYOrigin = 5 + 25 * this.infolabel_index;
		g.setColor(Color.black);
		g.drawString(message, rectXOrigin + 5, rectYOrigin + 15);
	}

	private void backsScreen(final Graphics g, final int height, final int width) {
		g.setColor(Color.gray);
		final int rectXOrigin = 800;
		final int rectYOrigin = 0;
		g.fillRect(rectXOrigin, rectYOrigin, width, height);

	}

	public Kernel getKernel() {
		return this.kernel;
	}

	private boolean isVisible(final Point2D point) {
		final double x = point.getX();
		final double y = point.getY();
		return (x <= this.imageWidth) && (y <= this.imageHeight) && (x >= 0)
				&& (y >= 0);
	}

	public void leavePrint(
			final double mass,
			final Coordinates coordinates,
			final PhysicsVector speed) {
		final double speedSize = speed.size;
		final Polygon p = new Polygon();
		final int xOrigin = (int) coordinates.getX();
		final int yOrigin = (int) coordinates.getY();
		p.addPoint(xOrigin, yOrigin);
		final int xspeed = (int) (speed.getXComponent() * accel2LineWidth);
		final int yspeed = (int) (speed.getYComponent() * accel2LineWidth);
		final double massCoef = mass * accel2LineWidth;
		p.addPoint(xOrigin + xspeed + (int) (Math.signum(xspeed) * massCoef),
				yOrigin + yspeed);
		p.addPoint(xOrigin + xspeed, yspeed + yOrigin
				+ (int) (Math.signum(yspeed) * massCoef));
		this.imageGraphics.setXORMode(Color.black);
		gradientMode(coordinates, Coordinates.ORIGIN.distance(xspeed + 0.1,
				yspeed + 0.1), setColorFromBody(mass, speedSize));
		this.imageGraphics.fillPolygon(p);
		this.imageGraphics.setStroke(this.stroke);
		this.imageGraphics.setColor(Color.white);
		this.imageGraphics.drawPolygon(p);
		this.imageGraphics.setPaintMode();
	}

	private void paintAgentsInformation(final Graphics g) {
		final Object[] agents = this.space.getPhysicalInfos();
		//g.setColor(Color.red);
		for (final Object object : agents) {
		
			final PhysicalInfo physicalInfo = (PhysicalInfo) object;

			try {
				final Coordinates coord = physicalInfo.getCoordinates();
				if (isVisible(coord)) {
					
					// Seems to be the drawing of the agent's circle		}
					 AbstractAgent agent1 = this.space.getAgentBis(physicalInfo);
					 
//					//Change color to see the difference between each agents.
					 
					 
				if(physicalInfo.getRoleName().equals("Painter")){
						g.setColor(Color.red);
				}
					else if(agent1.getRoles().get(0).getName().equals("Eater")){
						g.setColor(Color.GREEN);
					}else if(agent1.getRoles().get(0).getName().equals("sun")){
						g.setColor(Color.YELLOW);
					}else{
						g.setColor(Color.BLUE);
					}
					
					g.drawOval((int) (coord.getX() - 2),
							(int) (coord.getY() - 2), 4, 4);
					
				} else if (tooFar(coord)) {
					try {
						final AbstractAgent agent = this.space.getAgent(physicalInfo);
						if(agent.getRoles().get(0).getName().equals("Musical") ){
							try {
								this.kernel.addMusicalAgent();
							} catch (RoleInitException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (AgentInitException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (AgentConfigurationError e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (NoSuchMethodException e) {
								e.printStackTrace();
							} catch (InstantiationException e) {
								e.printStackTrace();
							} catch (IllegalAccessException e) {
								e.printStackTrace();
							} catch (InvocationTargetException e) {
								e.printStackTrace();
							}
						}
						if(agent.getRoles().get(0).getName().equals("Eater") ){
							try {
								this.kernel.addEaterAgent();
							} catch (RoleInitException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (AgentInitException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (AgentConfigurationError e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (NoSuchMethodException e) {
								e.printStackTrace();
							} catch (InstantiationException e) {
								e.printStackTrace();
							} catch (IllegalAccessException e) {
								e.printStackTrace();
							} catch (InvocationTargetException e) {
								e.printStackTrace();
							}
						}
						if(agent.getRoles().get(0).getName().equals("Painter") ){
							try {
								this.kernel.addPainterAgent();
							} catch (RoleInitException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (AgentInitException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (AgentConfigurationError e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (NoSuchMethodException e) {
								e.printStackTrace();
							} catch (InstantiationException e) {
								e.printStackTrace();
							} catch (IllegalAccessException e) {
								e.printStackTrace();
							} catch (InvocationTargetException e) {
								e.printStackTrace();
							}
						}
						agent.die();
					} catch (final MasException e) {
						e.printStackTrace();
					}
				}
			} catch (final NullPointerException e) {
				// TODO: We don't have that agent's position yet
			}
		}
		this.agentsNb = agents.length;

	}

	@Override
	public void paintComponent(final Graphics g) {

		whitenIfRequested();
		g.drawImage(this.bimage, 0, 0, null);
		//printInfo(g);

		if (!this.shutdown) {
			paintAgentsInformation(g);
		} else {
		}

		repaint();
	}

	private void paintShortInfo(final Graphics g, final String message) {
		this.infolabel_index++;
		final int rectwidth = 70;
		final int rectHeight = 20;
		final int rectXOrigin = 865;
		final int rectYOrigin = 5 + 25 * this.infolabel_index;
		g.setColor(Color.lightGray);
		g.fillRect(rectXOrigin, rectYOrigin, rectwidth, rectHeight);
		g.setColor(Color.black);
		g.drawRect(rectXOrigin, rectYOrigin, rectwidth, rectHeight);
		g.drawString(message, rectXOrigin + 5, rectYOrigin + 15);
	}

	private void printInfo(final Graphics g) {
		this.imageGraphics.setPaintMode();
		backsScreen(g, 500, 200);

		this.infolabel_index = 0;

		addlabel(g, "INFORMATION", 850);

		addlabel(g, "Initial agents nb: ", 820);
		paintShortInfo(g, "" + Mas.config.agentsInitialNumber);

		addlabel(g, "Agents remaining: ", 820);
		paintShortInfo(g, String.valueOf(this.agentsNb));

		addlabel(g, "Total cycles: ", 820);
		paintShortInfo(g, this.kernel.getCallNb());

		if (!this.shutdown) {
			addlabel(g, "Cycles per second: ", 820);
			paintShortInfo(g, this.kernel.getLastCyclesNb());
		} else {
			g.setColor(Color.white);
			g.fillRect(870, 245, 55, 23);
			g.setColor(Color.black);
			g.drawRect(870, 245, 55, 23);
			g.setColor(Color.red);
			g.drawString("DONE", 880, 260);
		}
	}

	@Override
	public void repaint() {
		super.repaint();
	}

	private Color setColorFromBody(final double mass, final double accel) {
		final double transfAccel = Math.log(mass + accel);
		final int newColor = (int) (DrawPanel.max_color_int * Math
				.cos(transfAccel));
		//System.out.println("Couleur : "+newColor);
		return new Color(newColor);
	}

	public void setSpace(final Space space) {
		this.space = space;
	}

	private void setStrokeFromBody(final double mass, final double accel) {
		final int lineWidth = (int) ((Math.log(mass + accel
				+ this.accel2LineWidth)));
		this.imageGraphics.setStroke(new BasicStroke(lineWidth));
	}

	private void setupMouseListeners() {
		addMouseListener(new MouseAdapter() {

			@Override
			public void mousePressed(final MouseEvent e) {
				switch (e.getButton()) {
				case MouseEvent.BUTTON3:
					withen();
					break;

				default:
					break;
				}
			}
		}

		);
	}

	public void shutdown() {
		if (!this.shutdown) {
			this.kernel = null;
			this.shutdown = true;
		}
	}

	private boolean tooFar(final Point2D point) {
		return point.distance(this.centerPoint) > this.max_distance_allowed;
	}

	
	public void trace(
			final double mass,
			double accel,
			final Point2D lastCoordinates,
			final Point2D newCoordinates,
			ArrayList<Double> musicValues) {
		
		if (accel < 1) {
			accel = 1;
		}
		setStrokeFromBody(mass, accel);
		final double lastX = lastCoordinates.getX();
		final double lastY = lastCoordinates.getY();
		final double newX = newCoordinates.getX();
		final double newY = newCoordinates.getY();
		this.imageGraphics.setColor(setColorFromBody(mass, accel));
		
		this.imageGraphics.drawLine((int) lastX, (int) lastY, (int) newX,
				(int) newY);
		int counter = this.chooseLineWidth(musicValues);
		//this.imageGraphics.fillRect((int)newX, (int)newY, counter+1, counter+1);
		this.imageGraphics.fillOval((int)(newX -((counter+1)/2)), (int)(newY - ((counter+1)/2)), counter+1, counter+1);
		}
	
	
	
	public void traceEater(
			final double mass,
			double accel,
			final Point2D lastCoordinates,
			final Point2D newCoordinates) {

		if (accel < 1) {
			accel = 1;
		}
		setStrokeFromBody(mass, accel);
		final double lastX = lastCoordinates.getX();
		final double lastY = lastCoordinates.getY();
		final double newX = newCoordinates.getX();
		final double newY = newCoordinates.getY();
		this.imageGraphics.setColor(Color.white);
		

		if((int)(Math.random()*10000)%9999 == 0){
			int rand = (int)(Math.random()*200)+100;
			this.imageGraphics.fillOval((int) lastX - rand/2 , (int) lastY - rand/2 ,rand,rand);	
		}else{
			this.imageGraphics.fillOval((int) lastX - 5 , (int) lastY - 5 ,10,10);
		}
		//this.imageGraphics.drawRect((int) lastX, (int) lastY,((int) lastX- (int) newX)*3, ((int) lastY-(int) newY)*3);
	}

	private void whitenIfRequested() {
		if (this.asyncWhiteningRequired) {
			this.imageGraphics.setColor(Color.white);
			this.imageGraphics
					.fillRect(0, 0, this.getWidth(), this.getHeight());
			this.asyncWhiteningRequired = false;
		}
	}

	void withen() {
		this.asyncWhiteningRequired = true;
	}

	public void drawCircle(double mass, Coordinates coordinates) {
		final double radius = mass * accel2LineWidth;
		final int xOrigin = (int) (coordinates.getX() - radius);
		final int yOrigin = (int) (coordinates.getY() - radius);
		final int diameter = (int) radius * 2;
		Color color1 = setColorFromBody(mass, radius);
		gradientMode(coordinates, radius, color1);
		this.imageGraphics.setXORMode(Color.black);
		System.out.println("paint3: "
				+ this.imageGraphics.getPaint().getClass().getSimpleName());
		this.imageGraphics.fillOval(xOrigin, yOrigin, diameter, diameter);
	}

	private void gradientMode(
			Coordinates coordinates,
			final double radius,
			Color color1) {
		final float[] sequence = { 0, 1 };
		final Color[] colsequence = { color1, Color.black };
		final RadialGradientPaint gradient = new RadialGradientPaint(
				coordinates, (float) radius, sequence, colsequence);
		this.imageGraphics.setPaint(gradient);
	}
	
	// MP3 file to be read be the program
	public File getFileToPlay(){
		return this.fileToPlay;
	}

	public boolean getFileOrEntry(){
		return this.isFileOrEntry;
	}
	
	public FilePlayer getFilePlayer(){
		return this.filePlayer;
	}
	
	public LineEntryPlayer getLinePlayer(){
		return this.linePlayer;
	}
	
	public HashMap<Double, Double> getMap(){
		return this.map;
	}
	private int chooseLineWidth(ArrayList<Double> musicalValues){
		if(musicalValues.get(0) == musicalValues.get(1))
			return 0;
		else
			return this.calculateStep(musicalValues);
			
		/*if(musicValue==null)
			return 0;
		return (int) (musicValue.doubleValue()/100);
		*
		/*
		if(musicValue==null || musicValue < 20)
			return 0;
		if(musicValue < 40)
			return 1;
		if(musicValue < 60)
			return 2;
		if(musicValue < 80)
			return 3;
		if(musicValue < 100)
			return 4;
		return 5;
		
	*/
	}
	
	private int calculateStep(ArrayList<Double> musicalValues){
		if(musicalValues.get(0)!= null && musicalValues.get(1) != null){
			/*Double oldV = musicalValues.get(0),newV = musicalValues.get(1),diff = Math.abs(newV - oldV) ;
			while((diff) > 7){
				diff = diff - 7;
			}
		
			return (int)(diff.intValue()*1.5);*/
			return  (musicalValues.get(0).intValue())/4;
			
		}
		return 0;
	}
	
}
