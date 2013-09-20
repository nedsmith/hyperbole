/**
 * 
 */
package hyperbolic;

/**
 * @author Ned
 *
 */
public class HalfPlaneMotion implements HyperbolicRigidMotion {

	/*
	 *         az + b
	 * z  ->  --------
	 *         cz + d
	 */
	
	private final double a;
	private final double b;
	private final double c;
	private final double d;
	
	
	public HalfPlaneMotion() {
		this(1,0,0,1);
	}
	
	public HalfPlaneMotion(double a, double b, double c, double d) {
		double determinant = Math.sqrt(a*d - b*c);
		this.a = a / determinant;
		this.b = b / determinant;
		this.c = c / determinant;
		this.d = d / determinant;
	}

	@Override
	public HyperbolicPoint transform(HyperbolicPoint point) {
		double[] halfPlanePosition = point.getHalfPlanePosition();
		double z = halfPlanePosition[0];
		double w = halfPlanePosition[1];
		
		double denominator = (c*z+d)*(c*z+d)+c*c*w*w;
		double realNumerator = (a*z+b)*(c*z+d)+a*c*w*w;
		double complexNumerator = a*w*(c*z+d)-c*w*(a*z+b);
		
		double newZ = realNumerator / denominator;
		double newW = complexNumerator / denominator;
		
		return new HalfPlaneRepresentation(newZ, newW);
	}

	@Override
	public HyperbolicRigidMotion inverse() {
		return new HalfPlaneMotion(d, -b, -c, a);
	}

	@Override
	public HyperbolicRigidMotion composeWith(HyperbolicRigidMotion other) {
		if (other instanceof HalfPlaneMotion) {
			HalfPlaneMotion otherMotion = (HalfPlaneMotion) other;
			double e = otherMotion.a;
			double f = otherMotion.b;
			double g = otherMotion.c;
			double h = otherMotion.d;
			return new HalfPlaneMotion(a*e+b*g, a*f+b*h, c*e+d*g, c*f+d*h);
		}
		return null;
	}

	@Override
	public HyperbolicLine transform(HyperbolicLine line) {
		HyperbolicPoint oldStartPoint = line.getStartPoint();
		HyperbolicPoint oldEndPoint = line.getEndPoint();
		HyperbolicPoint newStartPoint = transform(oldStartPoint);
		HyperbolicPoint newEndPoint = transform(oldEndPoint);
		return new SimpleHyperbolicLine(newStartPoint, newEndPoint);
	}

	@Override
	public HyperbolicPoint intersection() {
		double denominator = a*c;
		double numerator = -b*d;
		if (Math.abs(denominator)<1e-99) return null;
		double ySquared = numerator / denominator;
		if (ySquared<0) return null;
		final double y = Math.sqrt(ySquared);
		return new HalfPlaneRepresentation(0, y);
	}

}
