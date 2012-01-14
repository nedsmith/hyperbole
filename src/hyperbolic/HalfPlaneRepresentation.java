/**
 * 
 */
package hyperbolic;

/**
 * @author Ned
 *
 */
public final class HalfPlaneRepresentation implements HyperbolicPoint {

	private final double x;
	private final double y;
	
	public HalfPlaneRepresentation() {
		this(0,1);
	}
	
	public HalfPlaneRepresentation(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	@Override
	public double[] getHalfPlanePosition() {
		return new double[]{x,y};
	}

	@Override
	public double[] getDiskPosition() {
		double denominator = (1+y)*(1+y)+x*x;
		double realNumerator = 2*x;
		double complexNumerator = x*x+y*y-1;
		return new double[]{realNumerator/denominator,
				            complexNumerator/denominator};
	}

}
