/**
 * 
 */
package hyperbolic;

/**
 * @author Ned
 *
 */
public interface HyperbolicPointGenerator {
	
	HyperbolicPoint diskCenter();
	
	HyperbolicPoint halfPlane(double x, double y);
	
	HyperbolicPoint disk(double x, double y);

}
