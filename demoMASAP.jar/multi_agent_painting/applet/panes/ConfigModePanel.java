package multi_agent_painting.applet.panes;

import java.awt.BorderLayout;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.border.CompoundBorder;

import multi_agent_painting.gui.MadButtons;

@SuppressWarnings("serial")
public class ConfigModePanel extends javax.swing.JPanel {
	
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

	
	public class ConfigModePane extends javax.swing.JPanel {
		public ConfigModePane(){
			super();
			setBounds(0, 0, 900, 500);
			setPreferredSize(getSize());	
			this.setLayout(new BorderLayout(0, 14));
			
			final JLabel lbNbrAgent = new JLabel("Nombdre d'agents :");
			final JLabel lbGravite = new JLabel("Gravite :");
			final JLabel lbDistanceCollision = new JLabel("Distance de collision :");
			
			// les JSLIDER
			final JSlider jSNbragent = new JSlider(); 
			@SuppressWarnings("unused")
			final JSlider jSGravite = new JSlider();
			
			// Confi des JSLIDER
			jSNbragent.setMaximum(10);
			jSNbragent.setMinimum(0);
			jSNbragent.setMajorTickSpacing(1);
			jSNbragent.setMinorTickSpacing(1);
			jSNbragent.setPaintTicks(true);
			jSNbragent.setSnapToTicks(true);
			jSNbragent.setPaintLabels(true); 
			
			
			this.add(jSNbragent);
			
			
			
			this.add(lbNbrAgent,BorderLayout.WEST);
			this.add(lbGravite,BorderLayout.WEST);
			this.add(lbDistanceCollision,BorderLayout.WEST);
			//this.add(lbNbrAgent,BorderLayout.WEST);
		}
		
	}

	public ConfigModePanel() {
		super();
		//this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		//this.setBorder(new CompoundBorder());
		//final ButtonsPane buttonsPane = new ButtonsPane();
		
		//final ConfigModePane configModePane = new ConfigModePane();
		//this.add(configModePane, BorderLayout.NORTH);
		//this.add(buttonsPane, BorderLayout.SOUTH);
	}

}
