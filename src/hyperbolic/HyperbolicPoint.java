/**
 * 
 */
package hyperbolic;

/**
 * Interface for objects representing a hyperbolic point
 * 
 * @author Ned
 */
public interface HyperbolicPoint {
	
	/**
	 * @return The coordinates of this point in the half-plane
	 * representation of the hyperbolic plane, as an array of two doubles
	 * with the x coordinate first and the y coordinate second.
	 */
	double[] getHalfPlanePosition();
	
	/**
	 * @return The coordinates of this point in the disk
	 * representation of the hyperbolic plane, as an array of two doubles
	 * with the x coordinate first and the y coordinate second.
	 */
	double[] getDiskPosition();
	
	/**
	 * @param other Other point to compare to
	 * @return the hyperbolic distance from this point to the other point
	 */
	double distanceFrom(HyperbolicPoint other);

}
