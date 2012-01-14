/**
 * 
 */
package hyperbolic;

/**
 * @author Ned
 *
 */
public class HalfPlanePointGenerator implements HyperbolicPointGenerator {

	@Override
	public HyperbolicPoint diskCenter() {
		return new HalfPlaneRepresentation();
	}

	@Override
	public HyperbolicPoint halfPlane(double x, double y) {
		return new HalfPlaneRepresentation(x,y);
	}

	@Override
	public HyperbolicPoint disk(double x, double y) {
		double denominator = (1-y)*(1-y)+x*x;
		double realNumerator = 2*x;
		double complexNumerator = 1-y*y-x*x;
		return new HalfPlaneRepresentation(realNumerator/denominator,
				                           complexNumerator/denominator);
	}

}
