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

}
