/**
 * 
 */
package game;

import hyperbolic.HalfPlaneCollisionDetector;
import hyperbolic.HalfPlaneMotionGenerator;
import hyperbolic.HalfPlanePointGenerator;
import hyperbolic.HyperbolicCollisionDetector;
import hyperbolic.HyperbolicPoint;
import hyperbolic.HyperbolicPointGenerator;
import hyperbolic.HyperbolicRigidMotion;
import hyperbolic.HyperbolicRigidMotionGenerator;
import hyperbolic.SimpleHyperbolicLine;

import java.awt.Color;

import display.HyperbolicLineDrawing;
import display.HyperbolicLineSet;
import display.HyperbolicPicture;
import display.HyperbolicPoly;
import display.HyperbolicPolyDrawing;
import display.SimpleHyperbolicPicture;
import display.TerrainGenerator;
import display.UniformTessellation;

/**
 * This class contains most of the high-level functionality for the game including
 * the background, obstacles and movement of the player and camera.
 * 
 * @author Ned
 */
public class HyperbolicView {
	

	private HyperbolicPolyDrawing player = new HyperbolicPolyDrawing(new Color(80,220,255));
	private HyperbolicLineDrawing trail = new HyperbolicLineDrawing(Color.red);
	private HyperbolicPicture picture = new SimpleHyperbolicPicture();
	
	private HyperbolicPointGenerator pointGenerator = new HalfPlanePointGenerator();
	private HyperbolicRigidMotionGenerator motionGenerator = new HalfPlaneMotionGenerator();
	
	private HyperbolicRigidMotion positionAndDirection = motionGenerator.identity();
	private HyperbolicRigidMotion turns = motionGenerator.identity();
	private boolean gridMode=false;
	
	private HyperbolicCollisionDetector<Object> collisionDetector = new HalfPlaneCollisionDetector<Object>();
	
	private double currentTurn = 0;
	private int direction = 0;
	private boolean trailOn = false;
	
	private int counter = 0;
	private HyperbolicPoint[] playerDrawing = new HyperbolicPoint[] {
		pointGenerator.disk(0, 0.04),
		pointGenerator.disk(0.02, -0.02),
		pointGenerator.disk(-0.02, -0.02),
	};
	
	private UniformTessellation tessellation = new UniformTessellation();
	private HyperbolicLineSet tessellationLines = tessellation.makeLineSet();
	private HyperbolicPolyDrawing tessellationPolyDrawing;
	private TerrainGenerator terrainGenerator = new TerrainGenerator();
	private HyperbolicPolyDrawing terrain = new HyperbolicPolyDrawing(new Color(20,100,20));
	
	public HyperbolicView() {
		if (gridMode)
			picture.addDrawing(new HyperbolicLineDrawing(tessellationLines, Color.green));
		else {
			tessellationPolyDrawing = new HyperbolicPolyDrawing(new Color(0,40,0));
			picture.addDrawing(tessellationPolyDrawing);
		}
		terrain.setPolys(terrainGenerator.makePolys());
		for (HyperbolicPoint point : terrainGenerator.points()) {
			collisionDetector.add(new Object(), point);
		}
		picture.addDrawing(terrain);
		picture.addDrawing(trail);
		picture.addDrawing(player);
	}
	
	public void setTurn(double turn) {
		this.currentTurn = turn;
	}
	
	public void setDirection(int direction) {
		this.direction = direction;
	}
	
	public void setTrailOn(boolean trailOn) {
		this.trailOn = trailOn;
	}
	
	public void update() {
		double turnToUse = currentTurn;
		HyperbolicRigidMotion turn = motionGenerator.rotateAboutDiskCenter(turnToUse);
		HyperbolicRigidMotion goForward = motionGenerator.scalePlane(1.0 + (double)direction * 0.03);
		
		HyperbolicPoint currentPosition = positionAndDirection.transform(pointGenerator.diskCenter());
		positionAndDirection = positionAndDirection.composeWith(turn);
		HyperbolicRigidMotion newPositionAndDirection = positionAndDirection.composeWith(goForward);
		HyperbolicPoint newPosition = newPositionAndDirection.transform(pointGenerator.diskCenter());
		if (!collisionDetector.detectCollisions(newPosition).isEmpty()) {
			System.out.println("Collision!!");
		}
		else {
			positionAndDirection = newPositionAndDirection;
			if (direction!=0 && trailOn) {
				trail.getLineSet().addLine(new SimpleHyperbolicLine(currentPosition, newPosition));
			}
		}
		
		drawPlayer();
		
		if (counter%50==0)
			System.out.println("Distance: "+currentPosition.distanceFrom(pointGenerator.diskCenter()));
		
		if (counter%50==0) {
			updateTessellation(currentPosition);
		}

		turns = turns.composeWith(turn);
		HyperbolicRigidMotion cameraAngle = positionAndDirection.composeWith(turns.inverse());
		
		picture.setRigidMotion(cameraAngle.inverse());
		counter++;
	}
	
	private void updateTessellation(HyperbolicPoint currentPosition) {
		if (gridMode) {
			HyperbolicLineSet newLines = tessellation.makeLineSetNear(currentPosition);
			tessellationLines.setLines(newLines.getLines());
		}
		else {
			tessellationPolyDrawing.setPolys(tessellation.makePolysNear(currentPosition));
		}
	}
	
	private void drawPlayer() {
		player.clearPolys();
		int points = playerDrawing.length;
		HyperbolicPoint[] newPoints = new HyperbolicPoint[3];
		for (int i=0; i<points; i++) {
			newPoints[i] = positionAndDirection.transform(playerDrawing[i]);
		}
		player.addPoly(new HyperbolicPoly(newPoints));
	}
	
	public HyperbolicPicture getPicture() {
		return picture;
	}
}
