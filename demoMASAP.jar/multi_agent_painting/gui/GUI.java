package multi_agent_painting.gui;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.HashMap;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import multi_agent_painting.applet.panes.ConfigModePanel;
import multi_agent_painting.applet.panes.HelpModePanel;
import multi_agent_painting.applet.panes.InfoModePanel;
import multi_agent_painting.applet.panes.SplashPane;
import multi_agent_painting.applet.panes.drawingPane.DrawModePanel;
import multi_agent_painting.mas.Kernel;
import multi_agent_painting.mas.Mas;
import multi_agent_painting.mas.exceptions.RoleInitException;
import multi_agent_painting.mas.sound.FilePlayer;
import multi_agent_painting.mas.sound.LineEntryPlayer;
import tools.appControl.Logger;

@SuppressWarnings("serial")
public class GUI extends JFrame implements ActionListener {

	private final class StateManager implements Runnable {

		private void goDrawMode() throws RoleInitException {

			final DrawModePanel drawModePanel = new DrawModePanel(new Kernel(),
					400,GUI.this.playingOrRecording,GUI.this.song, new FilePlayer(GUI.this.song , GUI.this.map), new LineEntryPlayer(GUI.this.map), GUI.this.map);
			GUI.this.mainContentPane.add(drawModePanel);
			try {
				GUI.this.mas = new Mas(drawModePanel.getDrawingPanel());
			} catch (final Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.exit(1);
			}
		}

		private void goInfoMode() {
			final InfoModePanel splashInfo = new InfoModePanel();
			GUI.this.mainContentPane.add(splashInfo);

		}
		
		private void goWizardMode() {
			final ConfigModePanel configModePane = new ConfigModePanel();
			//GUI.this.mainContentPane.add(configModePane);
			
		}

		private void goHelpMode() {
			final HelpModePanel helpModePanel = new HelpModePanel();
			//GUI.this.mainContentPane.add(helpModePanel);
		}

		//Explication du if.
		//Si on a bien charger un son avant d'avoir la fenetre principale alors on la construit => apparition des bouton let's draw etc...
		//Sinon on fait apparaitre la fenetre de chargement
		private void goSplashMode() {
			if(GUI.this.soundSourceFrame.getApparitionBeforePainting()){
				GUI.this.splashPane = new SplashPane();
				GUI.this.mainContentPane.add(GUI.this.splashPane);
			}
			else{
				GUI.this.setVisible(false);
				GUI.this.soundSourceFrame.setVisible(false);
			}
			//GUI.this.soundSourceFrame.setApparitionBeforePainting();
		}

		public void run() {
			while (!GUI.this.stop) {
				if (GUI.this.currentState != GUI.this.state) {
					Logger.info("going to state " + GUI.this.state.toString());
					GUI.this.mainContentPane.removeAll();
					if (GUI.this.mas != null) {
						GUI.this.mas.shutdown();
						GUI.this.mas = null;
					}
					switch (GUI.this.state) {
					case SPLASH:
						goSplashMode();
						break;
					case DRAW:
						try {
							goDrawMode();
						} catch (final Exception e) {
							GUI.this.stop = true;
							e.printStackTrace();
						}
						break;
					case INFO:
						goInfoMode();
						break;
					case HELP:
						goHelpMode();
						break;
					case WIZARD:
						goWizardMode();
						break;
					}
					

					GUI.applet.validate();
					GUI.applet.repaint();
					GUI.this.currentState = GUI.this.state;
				}
				Thread.yield();
			}

		}

	}

	static GUI	applet;
	boolean playingOrRecording = false;
	File song = null;
	final SoundSourceFrame soundSourceFrame = new SoundSourceFrame(this);
	HashMap<Double, Double> map = new HashMap<Double, Double>();
	
	public void completeGUIStructure(boolean _playingOrRecording,File _song){
		
		initSoundSource(_playingOrRecording,_song);
		
		this.init();
		
		this.pack();
		
	}
	
	public void initSoundSource(boolean _playingOrRecording,File _song){
		this.playingOrRecording = _playingOrRecording;
		this.song = _song;
	}
	
		
	public void goSplasModeFromSoundSourceFrame(boolean _playingOrRecording,File _song){
		initSoundSource(_playingOrRecording,_song);
		GUI.this.splashPane = new SplashPane();
		GUI.this.mainContentPane.add(GUI.this.splashPane);
	}
	
	public static void main(final String[] args) {
		final GUI gui = new GUI();
		gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		final Dimension dimension = new Dimension(1000, 798);
		
		gui.setSize(dimension);
		
		gui.setMinimumSize(dimension);
		
	}

	private MadButtons		currentState	= MadButtons.INIT;

	private final Container	mainContentPane;

	private JPanel			splashPane;

	protected MadButtons	state			= MadButtons.SPLASH;

	JLabel					statusLabel;

	private boolean			stop;

	Mas						mas;

	public GUI() throws HeadlessException {
		super();
		soundSourceFrame.setVisible(true);
		this.setTitle("Multi Agent Drawing");
		GUI.applet = this;
		this.mainContentPane = getContentPane();
		if (this.mainContentPane == null) {
			System.out.println("oops");
		} else {
			System.out.println(this.mainContentPane.getClass());
		}
	}

	@Override
	public void actionPerformed(final ActionEvent arg0) {
		// TODO Auto-generated method stub
		// Nothing to do, actually.
	}

	public void back() {

	}


	public void init() {

		// Execute a job on the event-dispatching thread:
		// creating this applet's GUI.
		try {
			//SwingUtilities.invokeAndWait(new Runnable() {
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {

					setState(MadButtons.SPLASH);
					// YES, nested Runnable... So much for cyclomatic
					// complexity. (feth)
					new Thread(new StateManager()).start();
				}
			});
		} catch (final Exception e) {
			System.err.println("createGUI didn't successfully complete");
		}

	}

	public void setState(final MadButtons state) {
		this.state = state;
	}
}
