package multi_agent_painting.gui;

import java.net.URL;

import javax.swing.JButton;

/**
 * This enum statically contains the applet's main buttons.<br>
 * It must be in sync with the state related enum in MadJapplet -eventually this
 * later enum should be deleted in a refactoring, because it no longer makes
 * sense.
 */
public enum MadButtons {
	DRAW("/icons/dessiner.png", "Let agents draw", true),
	INFO("/icons/info.png", "About....", true),
	SPLASH("/icons/uturn.png", "Back to menu", true),
	WIZARD("/icons/tools.png", "New drawing configuration", true),
	INIT(),
	NEXT("/icons/next.png", "Next", true),
	HELP("/icons/help.png", "Explanation", true),
	EXPORTIMAGE("/icons/export.png", "Save image", false),
	MKSPECIALAREA("/icons/specialArea.png", "Draw special area", false),
	PLAY_PAUSE_NEXT("/icons/pause.png", "Pause", false),
	STEPBYSTEP("/icons/stepbystep.png", "Step by step", false),
	ERASE("/icons/erase.png", "Erase", false),
	BACK("/icons/back.png", "BACK", false);

	public final JButton	jButton	= new JButton();

	MadButtons() {
		if (!this.name().equals("INIT")) {
			throw new ExceptionInInitializerError(
					"this constructor is reserved for the initial state.");
		}

	}

	MadButtons(final String image, final String text, final boolean appstate) {
		this.jButton.setFont(this.jButton.getFont().deriveFont((float) 24));
		final URL resource = getClass().getResource(image);
		if (resource == null) {
			throw new NullPointerException("Image " + image + " not found.");
		}
		final javax.swing.ImageIcon imageIcon = new javax.swing.ImageIcon(
				resource);
		this.jButton.setIcon(imageIcon);
		this.jButton.setText(text);
		if (appstate) {
			final MadButtons madButton = this;
			this.jButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(final java.awt.event.ActionEvent evt) {
					GUI.applet.setState(madButton);
				}
			});
		}
	}
}
