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

import random.RandomNumberGenerator;
import random.RandomNumberGeneratorFactory;

/**
 * @author Ned
 *
 */
public class TerrainGenerator implements HyperbolicPolyTessellation {

	
	private HyperbolicPointGenerator pointGenerator = new HalfPlanePointGenerator();
	private HyperbolicRigidMotionGenerator motionGenerator = new HalfPlaneMotionGenerator();
	
	private RandomNumberGenerator rng = RandomNumberGeneratorFactory.makeDefaultRng();
	private List<HyperbolicRigidMotion> verticies = new ArrayList<HyperbolicRigidMotion>();
	private List<HyperbolicPoly> polySet = new ArrayList<HyperbolicPoly>();
	private HyperbolicCollisionDetector<Object> collisionDetector = new HalfPlaneCollisionDetector<Object>();
	
	
	private final double forwardUnit = 1.1;
	private final double width = 1.05;
	private final int iterations = 250;
	
	private final HyperbolicPoint diskCenter = pointGenerator.diskCenter();
	private final HyperbolicRigidMotion forward = motionGenerator.scalePlane(forwardUnit);
	private final HyperbolicRigidMotion turnLeft = motionGenerator.rotateAboutDiskCenter(-Math.PI / 2.0);
	private final HyperbolicRigidMotion turnRight = motionGenerator.rotateAboutDiskCenter(Math.PI / 2.0);
	private final HyperbolicRigidMotion stepLeft = 
		turnLeft.composeWith(motionGenerator.scalePlane(width));
	private final HyperbolicRigidMotion stepRight = 
		turnRight.composeWith(motionGenerator.scalePlane(width));
	
	private HyperbolicPoint point(HyperbolicRigidMotion motion) {
		return motion.transform(diskCenter);
	}
	
	@Override
	public List<HyperbolicPoly> makePolys() {
		verticies.add(motionGenerator.identity());
		for (int i=0; i<iterations; i++) {
			iterate();
		}
		return polySet;
	}
	
	private void iterate() {
		int numVertices = verticies.size();
		int i = rng.randomInt(numVertices);
		expand(verticies.get(i));
	}
	
	private void expand(HyperbolicRigidMotion vertex) {
		if (rng.random()<0.5) advance(vertex);
		if (rng.random()<0.3) advance(vertex.composeWith(turnLeft));
		if (rng.random()<0.3) advance(vertex.composeWith(turnRight));
	}
	
	private void advance(HyperbolicRigidMotion vertex) {
		HyperbolicRigidMotion position = vertex;
		int numSteps = rng.randomInt(30);
		for (int i=0; i<numSteps; i++) {
			position = takeOneStep(position);
		}
		verticies.add(position);
	}
	
	private HyperbolicRigidMotion takeOneStep(HyperbolicRigidMotion vertex) {
		HyperbolicPoint[] points = new HyperbolicPoint[4];
		HyperbolicRigidMotion position = vertex;
		points[0] = point(position.composeWith(stepLeft));
		points[1] = point(position.composeWith(stepRight));
		position = position.composeWith(forward);
		collisionDetector.add(new Object(), position.transform(diskCenter));
		points[2] = point(position.composeWith(stepRight));
		points[3] = point(position.composeWith(stepLeft));
		HyperbolicPoly poly = new HyperbolicPoly(points);
		polySet.add(poly);
		return position;
	}

	@Override
	public List<HyperbolicPoly> makePolysNear(HyperbolicPoint point) {
		return makePolys();
	}

}
