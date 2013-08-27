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
import display.HyperbolicPoly;
import display.HyperbolicPolyDrawing;
import display.SimpleHyperbolicPicture;
import display.UniformTessellation;

/**
 * @author Ned
 *
 */
public class HyperbolicView {
	

	private HyperbolicPolyDrawing player = new HyperbolicPolyDrawing(Color.blue);
	private HyperbolicLineDrawing trail = new HyperbolicLineDrawing(Color.red);
	private HyperbolicPicture picture = new SimpleHyperbolicPicture();
	
	private HyperbolicPointGenerator pointGenerator = new HalfPlanePointGenerator();
	private HyperbolicRigidMotionGenerator motionGenerator = new HalfPlaneMotionGenerator();
	
	private HyperbolicRigidMotion positionAndDirection = motionGenerator.identity();
	private HyperbolicRigidMotion turns = motionGenerator.identity();
	private boolean gridMode=false;
	
	private double currentTurn = 0;
	private int direction = 0;
	private int counter = 0;
	private HyperbolicPoint[] playerDrawing = new HyperbolicPoint[] {
		pointGenerator.disk(0, 0.04),
		pointGenerator.disk(0.02, -0.02),
		pointGenerator.disk(-0.02, -0.02),
	};
	
	private UniformTessellation tessellation = new UniformTessellation();
	private HyperbolicLineSet tessellationLines = tessellation.makeLineSet();
	private HyperbolicPolyDrawing tessellationPolyDrawing;
	
	public HyperbolicView() {
		if (gridMode)
			picture.addDrawing(new HyperbolicLineDrawing(tessellationLines, Color.green));
		else {
			tessellationPolyDrawing = new HyperbolicPolyDrawing(new Color(0,100,0));
			picture.addDrawing(tessellationPolyDrawing);
		}
		picture.addDrawing(trail);
		picture.addDrawing(player);
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
