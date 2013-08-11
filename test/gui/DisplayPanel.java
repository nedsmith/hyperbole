/**
 * 
 */
package gui;


import java.awt.Graphics;

import javax.swing.JPanel;

import display.Painter;

/**
 * @author Ned
 *
 */
public final class DisplayPanel extends JPanel {

	/***/
	private static final long serialVersionUID = 192006444394364516L;
	private final Painter painter;
	
	
	public DisplayPanel(Painter painter) {
		this.painter = painter;
	}
	
	@Override
	public void paint(Graphics g) {
		painter.paint(g);
	}

}
