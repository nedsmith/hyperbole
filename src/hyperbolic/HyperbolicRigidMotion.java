/**
 * 
 */
package hyperbolic;

/**
 * A rigid motion is a mapping of the hyperbolic plane to itself that preserves (hyperbolic) distance.
 * 
 * Rotation around a point and scaling of the half-plane representation are examples of this.
 * 
 * @author Ned
 */
public interface HyperbolicRigidMotion {
	
	/**
	 * Transform a point under this motion
	 * @param point Point to transform
	 * @return the transformed point
	 */
	HyperbolicPoint transform(HyperbolicPoint point);
	
	/**
	 * Transform a line segment under this motion
	 * @param line line to transform
	 * @return The transformed line
	 */
	HyperbolicLine transform(HyperbolicLine line);
	
	/**
	 * @return The inverse of this motion. Any motion composed with its inverse will result in the identity.
	 */
	HyperbolicRigidMotion inverse();
	
	/**
	 * Compose this motion with another
	 * @param other The other motion to compose with
	 * @return The motion resulting from the composition
	 */
	HyperbolicRigidMotion composeWith(HyperbolicRigidMotion other);
		
	HyperbolicRigidMotion projection();
	
	double intersectionAngle();

}
