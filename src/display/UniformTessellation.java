/**
 * 
 */
package display;

import hyperbolic.HalfPlaneMotionGenerator;
import hyperbolic.HalfPlanePointGenerator;
import hyperbolic.HyperbolicPoint;
import hyperbolic.HyperbolicPointGenerator;
import hyperbolic.HyperbolicRigidMotion;
import hyperbolic.HyperbolicRigidMotionGenerator;
import hyperbolic.SimpleHyperbolicLine;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ned
 *
 */
public final class UniformTessellation implements HyperbolicTessellation {

	private HyperbolicPointGenerator pointGenerator = new HalfPlanePointGenerator();
	private HyperbolicRigidMotionGenerator motionGenerator = new HalfPlaneMotionGenerator();
	private List<HyperbolicRigidMotion> vertices = new ArrayList<HyperbolicRigidMotion>();
	private HyperbolicLineSet lineSet = new HashHyperbolicLineSet();
	private HyperbolicPoint diskCenter = pointGenerator.diskCenter();
	private int numVertices = 0;
	
	private double advanceFactor = Math.pow(2.414, 0.5);
	private double sideLength = 4;
	private double turnAngle = Math.PI / 3.0;
	private int pathways = 5;
	private int levels = 4;
	
	private HyperbolicRigidMotion goForward = motionGenerator.scalePlane(advanceFactor);
	
	@Override
	public HyperbolicLineSet makeLineSet() {
		vertices.add(motionGenerator.identity());
		for (int i=0; i<levels; i++) {
			expandVerticies();
			System.out.println(numVertices);
		}
		return lineSet;
	}
	
	private void expandVerticies() {
		int firstVertex = numVertices;
		numVertices = vertices.size();
		int endVertex = vertices.size();
		for (int i=firstVertex; i<endVertex; i++) {
			expandVertex(vertices.get(i));
		}

	}
	
	private void expandVertex(HyperbolicRigidMotion vertex) {
		HyperbolicRigidMotion uTurn = motionGenerator.rotateAboutDiskCenter(Math.PI);
		HyperbolicRigidMotion startPosition = vertex.composeWith(uTurn);
		HyperbolicRigidMotion turn = motionGenerator.rotateAboutDiskCenter(turnAngle);
		for (int i=0; i<pathways; i++) {
			startPosition = startPosition.composeWith(turn);
			HyperbolicRigidMotion endPosition = advanceFrom(startPosition);
			vertices.add(endPosition);
		}
	}
	
	private HyperbolicRigidMotion advanceFrom(HyperbolicRigidMotion startPosition) {
		HyperbolicRigidMotion positionAndDirection = startPosition;
		for (int i=0; i<sideLength; i++) {
			HyperbolicPoint currentPosition = positionAndDirection.transform(diskCenter);
			positionAndDirection = positionAndDirection.composeWith(goForward);
			HyperbolicPoint newPosition = positionAndDirection.transform(diskCenter);
			lineSet.addLine(new SimpleHyperbolicLine(currentPosition, newPosition));
		}
		return positionAndDirection;
	}

}
