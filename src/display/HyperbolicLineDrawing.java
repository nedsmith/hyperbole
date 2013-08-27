/**
 * 
 */
package display;

import java.awt.Color;

/**
 * @author Ned
 *
 */
public final class HyperbolicLineDrawing extends HyperbolicDrawing {

	private final HyperbolicLineSet lineSet;
	
	/**
	 * @return the lineSet
	 */
	public HyperbolicLineSet getLineSet() {
		return lineSet;
	}

	/**
	 * 
	 */
	public HyperbolicLineDrawing() {
		this(new ArrayHyperbolicLineSet(),Color.black);
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
		super(color);
		this.lineSet = lineSet;
	}

	/**
	 * @param color
	 */
	public HyperbolicLineDrawing(Color color) {
		this(new ArrayHyperbolicLineSet(), color);
	}
	
	
}
