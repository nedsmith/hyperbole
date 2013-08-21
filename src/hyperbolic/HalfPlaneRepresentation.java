/**
 * 
 */
package hyperbolic;

import static java.lang.Math.*;

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

	@Override
	public double distanceFrom(HyperbolicPoint other) {
		double[] coords = other.getHalfPlanePosition();
		double x2 = coords[0];
		double y2 = coords[1];
		double z = ((x-x2)*(x-x2)+(y-y2)*(y-y2))/(2*y*y2)+1;
		return acosh(z);
	}
	
	private double acosh(double z) {
		return log(z+sqrt(z*z-1));
	}

}
