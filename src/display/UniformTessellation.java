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
public final class UniformTessellation implements HyperbolicTessellation, HyperbolicPolyTessellation {

	private HyperbolicPointGenerator pointGenerator = new HalfPlanePointGenerator();
	private HyperbolicRigidMotionGenerator motionGenerator = new HalfPlaneMotionGenerator();
	private List<Vertex> vertices = new ArrayList<Vertex>();
	private boolean polyMode;
	private HyperbolicLineSet lineSet = new ArrayHyperbolicLineSet();
	private List<HyperbolicPoly> polySet = new ArrayList<HyperbolicPoly>();
	private int numVertices = 0;
	
	private double advanceFactor = Math.pow(2.414, 0.5);
	private int sideLength = 4;
	private double turnAngle = Math.PI / 3.0;
	private int pathways = 5;
	private int levels = 4;
	
	private HyperbolicPoint diskCenter = pointGenerator.diskCenter();
	private HyperbolicRigidMotion uTurn = motionGenerator.rotateAboutDiskCenter(Math.PI);
	private HyperbolicRigidMotion turn = motionGenerator.rotateAboutDiskCenter(turnAngle);
	
	private HyperbolicRigidMotion toFaceCentreLeft;
	private HyperbolicRigidMotion toFaceCentreRight;
	{
		final HyperbolicRigidMotion halfTurnLeft = motionGenerator.rotateAboutDiskCenter(-turnAngle/2.0);
		final HyperbolicRigidMotion halfTurnRight = motionGenerator.rotateAboutDiskCenter(turnAngle/2.0);
		final HyperbolicRigidMotion forward = motionGenerator.scalePlane(4.0);
		toFaceCentreLeft = halfTurnLeft.composeWith(forward);
		toFaceCentreRight = halfTurnRight.composeWith(forward);
	}
	
	private class Vertex {
		HyperbolicRigidMotion position;
		boolean colour;
	}
	
	private HyperbolicRigidMotion goForward = motionGenerator.scalePlane(advanceFactor);
	
	@Override
	public HyperbolicLineSet makeLineSet() {
		polyMode=false;
		expandFromStart();
		return lineSet;
	}
	
	private void expandFromStart() {
		Vertex vertex = new Vertex();
		vertex.position = motionGenerator.identity();
		vertex.colour = false;
		expandFrom(vertex);
	}
	
	private void expandFrom(Vertex startPosition) {
		lineSet.clearLines();
		polySet.clear();
		vertices.add(startPosition);
		for (int i=0; i<levels; i++) {
			expandVerticies();
		}
	}
	
	private void expandVerticies() {
		int firstVertex = numVertices;
		numVertices = vertices.size();
		int endVertex = vertices.size();
		for (int i=firstVertex; i<endVertex; i++) {
			expandVertex(vertices.get(i));
		}
	}
	
	private void expandVertex(Vertex vertex) {
		HyperbolicRigidMotion startPosition = vertex.position.composeWith(uTurn);
		for (int i=0; i<pathways; i++) {
			startPosition = startPosition.composeWith(turn);
			boolean colour = i%2==0==vertex.colour;
			HyperbolicRigidMotion endPosition = advanceAlongLine(startPosition, colour);
			Vertex newVertex = new Vertex();
			newVertex.position = endPosition;
			newVertex.colour = colour;
			vertices.add(newVertex);
		}
	}
	
	private HyperbolicRigidMotion advanceAlongLine(HyperbolicRigidMotion startPosition, boolean colour) {
		HyperbolicRigidMotion positionAndDirection = startPosition;
		HyperbolicPoint faceCentre = null;
		if (polyMode) {
			HyperbolicRigidMotion toFaceCentre  = colour ? toFaceCentreLeft : toFaceCentreRight;
			faceCentre = positionAndDirection.composeWith(toFaceCentre).transform(diskCenter);
		}
		for (int i=0; i<sideLength; i++) {
			HyperbolicPoint currentPosition = positionAndDirection.transform(diskCenter);
			positionAndDirection = positionAndDirection.composeWith(goForward);
			HyperbolicPoint newPosition = positionAndDirection.transform(diskCenter);
			if (polyMode) {
				polySet.add(new HyperbolicPoly(currentPosition, newPosition, faceCentre));
			}
			else
				lineSet.addLine(new SimpleHyperbolicLine(currentPosition, newPosition));
		}
		return positionAndDirection;
	}
	
	private void expandNear(HyperbolicPoint point) {
		double minDistance = Double.MAX_VALUE;
		Vertex closestVertex = null;
		for (Vertex vertex : vertices) {
			HyperbolicPoint transform = vertex.position.inverse().transform(point);
			double[] diskPosition = transform.getDiskPosition();
			double distance = 
					diskPosition[0]*diskPosition[0]+diskPosition[1]*diskPosition[1];
			if (distance < minDistance) {
				minDistance = distance;
				closestVertex = vertex;
			}
			
		}
		vertices.clear();
		numVertices = 0;
		expandFrom(closestVertex);
	}

	@Override
	public HyperbolicLineSet makeLineSetNear(HyperbolicPoint position) {
		polyMode=false;
		expandNear(position);
		return lineSet;
	}

	@Override
	public List<HyperbolicPoly> makePolys() {
		polyMode=true;
		expandFromStart();
		return polySet;
	}

	@Override
	public List<HyperbolicPoly> makePolysNear(HyperbolicPoint point) {
		polyMode=true;
		expandNear(point);
		return polySet;
	}

}
