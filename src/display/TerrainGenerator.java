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
	private List<Vertex> vertices = new ArrayList<Vertex>();
	private List<HyperbolicPoly> polySet = new ArrayList<HyperbolicPoly>();
	private HyperbolicCollisionDetector<Vertex> collisionDetector = new HalfPlaneCollisionDetector<Vertex>(0.3);
	
	
	private final double forwardUnit = 1.1;
	private final double width = 1.05;
	private final int iterations = 300;
	
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
	
	private class Vertex {
		Vertex parent;
		HyperbolicRigidMotion position;
		boolean collided= false;
		
		Vertex(HyperbolicRigidMotion position, Vertex parent) {
			this.position = position;
			this.parent = parent;
		}
		
		Vertex compose(HyperbolicRigidMotion motion) {
			return new Vertex(position.composeWith(motion), this);
		}
	}
	
	private void makeInitialSet() {
		for (int i=0; i<5; i++) {
			double angle = rng.random()*Math.PI*2.0;
			double distance = 1.0 + rng.random()*5.0;
			HyperbolicRigidMotion motion = motionGenerator
					.rotateAboutDiskCenter(angle)
					.composeWith(motionGenerator.scalePlane(distance));
			vertices.add(new Vertex(motion, null));
		}
	}
	
	@Override
	public List<HyperbolicPoly> makePolys() {
		makeInitialSet();
		for (int i=0; i<iterations; i++) {
			iterate();
		}
		return polySet;
	}
	
	private void iterate() {
		int numVertices = vertices.size();
		int i = rng.randomInt(numVertices);
		Vertex vertex = vertices.get(i);
		expand(vertex);
		if (vertex.collided) vertices.remove(i);
	}
	
	private void expand(Vertex vertex) {
		if (rng.random()<0.3) advance(vertex);
		if (rng.random()<0.7) advance(vertex.compose(turnLeft));
		if (rng.random()<0.7) advance(vertex.compose(turnRight));
	}
	
	private void advance(Vertex vertex) {
		int numSteps = rng.randomInt(5)+4;
		for (int i=0; i<numSteps; i++) {
			takeOneStep(vertex);
			if (vertex.collided) return;
		}
		vertices.add(vertex);
	}
	
	private void takeOneStep(Vertex vertex) {
		HyperbolicPoint[] points = new HyperbolicPoint[4];
		HyperbolicRigidMotion position = vertex.position;
		points[0] = point(position.composeWith(stepLeft));
		points[1] = point(position.composeWith(stepRight));
		position = position.composeWith(forward);
		checkCollisions(vertex, point(position));
		if (vertex.collided) return;
		vertex.position = position;
		points[2] = point(position.composeWith(stepRight));
		points[3] = point(position.composeWith(stepLeft));
		HyperbolicPoly poly = new HyperbolicPoly(points);
		polySet.add(poly);
	}
	
	private void checkCollisions(Vertex vertex, HyperbolicPoint point) {
		List<Vertex> collisions = collisionDetector.detectCollisions(point);
		for (Vertex otherVertex : collisions) {
			if (mayCollide(vertex, otherVertex)) {
				vertex.collided = true;
				return;
			}
		}
		collisionDetector.add(vertex, point);
	}
	
	private boolean mayCollide(Vertex v1, Vertex v2) {
		if (v1.parent==v2) return false;
		if (v2.parent==v1) return false;
		if (v1.parent==v2.parent) return false;
		return true;
	}

	@Override
	public List<HyperbolicPoly> makePolysNear(HyperbolicPoint point) {
		return makePolys();
	}
	
	public List<HyperbolicPoint> points() {
		return collisionDetector.points();
	}

}
