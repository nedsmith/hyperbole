/**
 * 
 */
package hyperbolic;

/**
 * @author Ned
 *
 */
public class HalfPlaneMotionGenerator implements HyperbolicRigidMotionGenerator {

	@Override
	public HyperbolicRigidMotion identity() {
		return new HalfPlaneMotion();
	}

	@Override
	public HyperbolicRigidMotion rotateAboutDiskCenter(double radians) {
		double cosTheta = Math.cos(radians);
		double sinTheta = Math.sin(radians);
		return new HalfPlaneMotion(1+cosTheta, sinTheta, -sinTheta, 1+cosTheta);
	}

	@Override
	public HyperbolicRigidMotion translatePlane(double distance) {
		return new HalfPlaneMotion(1,distance,0,1);
	}

	@Override
	public HyperbolicRigidMotion scalePlane(double factor) {
		return new HalfPlaneMotion(factor, 0, 0, 1);
	}

	@Override
	public HyperbolicRigidMotion invertPlane() {
		return new HalfPlaneMotion(0,1,-1,0);
	}

}
