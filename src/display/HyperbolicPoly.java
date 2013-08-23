/**
 * 
 */
package display;

import hyperbolic.HyperbolicPoint;

/**
 * @author Ned
 *
 */
public final class HyperbolicPoly {

	private final HyperbolicPoint[] points;
	
	public HyperbolicPoly(HyperbolicPoint ... points) {
		this.points = points;
	}
	
	public HyperbolicPoint[] points() {
		return points;
	}
}
