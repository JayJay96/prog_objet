package multi_agent_painting.applet.panes;

import java.awt.BorderLayout;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.border.CompoundBorder;

import multi_agent_painting.gui.MadButtons;

@SuppressWarnings("serial")
public class InfoModePanel extends javax.swing.JPanel {

	public class ButtonsPane extends javax.swing.JPanel {
		public ButtonsPane() {
			super();
			this.setLayout(new BorderLayout(0, 14));
			this.setBorder(new javax.swing.border.SoftBevelBorder(
					javax.swing.border.BevelBorder.RAISED));
			this.add(MadButtons.DRAW.jButton, BorderLayout.WEST);
			this.add(MadButtons.SPLASH.jButton, BorderLayout.EAST);

		}
	}

	public class InfoImagePane extends javax.swing.JPanel {
		public InfoImagePane() {
			super();
			setBounds(0, 0, 900, 500);
			setPreferredSize(getSize());
			this.setLayout(new BorderLayout(0, 14));

			final JLabel jLabel1 = new javax.swing.JLabel(
					new javax.swing.ImageIcon(getClass().getResource(
							"/icons/infoPanel.png")));
			jLabel1.setHorizontalTextPosition(SwingConstants.CENTER);
			this.setBorder(new javax.swing.border.SoftBevelBorder(
					javax.swing.border.BevelBorder.RAISED));
			this.add(jLabel1, BorderLayout.CENTER);

			// this.add(MadButtons.BACK.jButton, BorderLayout.WEST);
			// this.add(MadButtons.NEXT.jButton, BorderLayout.PAGE_END);
			this.add(MadButtons.HELP.jButton, BorderLayout.PAGE_END);

		}
	}

	public InfoModePanel() {
		super();
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.setBorder(new CompoundBorder());
		final InfoImagePane infoImagePane = new InfoImagePane();
		this.add(infoImagePane, BorderLayout.NORTH);
		final ButtonsPane buttonsPane = new ButtonsPane();
		this.add(buttonsPane, BorderLayout.SOUTH);
	}

}
