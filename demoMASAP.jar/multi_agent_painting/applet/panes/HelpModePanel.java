package multi_agent_painting.applet.panes;

import java.awt.BorderLayout;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.border.CompoundBorder;
import multi_agent_painting.gui.MadButtons;

@SuppressWarnings("serial")
public class HelpModePanel extends javax.swing.JPanel {

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

	public class HelpTextPane extends javax.swing.JPanel {

		public HelpTextPane() {
			super();
			setBounds(0, 0, 900, 500);
			setPreferredSize(getSize());

			final JLabel jLabel = new javax.swing.JLabel();
			String str;
			str = "<html>"
					+ "<font size=\"10\"color=\"red\">Multi Agent Drawing</font><br><br>"
					+ "Le but de ce projet étant de mettre en place des agents réactifs.<br>"
					+ "Nous avons mis en place un certain nombre d’agents avec des comportements différents pour obtenir une toile.<p> "
					+ "Notre rôle n’étant pas de contrôler les agents, nous ne leur imposons aucun dessin, les agents sont libres.<br><br>"
					+ "<font size=\"5\" color=\"blue\">Comment intéragissent les différents agents de notre Système?</font><br><br>"
					+ "Les agents dessinent en mode Paint lorsqu'ils accélèrent, et que l'épaisseur et la couleur dépendent de la masse.<br>"
					+ "En cas de collision, les agents dessinent en mode XOR :"
					+ "<ul>"
					+ "<li>Un cercle pour la coalescence.</li>"
					+ "<li>Un triangle quand ils en font exploser un autre.</li>"
					+ "</ul>"
					+ "Les forces en jeu sont :"
					+ "<ul>"
					+ "<li>La gravitation</li>"
					+ "<li>La pression de radiation (Rayonnement du corps noir)</li>"
					+ "<li>Le frottement fluide (En utilisant un facteur de forme pour chaque objet)</li>"
					+ "</ul>" + "</html>";

			this.setLayout(new BorderLayout(0, 14));

			jLabel.setText(str);
			jLabel.setHorizontalTextPosition(SwingConstants.CENTER);
			this.setBorder(new javax.swing.border.SoftBevelBorder(
					javax.swing.border.BevelBorder.RAISED));
			this.add(jLabel, BorderLayout.CENTER);

			// this.add(MadButtons.BACK.jButton, BorderLayout.WEST);
			// this.add(MadButtons.NEXT.jButton, BorderLayout.PAGE_END);
			this.add(MadButtons.HELP.jButton, BorderLayout.PAGE_END);
		}
	}

	public HelpModePanel() {
		super();

		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.setBorder(new CompoundBorder());
		final ButtonsPane buttonsPane = new ButtonsPane();
		final HelpTextPane helpTextPane = new HelpTextPane();
		MadButtons.HELP.jButton.setEnabled(false);
		this.add(helpTextPane, BorderLayout.CENTER);
		this.add(buttonsPane, BorderLayout.SOUTH);
	}

}
