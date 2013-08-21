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
public class HyperbolicDistanceTest {

	HyperbolicPointGenerator pointGenerator = new HalfPlanePointGenerator();
	double tolerance = 1e-3;
	
	private void testEqual(double d1, double d2) {
		if (Math.abs(d1-d2)>tolerance)
			fail("Expected "+d1+" but was "+d2);
	}
	
	@Test
	public void testHalfPlaneDistance() {
		HyperbolicPoint p1 = pointGenerator.halfPlane(1, 2);
		HyperbolicPoint p2 = pointGenerator.halfPlane(2, 4);
		HyperbolicPoint p3 = pointGenerator.halfPlane(-3, 0.5);
		HyperbolicPoint p4 = pointGenerator.halfPlane(-6, 1);
		
		double dist1 = p1.distanceFrom(p3);
		double dist2 = p2.distanceFrom(p4);
		testEqual(dist1, dist2);
	}
	
	@Test
	public void testDiskDistance() {
		HyperbolicPoint p1 = pointGenerator.disk(1, 2);
		HyperbolicPoint p2 = pointGenerator.disk(2, -1);
		HyperbolicPoint p3 = pointGenerator.disk(-3, 4);
		HyperbolicPoint p4 = pointGenerator.disk(4, 3);
		
		double dist1 = p1.distanceFrom(p3);
		double dist2 = p2.distanceFrom(p4);
		testEqual(dist1, dist2);
	}
	
}
