/**
 * 
 */
package display;

import hyperbolic.HalfPlaneCollisionDetector;
import hyperbolic.HalfPlaneMotionGenerator;
import hyperbolic.HalfPlanePointGenerator;
import hyperbolic.HyperbolicCollisionDetector;
import hyperbolic.HyperbolicPoint;
import hyperbolic.HyperbolicPointGenerator;
import hyperbolic.HyperbolicRigidMotion;
import hyperbolic.HyperbolicRigidMotionGenerator;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ned
 *
 */
public class TerrainGenerator implements HyperbolicPolyTessellation {

	
	private HyperbolicPointGenerator pointGenerator = new HalfPlanePointGenerator();
	private HyperbolicRigidMotionGenerator motionGenerator = new HalfPlaneMotionGenerator();
	
//	private RandomNumberGenerator rng = RandomNumberGeneratorFactory.makeDefaultRng();
//	private List<HyperbolicRigidMotion> verticies = new ArrayList<HyperbolicRigidMotion>();
	private List<HyperbolicPoly> polySet = new ArrayList<HyperbolicPoly>();
	private HyperbolicCollisionDetector<Object> collisionDetector = new HalfPlaneCollisionDetector<Object>();
	
	
	private final double forwardUnit = 1.1;
	private final double width = 1.05;
	
	private final HyperbolicPoint diskCenter = pointGenerator.diskCenter();
	private final HyperbolicRigidMotion forward = motionGenerator.scalePlane(forwardUnit);
	private final HyperbolicRigidMotion left = 
		motionGenerator.rotateAboutDiskCenter(-Math.PI / 2.0)
		.composeWith(motionGenerator.scalePlane(width));
	private final HyperbolicRigidMotion right = 
		motionGenerator.rotateAboutDiskCenter(Math.PI / 2.0)
		.composeWith(motionGenerator.scalePlane(width));
	
	
	private HyperbolicPoint point(HyperbolicRigidMotion motion) {
		return motion.transform(diskCenter);
	}
	
	private void advance(HyperbolicRigidMotion vertex) {
		HyperbolicPoint[] points = new HyperbolicPoint[4];
		HyperbolicRigidMotion position = vertex;
		points[0] = point(position.composeWith(left));
		points[1] = point(position.composeWith(right));
		position = position.composeWith(forward);
		collisionDetector.add(new Object(), position.transform(diskCenter));
		points[2] = point(position.composeWith(right));
		points[3] = point(position.composeWith(left));
		HyperbolicPoly poly = new HyperbolicPoly(points);
		polySet.add(poly);
	}
		
	
	@Override
	public List<HyperbolicPoly> makePolys() {
		advance(motionGenerator.identity());
		return polySet;
	}

	@Override
	public List<HyperbolicPoly> makePolysNear(HyperbolicPoint point) {
		return makePolys();
	}

}
