/**
 * 
 */
package hyperbolic;

/**
 * @author Ned
 *
 */
public interface HyperbolicRigidMotionGenerator {
	
	HyperbolicRigidMotion identity();
	
	HyperbolicRigidMotion rotateAboutDiskCenter(double radians);
	
	HyperbolicRigidMotion translatePlane(double distance);
	
	HyperbolicRigidMotion scalePlane(double factor);
	
	HyperbolicRigidMotion invertPlane();

}
