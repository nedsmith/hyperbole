/**
 * 
 */
package hyperbolic;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * @author Ned
 *
 */
public class HyperbolicProjectionTest {

	HyperbolicRigidMotionGenerator motionGenerator = new HalfPlaneMotionGenerator();
	HyperbolicPointGenerator pointGenerator = new HalfPlanePointGenerator();
	
	double tolerance = 1e-3;
	
	void testEqual(double [] point, double x, double y) {
		if (point.length != 2) {
			fail("Not a 2-vector");
		}
		if (Math.abs(x - point[0]) > tolerance
		 || Math.abs(y - point[1]) > tolerance) {
			fail("expected "+x+","+y+" but was "+point[0]+","+point[1]);
		}
	}
	
	/**
	 * Test that projection of scaling plane is equal to itself
	 */
	@Test
	public void testScalePlane() {
		final HyperbolicRigidMotion motion = motionGenerator.scalePlane(3);
		final HyperbolicRigidMotion projection = motion.projection();
		
		HyperbolicPoint transform = projection.transform(pointGenerator.diskCenter());
		testEqual(transform.getHalfPlanePosition(), 0, 3);
		transform = projection.transform(pointGenerator.halfPlane(2,4));
		testEqual(transform.getHalfPlanePosition(), 6, 12);
	}
	
	/**
	 * Test that projection of a rotation is the identity
	 */
	@Test
	public void testRotation() {
		final HyperbolicRigidMotion motion = motionGenerator.rotateAboutDiskCenter(0.86);
		final HyperbolicRigidMotion projection = motion.projection();
		
		HyperbolicPoint transform = projection.transform(pointGenerator.diskCenter());
		testEqual(transform.getHalfPlanePosition(), 0, 1);
		transform = projection.transform(pointGenerator.halfPlane(2,4));
		testEqual(transform.getHalfPlanePosition(), 2, 4);
	}
	
	/**
	 * Test that a line ultraparallel to the y axis has no projection
	 */
	@Test
	public void testUltraParallel() {
		final HyperbolicRigidMotion forward = motionGenerator.scalePlane(2);
		final HyperbolicRigidMotion turn = motionGenerator.rotateAboutDiskCenter(Math.PI/2.0);
		final HyperbolicRigidMotion motion = turn.composeWith(forward).composeWith(turn.inverse());
		final HyperbolicRigidMotion projection = motion.projection();
		assertNull("Projection should be null", projection);
	}
	
	/**
	 * Test that projection of a movement at 90 degrees is the identity
	 */
	@Test
	public void testMoveAt90Degrees() {
		final HyperbolicRigidMotion forward = motionGenerator.scalePlane(2);
		final HyperbolicRigidMotion turn = motionGenerator.rotateAboutDiskCenter(Math.PI/2.0);
		final HyperbolicRigidMotion motion = turn.composeWith(forward);
		final HyperbolicRigidMotion projection = motion.projection();
		
		HyperbolicPoint transform = projection.transform(pointGenerator.diskCenter());
		testEqual(transform.getHalfPlanePosition(), 0, 1);
		transform = projection.transform(pointGenerator.halfPlane(2,4));
		testEqual(transform.getHalfPlanePosition(), 2, 4);
	}
	
	/**
	 * Test that projection of a movement followed by a rotation is just the movement
	 */
	@Test
	public void testMoveThenRotateIsJustMove() {
		final HyperbolicRigidMotion forward = motionGenerator.scalePlane(2);
		final HyperbolicRigidMotion turn = motionGenerator.rotateAboutDiskCenter(Math.PI/2.0);
		final HyperbolicRigidMotion motion = forward.composeWith(turn);
		final HyperbolicRigidMotion projection = motion.projection();
		
		HyperbolicPoint transform = projection.transform(pointGenerator.diskCenter());
		testEqual(transform.getHalfPlanePosition(), 0, 2);
		transform = projection.transform(pointGenerator.halfPlane(2,4));
		testEqual(transform.getHalfPlanePosition(), 4, 8);
	}
}
