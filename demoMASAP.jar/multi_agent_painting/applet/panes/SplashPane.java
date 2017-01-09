package multi_agent_painting.applet.panes;

import java.awt.BorderLayout;
import java.net.URL;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.border.CompoundBorder;

import multi_agent_painting.gui.MadButtons;

@SuppressWarnings("serial")
public class SplashPane extends javax.swing.JPanel {

	public class ButtonsPane extends javax.swing.JPanel {
		public ButtonsPane() {
			super();
			this.setLayout(new BorderLayout(0, 14));
			this.setBorder(new javax.swing.border.SoftBevelBorder(
					javax.swing.border.BevelBorder.RAISED));
			this.add(MadButtons.DRAW.jButton, BorderLayout.WEST);
			//this.add(MadButtons.INFO.jButton, BorderLayout.EAST);

		}
	}

	public class SplashImagePane extends javax.swing.JPanel {
		public SplashImagePane() {
			super();
			setBounds(0, 0, 798, 500);
			setPreferredSize(getSize());
			this.setLayout(new BorderLayout(0, 14));
			final URL resource = getClass().getResource("/icons/splash.png");
			final JLabel jLabel1 = new javax.swing.JLabel(
					new javax.swing.ImageIcon(resource));
			jLabel1.setHorizontalTextPosition(SwingConstants.CENTER);
			this.setBorder(new javax.swing.border.SoftBevelBorder(
					javax.swing.border.BevelBorder.RAISED));
			//this.add(jLabel1, BorderLayout.CENTER);
		}
	}

	public SplashPane() {
		super();
		MadButtons.HELP.jButton.setEnabled(true);

		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.setBorder(new CompoundBorder());
		final SplashImagePane splashImagePane = new SplashImagePane();
		this.add(splashImagePane, BorderLayout.NORTH);
		final ButtonsPane buttonsPane = new ButtonsPane();
		this.add(buttonsPane, BorderLayout.SOUTH);
	}

}
