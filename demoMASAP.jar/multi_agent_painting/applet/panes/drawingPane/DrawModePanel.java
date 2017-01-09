package multi_agent_painting.applet.panes.drawingPane;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.border.CompoundBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import tools.appControl.Logger;

import multi_agent_painting.gui.MadButtons;
import multi_agent_painting.mas.Kernel;
import multi_agent_painting.mas.KernelState;
import multi_agent_painting.mas.sound.FilePlayer;
import multi_agent_painting.mas.sound.LineEntryPlayer;
import multi_agent_painting.mas.sound.SoundThreadState;

@SuppressWarnings("serial")
public class DrawModePanel extends JPanel {

	public class ButtonsPane extends javax.swing.JPanel {

		public ButtonsPane() {
			super();

			//this.setLayout(new GridLayout(1, 1));
			this.setBorder(new CompoundBorder());
			//this.add(MadButtons.WIZARD.jButton);
			//this.add(MadButtons.SPLASH.jButton);
			//this.add(MadButtons.EXPORTIMAGE.jButton);
			//this.add(MadButtons.MKSPECIALAREA.jButton);

			/*MadButtons.MKSPECIALAREA.jButton
					.addActionListener(new java.awt.event.ActionListener() {

						@Override
						public void actionPerformed(final ActionEvent arg0) {
							// userSquareDrawMode(true);
							System.err.println("woops... Not implemented !");
						}

					});

			mkExportButton();
			this.setEnabled(true);
*/
		}

		private void clearActionListeners() {
			final ActionListener[] actionListeners = MadButtons.EXPORTIMAGE.jButton
					.getActionListeners();
			for (final ActionListener actionListener : actionListeners) {
				MadButtons.EXPORTIMAGE.jButton
						.removeActionListener(actionListener);
			}
		}

		private void mkExportButton() {
			clearActionListeners();
			final java.awt.event.ActionListener exportAction = new java.awt.event.ActionListener() {

				@Override
				public void actionPerformed(final ActionEvent arg0) {
					File file;
					final JFileChooser fc = new JFileChooser();
					fc.setDialogTitle("Save As PNG Image");
					fc.setFileFilter(new FileNameExtensionFilter(
							"Image (*.png)", new String[] { "png" }));
					fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
					final int result = fc.showSaveDialog(DrawModePanel.this);
					if (result == JFileChooser.APPROVE_OPTION) {
						file = fc.getSelectedFile();
						try {
							ImageIO.write(DrawModePanel.this.drawPanel.bimage,
									"png", new File(file.toString() + ".png"));
						} catch (final IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			};
			MadButtons.EXPORTIMAGE.jButton.addActionListener(exportAction);
		}

		@Override
		public void setEnabled(final boolean enabled) {
			super.setEnabled(enabled);
			final Component[] components = getComponents();
			for (final Component component : components) {
				if (component != MadButtons.SPLASH.jButton) {
					component.setEnabled(enabled);
				}
			}
		}
	}

	public class ControlPane extends JPanel {
		final Kernel	kernel;
		final FilePlayer filePlayer;
		final LineEntryPlayer linePlayer;
		public ControlPane(final Kernel masKernel, final FilePlayer filePlayer, final LineEntryPlayer linePlayer) {

			super();

			this.kernel = masKernel;
			this.filePlayer = filePlayer;
			this.linePlayer = linePlayer;

		/*	DrawModePanel.this.playPauseNext
					.addActionListener(new java.awt.event.ActionListener() {

						@Override
						public void actionPerformed(
								final java.awt.event.ActionEvent evt) {

							playPauseNextButtonHandler();

						}

					});

			DrawModePanel.this.erase
					.addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(
								final java.awt.event.ActionEvent evt) {
							DrawModePanel.this.drawPanel.withen();

						}
					});

			DrawModePanel.this.stepByStep
					.addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(
								final java.awt.event.ActionEvent evt) {
							switch (ControlPane.this.kernel.getState()) {
							case PLAY:
							case PAUSE:
								ControlPane.this.kernel
										.setState(KernelState.STEPBYSTEP);
								ControlPane.this.filePlayer.setState(SoundThreadState.STEPBYSTEP);
								ControlPane.this.linePlayer.setState(SoundThreadState.STEPBYSTEP);
								DrawModePanel.this.playPauseNext
										.setText("Next");
								DrawModePanel.this.stepByStep.setText("Resume");
								DrawModePanel.this.stepByStep
										.setIcon(new javax.swing.ImageIcon(
												getClass().getResource(
														"/icons/resume.png")));
								DrawModePanel.this.playPauseNext
										.setIcon(new javax.swing.ImageIcon(
												getClass().getResource(
														"/icons/next.png")));
								break;
							case STEPBYSTEP:
								ControlPane.this.kernel
										.setState(KernelState.PLAY);
								ControlPane.this.filePlayer.setState(SoundThreadState.PLAY);
								ControlPane.this.linePlayer.setState(SoundThreadState.PLAY);
								DrawModePanel.this.stepByStep
										.setText("Step by step");
								DrawModePanel.this.playPauseNext
										.setText("Pause");
								DrawModePanel.this.stepByStep
										.setIcon(new javax.swing.ImageIcon(
												getClass()
														.getResource(
																"/icons/stepbystep.png")));
								DrawModePanel.this.playPauseNext
										.setIcon(new javax.swing.ImageIcon(
												getClass().getResource(
														"/icons/pause.png")));

							default:
								break;
							}

						}
					}); */

			// this.add(erase, BorderLayout.EAST);
			// this.add(playPauseNext, BorderLayout.NORTH);
			// this.add(stepByStep, BorderLayout.WEST);
			//			

			//this.add(DrawModePanel.this.stepByStep, 0);
			//this.add(DrawModePanel.this.playPauseNext, 0);
			//this.add(DrawModePanel.this.erase, 0);
			//final Spinner spinner = new Spinner();
			//this.add(spinner, 0);
			//this.kernel.setSpinner(spinner);
		}
	}

	private DrawPanel			drawPanel;
	private final ButtonsPane	buttonsPane;
	final JButton				playPauseNext	= MadButtons.PLAY_PAUSE_NEXT.jButton;
	final JButton				stepByStep		= MadButtons.STEPBYSTEP.jButton;

	final JButton				erase			= MadButtons.ERASE.jButton;

	private final Kernel		kernel;
	
	private final FilePlayer filePlayer;
	private final LineEntryPlayer lineplayer;
	private final HashMap<Double, Double> map;

	public DrawModePanel(final Kernel kernel, final int max_distance_allowed,boolean _playingOrRecording,File _song, FilePlayer filePlayer, LineEntryPlayer lineEntryPlayer, HashMap<Double, Double> map) {
		super();
		this.kernel = kernel;
		this.setLayout(new BorderLayout());
		this.setBorder(new CompoundBorder());
		this.lineplayer = lineEntryPlayer;
		this.filePlayer = filePlayer;
		makeDrawPanel(max_distance_allowed, _playingOrRecording,_song, map);
		final ControlPane controlpane = new ControlPane(kernel, filePlayer, lineEntryPlayer);
		//this.add(controlpane, BorderLayout.PAGE_START);
		this.add(this.drawPanel);
		this.buttonsPane = new ButtonsPane();
		//this.add(this.buttonsPane, BorderLayout.PAGE_END);
		this.map = map;
	}
	
	public HashMap<Double, Double> getMap(){
		return this.map;
	}

	public DrawPanel getDrawingPanel() {
		return this.drawPanel;
	}

	private void makeDrawPanel(final int max_distance_allowed,boolean _playingOrRecording, File _song, HashMap<Double, Double> map) {
		this.drawPanel = new DrawPanel(this.kernel, max_distance_allowed,_playingOrRecording,_song, this.lineplayer, this.filePlayer, map);
	}

	private void playPauseNextButtonHandler() {
		switch (this.kernel.getState()) {
		case PLAY:
			this.kernel.setState(KernelState.PAUSE);
			this.filePlayer.setState(SoundThreadState.PAUSE);
			this.lineplayer.setState(SoundThreadState.PAUSE);
			this.playPauseNext.setText("Play");
			final javax.swing.ImageIcon imageIcon = new javax.swing.ImageIcon(
					getClass().getResource("/icons//play.png"));

			this.playPauseNext.setIcon(imageIcon);

			break;
		case PAUSE:

			this.kernel.setState(KernelState.PLAY);
			this.filePlayer.setState(SoundThreadState.PLAY);
			this.lineplayer.setState(SoundThreadState.PLAY);
			this.playPauseNext.setText("Pause");

			this.playPauseNext.setIcon(new javax.swing.ImageIcon(getClass()
					.getResource("/icons/pause.png")));

		case STEPBYSTEP:
			try {
				this.kernel.nextStep();
			} catch (final Exception e) {
				// TODO: message to user
				e.printStackTrace();
			}
		default:
			break;
		}
	}
}
