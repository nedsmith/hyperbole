/**
 * 
 */
package gui;


import javax.swing.JFrame;

import display.Painter;

/**
 * @author Ned
 *
 */
public final class DisplayFrame extends JFrame {

	/***/
	private static final long serialVersionUID = -4863833159001431414L;
	
	private final DisplayPanel panel;
	
	public DisplayFrame(Painter painter) {
		setTitle("DisplayFrame");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 900, 900);
        setVisible(true);
        
		this.panel = new DisplayPanel(painter);
		add(this.panel);
	}
	
	public void repaint() {
		this.panel.repaint();
	}

}
