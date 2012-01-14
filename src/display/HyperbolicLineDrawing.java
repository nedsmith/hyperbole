/**
 * 
 */
package display;

import java.awt.Color;

/**
 * @author Ned
 *
 */
public final class HyperbolicLineDrawing {

	private final HyperbolicLineSet lineSet;
	private final Color color;
	
	/**
	 * @return the lineSet
	 */
	public HyperbolicLineSet getLineSet() {
		return lineSet;
	}

	/**
	 * @return the color
	 */
	public Color getColor() {
		return color;
	}

	/**
	 * 
	 */
	public HyperbolicLineDrawing() {
		this(new HashHyperbolicLineSet(),Color.black);
	}

	/**
	 * @param lineSet
	 */
	public HyperbolicLineDrawing(HyperbolicLineSet lineSet) {
		this(lineSet, Color.black);
	}

	/**
	 * @param lineSet
	 * @param color
	 */
	public HyperbolicLineDrawing(HyperbolicLineSet lineSet, Color color) {
		this.lineSet = lineSet;
		this.color = color;
	}

	/**
	 * @param color
	 */
	public HyperbolicLineDrawing(Color color) {
		this(new HashHyperbolicLineSet(), color);
	}
	
	
}
