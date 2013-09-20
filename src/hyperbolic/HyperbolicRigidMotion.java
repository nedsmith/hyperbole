/**
 * 
 */
package hyperbolic;

/**
 * @author Ned
 *
 */
public interface HyperbolicRigidMotion {
	
	HyperbolicPoint transform(HyperbolicPoint point);
	
	HyperbolicLine transform(HyperbolicLine line);
	
	HyperbolicRigidMotion inverse();
	
	HyperbolicRigidMotion composeWith(HyperbolicRigidMotion other);
	
	HyperbolicPoint intersection();

}
