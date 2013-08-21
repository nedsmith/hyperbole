/**
 * 
 */
package hyperbolic;

/**
 * @author Ned
 *
 */
public interface HyperbolicPoint {
	
	double[] getHalfPlanePosition();
	
	double[] getDiskPosition();
	
	double distanceFrom(HyperbolicPoint other);

}
