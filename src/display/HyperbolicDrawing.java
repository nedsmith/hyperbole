/**
 * 
 */
package display;

import java.awt.Color;

/**
 * @author Ned
 *
 */
public abstract class HyperbolicDrawing {
	private Color color;
	
	public HyperbolicDrawing(Color color) {
		this.color=color;
	}
	
	public Color getColor() {
		return this.color;
	}
}
