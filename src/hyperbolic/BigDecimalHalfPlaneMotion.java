/**
 * 
 */
package hyperbolic;

import java.math.BigDecimal;
import java.math.MathContext;

/**
 * @author Ned
 *
 */
public class BigDecimalHalfPlaneMotion implements HyperbolicRigidMotion {

	/*
	 *         az + b
	 * z  ->  --------
	 *         cz + d
	 */
	
	private final BigDecimal a;
	private final BigDecimal b;
	private final BigDecimal c;
	private final BigDecimal d;
	private static MathContext mathContext = new MathContext(20);
	
	
	public BigDecimalHalfPlaneMotion() {
		this(1,0,0,1);
	}
	
	public BigDecimalHalfPlaneMotion(double a, double b, double c, double d) {
		this(BigDecimal.valueOf(a), BigDecimal.valueOf(b), BigDecimal.valueOf(c), BigDecimal.valueOf(d));
	}
	
	public BigDecimalHalfPlaneMotion(BigDecimal a, BigDecimal b, BigDecimal c, BigDecimal d) {
		this.a = a;
		this.b = b;
		this.c = c;
		this.d = d;
	}

	@Override
	public HyperbolicPoint transform(HyperbolicPoint point) {
		double[] halfPlanePosition = point.getHalfPlanePosition();
		BigDecimal z = BigDecimal.valueOf(halfPlanePosition[0]);
		BigDecimal w = BigDecimal.valueOf(halfPlanePosition[1]);
		
		/* denominator = (c*z+d)^2 + c^2*w^2
		   realNumerator = (a*z+b)*(c*z+d) + a*c*w^2
		   complexNumerator = a*w*(c*z+d)-c*w*(a*z+b)		
		   newZ = realNumerator / denominator
		   newW = complexNumerator / denominator  */
		
		BigDecimal denominator = c.multiply(z).add(d).pow(2).add(c.multiply(w).pow(2));
		BigDecimal realNumerator = a.multiply(z).add(b).multiply(c.multiply(z).add(d)).add(a.multiply(c).multiply(w.pow(2)));
		BigDecimal complexNumerator = a.multiply(w).multiply(c.multiply(z).add(d))
		                    .subtract(c.multiply(w).multiply(a.multiply(z).add(b)));
		BigDecimal newZ = realNumerator.divide(denominator, mathContext);
		BigDecimal newW = complexNumerator.divide(denominator, mathContext);
		
		return new BigDecimalHalfPlaneRepresentation(newZ, newW);
	}

	@Override
	public HyperbolicRigidMotion inverse() {
		return new BigDecimalHalfPlaneMotion(d, b.negate(), c.negate(), a);
	}

	@Override
	public HyperbolicRigidMotion composeWith(HyperbolicRigidMotion other) {
		if (other instanceof BigDecimalHalfPlaneMotion) {
			BigDecimalHalfPlaneMotion otherMotion = (BigDecimalHalfPlaneMotion) other;
			BigDecimal e = otherMotion.a;
			BigDecimal f = otherMotion.b;
			BigDecimal g = otherMotion.c;
			BigDecimal h = otherMotion.d;
			/*  a*e+b*g, a*f+b*h, c*e+d*g, c*f+d*h  */
			return new BigDecimalHalfPlaneMotion(a.multiply(e).add(b.multiply(g)),
					                   a.multiply(f).add(b.multiply(h)),
					                   c.multiply(e).add(d.multiply(g)),
					                   c.multiply(f).add(d.multiply(h)));
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
	public HyperbolicRigidMotion projection() {
		throw new UnsupportedOperationException("Not supported !");
	}

}
