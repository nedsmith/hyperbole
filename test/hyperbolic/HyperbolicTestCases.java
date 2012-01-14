/**
 * 
 */
package hyperbolic;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * @author Ned
 *
 */
public class HyperbolicTestCases {
	
	HyperbolicPointGenerator pointGenerator = new HalfPlanePointGenerator();
	HyperbolicRigidMotionGenerator motionGenerator = new HalfPlaneMotionGenerator();
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
	
	void testEqualHalfPlane(HyperbolicPoint point, double x, double y) {
		testEqual(point.getHalfPlanePosition(), x, y);
	}
	
	void testEqualDisk(HyperbolicPoint point, double x, double y) {
		testEqual(point.getDiskPosition(), x, y);
	}
	
	@Test
	public void testTranslatePlane() {
		
		HyperbolicPoint middle = pointGenerator.diskCenter();
		HyperbolicRigidMotion moveRight = motionGenerator.translatePlane(1.0);
		
		HyperbolicPoint transformed = moveRight.transform(middle);
		testEqualHalfPlane(transformed, 1, 1);
		
		HyperbolicPoint again = moveRight.transform(transformed);
		testEqualHalfPlane(again, 2, 1);
		
		HyperbolicRigidMotion moveLeft = moveRight.inverse();
		HyperbolicPoint transformedBackwards = moveLeft.transform(middle);
		testEqualHalfPlane(transformedBackwards, -1, 1);
		
		HyperbolicRigidMotion moveRightTwice = moveRight.composeWith(moveRight);
		HyperbolicPoint transformedTwice = moveRightTwice.transform(middle);
		testEqualHalfPlane(transformedTwice, 2, 1);
	}
	
	@Test
	public void testScalePlane() {
		HyperbolicPoint middle = pointGenerator.diskCenter();
		HyperbolicRigidMotion grow = motionGenerator.scalePlane(2.0);
		HyperbolicPoint big = grow.transform(middle);
		testEqualHalfPlane(big, 0, 2);
		
		HyperbolicPoint huge = grow.transform(big);
		testEqualHalfPlane(huge, 0, 4);
		
		HyperbolicPoint toRight = motionGenerator.translatePlane(1).transform(middle);
		testEqualHalfPlane(toRight, 1, 1);
		HyperbolicPoint bigRight = grow.transform(toRight);
		testEqualHalfPlane(bigRight, 2, 2);
		
		HyperbolicRigidMotion shrink = grow.inverse();
		HyperbolicPoint littleRight = shrink.transform(toRight);
		testEqualHalfPlane(littleRight, .5, .5);
	}
	
	@Test
	public void testInvert() {
		HyperbolicPoint middle = pointGenerator.diskCenter();
		HyperbolicRigidMotion inversion = motionGenerator.invertPlane();
		HyperbolicPoint inverted = inversion.transform(middle);
		testEqualHalfPlane(inverted, 0, 1);
	}
	
	@Test
	public void testRotate() {
		HyperbolicPoint point = pointGenerator.disk(0.3, 0.4);
		HyperbolicRigidMotion spin = motionGenerator.rotateAboutDiskCenter(Math.PI / 2.0);
		HyperbolicPoint spinned = spin.transform(point);
		testEqualDisk(spinned, -0.4, 0.3);
	}
	
	private void testPlanePosition(double x, double y) {
		HyperbolicPoint point = pointGenerator.halfPlane(x, y);
		testEqualHalfPlane(point, x, y);
	}
	
	private void testDiskPosition(double x, double y) {
		HyperbolicPoint point = pointGenerator.disk(x, y);
		testEqualDisk(point, x, y);
	}
	
	@Test
	public void testPositions() {
		testPlanePosition(-6.5,2.3);
		testPlanePosition(24.5,5.2);
		testPlanePosition(0.04,6.4);
		testPlanePosition(385,0.42);
		testPlanePosition(-62,73.1);
		
		testDiskPosition(0.3, 0.4);
		testDiskPosition(-0.01, 0.2);
		testDiskPosition(0.5, -0.1);
		testDiskPosition(0.95, 0.02);
		testDiskPosition(-0.12, -0.21);
	}

}
