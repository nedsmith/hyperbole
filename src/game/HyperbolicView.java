/**
 * 
 */
package game;

import hyperbolic.HalfPlaneMotionGenerator;
import hyperbolic.HalfPlanePointGenerator;
import hyperbolic.HyperbolicPoint;
import hyperbolic.HyperbolicPointGenerator;
import hyperbolic.HyperbolicRigidMotion;
import hyperbolic.HyperbolicRigidMotionGenerator;
import hyperbolic.SimpleHyperbolicLine;

import java.awt.Color;

import display.HyperbolicLineDrawing;
import display.HyperbolicLineSet;
import display.HyperbolicPicture;
import display.HyperbolicTessellation;
import display.SimpleHyperbolicPicture;
import display.UniformTessellation;

/**
 * @author Ned
 *
 */
public class HyperbolicView {
	

	private HyperbolicLineDrawing player = new HyperbolicLineDrawing(Color.blue);
	private HyperbolicLineDrawing trail = new HyperbolicLineDrawing(Color.red);
	private HyperbolicPicture picture = new SimpleHyperbolicPicture();
	
	private HyperbolicPointGenerator pointGenerator = new HalfPlanePointGenerator();
	private HyperbolicRigidMotionGenerator motionGenerator = new HalfPlaneMotionGenerator();
	
	private HyperbolicRigidMotion positionAndDirection = motionGenerator.identity();
	private HyperbolicRigidMotion turns = motionGenerator.identity();
	
	private double currentTurn = 0;
	private int direction = 0;
	private int counter = 0;
	private HyperbolicPoint[] playerDrawing = new HyperbolicPoint[] {
		pointGenerator.disk(0, 0.04),
		pointGenerator.disk(0.02, -0.02),
		pointGenerator.disk(-0.02, -0.02),
	};
	
	private HyperbolicTessellation tessellation = new UniformTessellation();
	private HyperbolicLineSet tessellationLines = tessellation.makeLineSet();
	
	public HyperbolicView() {
		HyperbolicLineDrawing tessellationDrawing = new HyperbolicLineDrawing(tessellationLines, Color.green);
		picture.addLineDrawing(tessellationDrawing);
		picture.addLineDrawing(trail);
		picture.addLineDrawing(player);
	}
	
	public void setTurn(double turn) {
		this.currentTurn = turn;
	}
	
	public void setDirection(int direction) {
		this.direction = direction;
	}
	
	public void update() {
		double turnToUse = currentTurn;
		HyperbolicRigidMotion turn = motionGenerator.rotateAboutDiskCenter(turnToUse);
		HyperbolicRigidMotion goForward = motionGenerator.scalePlane(1.0 + (double)direction * 0.03);
		
		HyperbolicPoint currentPosition = positionAndDirection.transform(pointGenerator.diskCenter());
		positionAndDirection = positionAndDirection.composeWith(goForward);
		positionAndDirection = positionAndDirection.composeWith(turn);
		HyperbolicPoint newPosition = positionAndDirection.transform(pointGenerator.diskCenter());
		drawPlayer();
		
		if (direction!=0)
			trail.getLineSet().addLine(new SimpleHyperbolicLine(currentPosition, newPosition));
		if (counter%50==0) {
			HyperbolicLineSet newLines = tessellation.makeLineSetNear(currentPosition);
			tessellationLines.setLines(newLines.getLines());
		}

		turns = turns.composeWith(turn);
		HyperbolicRigidMotion cameraAngle = positionAndDirection.composeWith(turns.inverse());
		
		picture.setRigidMotion(cameraAngle.inverse());
		counter++;
	}
	
	private void drawPlayer() {
		player.getLineSet().clearLines();
		int points = playerDrawing.length;
		for (int i=0; i<points-1; i++) {
			HyperbolicPoint p1 = positionAndDirection.transform(playerDrawing[i]);
			HyperbolicPoint p2 = positionAndDirection.transform(playerDrawing[i+1]);
			SimpleHyperbolicLine line = new SimpleHyperbolicLine(p1, p2);
			player.getLineSet().addLine(line);
		}
		HyperbolicPoint p1 = positionAndDirection.transform(playerDrawing[points-1]);
		HyperbolicPoint p2 = positionAndDirection.transform(playerDrawing[0]);
		SimpleHyperbolicLine line = new SimpleHyperbolicLine(p1, p2);
		player.getLineSet().addLine(line);
	}
	
	public HyperbolicPicture getPicture() {
		return picture;
	}
}
