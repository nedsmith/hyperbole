/**
 * 
 */
package hyperbolic;

/**
 * This interface represents a hyperbolic line segment, i.e. a line from a start point to an end point.
 * 
 * @author Ned
 */
public interface HyperbolicLine {
	
	HyperbolicPoint getStartPoint();
	
	HyperbolicPoint getEndPoint();

}
