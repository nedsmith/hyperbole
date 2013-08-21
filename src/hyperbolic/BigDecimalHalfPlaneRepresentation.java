/**
 * 
 */
package hyperbolic;

import static java.lang.Math.log;
import static java.lang.Math.sqrt;

import java.math.BigDecimal;
import java.math.MathContext;

/**
 * @author Ned
 *
 */
public final class BigDecimalHalfPlaneRepresentation implements HyperbolicPoint {

	private final BigDecimal x;
	private final BigDecimal y;
	private static MathContext mathContext = new MathContext(20);
	
	public BigDecimalHalfPlaneRepresentation() {
		this(0,1);
	}
	
	public BigDecimalHalfPlaneRepresentation(BigDecimal x, BigDecimal y) {
		this.x = x;
		this.y = y;
	}
	
	public BigDecimalHalfPlaneRepresentation(double x, double y) {
		this(new BigDecimal(x), new BigDecimal(y));
	}
	
	@Override
	public double[] getHalfPlanePosition() {
		return new double[]{x.doubleValue(),y.doubleValue()};
	}

	@Override
	public double[] getDiskPosition() {
		/*double denominator = (1+y)*(1+y)+x*x;
		double realNumerator = 2*x;
		double complexNumerator = x*x+y*y-1;
		return new double[]{realNumerator/denominator,
				            complexNumerator/denominator};*/
		BigDecimal denominator = y.add(BigDecimal.ONE).pow(2).add(x.pow(2));
		BigDecimal realNumerator = x.add(x);
		BigDecimal complexNumerator = x.pow(2).add(y.pow(2)).subtract(BigDecimal.ONE);
		BigDecimal realPart = realNumerator.divide(denominator, mathContext);
		BigDecimal complexPart = complexNumerator.divide(denominator, mathContext);
		return new double[]{realPart.doubleValue(), complexPart.doubleValue()};
	}

	@Override
	public double distanceFrom(HyperbolicPoint other) {
		BigDecimal x2, y2;
		if (other instanceof BigDecimalHalfPlaneRepresentation) {
			BigDecimalHalfPlaneRepresentation oth = (BigDecimalHalfPlaneRepresentation)other;
			x2 = oth.x;
			y2 = oth.y;
		}
		else {
			double[] coords = other.getHalfPlanePosition();
			x2 = new BigDecimal(coords[0]);
			y2 = new BigDecimal(coords[1]);
		}
		
		/* double z = ((x-x2)*(x-x2)+(y-y2)*(y-y2))/(2*y*y2);
		   return acosh(z) */
		
		BigDecimal numerator = x.subtract(x2).pow(2).add(y.subtract(y2).pow(2));
		BigDecimal denominator = y.multiply(y2).multiply(BigDecimal.valueOf(2));
		BigDecimal z = numerator.divide(denominator, mathContext);
		return acosh(z.doubleValue());
	}
	
	private double acosh(double z) {
		return log(z+sqrt(z+1)*sqrt(z-1));
	}

}
