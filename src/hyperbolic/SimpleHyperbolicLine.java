/**
 * 
 */
package hyperbolic;

/**
 * Obvious implementation of HyperbolicLine, a class with a reference to the start and end points.
 * 
 * @author Ned
 */
public final class SimpleHyperbolicLine implements HyperbolicLine {

	private final HyperbolicPoint startPoint;
	private final HyperbolicPoint endPoint;
	
	public SimpleHyperbolicLine(HyperbolicPoint startPoint, HyperbolicPoint endPoint) {
		this.startPoint = startPoint;
		this.endPoint = endPoint;
	}
	
	@Override
	public HyperbolicPoint getStartPoint() {
		return this.startPoint;
	}

	@Override
	public HyperbolicPoint getEndPoint() {
		return this.endPoint;
	}

}
