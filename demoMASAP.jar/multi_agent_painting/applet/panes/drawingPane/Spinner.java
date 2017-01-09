/**
 * 
 */
package multi_agent_painting.applet.panes.drawingPane;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.JLabel;
import javax.swing.border.SoftBevelBorder;

@SuppressWarnings("serial")
public class Spinner extends JLabel {

	BufferedImage		bi	= new BufferedImage(64, 64,
									BufferedImage.TYPE_4BYTE_ABGR);
	private Graphics2D	g2d	= this.bi.createGraphics();
	private boolean		darker;

	public Spinner() {
		this.g2d.setColor(Color.black);
		this.g2d.fillRect(0, 0, 64, 64);
		this.setBorder(new SoftBevelBorder(
				javax.swing.border.BevelBorder.RAISED));

	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(64, 64);
	}

	@Override
	public void paintComponent(final Graphics g) {
		super.paintComponents(g);
		// this.g2d.fillOval(5, 5, 44, 44);
		g.drawImage(this.bi, 0, 0, this);
	}

	@Override
	public void repaint() {
		super.repaint();
	}

	public void rotate() {

		Color color = this.g2d.getColor();
		if (this.darker) {
			color = color.darker();
			this.g2d.setColor(color);
		} else {
			color = color.brighter();
			this.g2d.setColor(color);
		}
		if (color.equals(Color.black)) {
			this.darker = false;
		} else if (color.equals(Color.white)) {
			this.darker = true;
		}

		this.g2d.fillOval(50, 50, 2, 25);
		this.g2d.rotate(0.4, 32, 32);

		repaint();
	}

	public void shutdown() {
		this.bi = null;
		this.g2d = null;
	}
}