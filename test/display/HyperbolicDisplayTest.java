package display;

import gui.DisplayFrame;
import hyperbolic.HalfPlaneMotionGenerator;
import hyperbolic.HalfPlanePointGenerator;
import hyperbolic.HyperbolicPoint;
import hyperbolic.HyperbolicPointGenerator;
import hyperbolic.HyperbolicRigidMotion;
import hyperbolic.HyperbolicRigidMotionGenerator;
import hyperbolic.SimpleHyperbolicLine;

import java.awt.Color;

import org.junit.Test;

public class HyperbolicDisplayTest {
	
	private HyperbolicLineDrawing trail = new HyperbolicLineDrawing(Color.red);
	private HyperbolicPicture picture = new SimpleHyperbolicPicture();
	private HyperbolicPainter painter = new StraightLineDiscPainter(picture);
	
	private HyperbolicPointGenerator pointGenerator = new HalfPlanePointGenerator();
	private HyperbolicRigidMotionGenerator motionGenerator = new HalfPlaneMotionGenerator();
	
	private HyperbolicRigidMotion positionAndDirection = motionGenerator.identity();
	private HyperbolicRigidMotion turns = motionGenerator.identity();
	private double turnFactor = 0.001;
	private double currentTurn = 0;
	private double maxTurn = 0.01;
	
	@Test
	public void runTest() {
		DisplayFrame frame = new DisplayFrame(painter);
		int counter = 0;
		
		HyperbolicTessellation tessellation = new UniformTessellation();
		HyperbolicLineSet tessellationLines = tessellation.makeLineSet();
		HyperbolicLineDrawing tessellationDrawing = new HyperbolicLineDrawing(tessellationLines, Color.green);
		picture.addLineDrawing(tessellationDrawing);
		picture.addLineDrawing(trail);
		
		
		for (;;) {
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				System.out.println("I was interrupted!");
			}
			
			double angle = turnFactor*(Math.random() - 0.5);
			currentTurn += angle;
			if (currentTurn>maxTurn)  currentTurn = maxTurn;
			if (currentTurn<-maxTurn) currentTurn = -maxTurn;
			double turnToUse = currentTurn;
			HyperbolicRigidMotion turn = motionGenerator.rotateAboutDiskCenter(turnToUse);
			HyperbolicRigidMotion goForward = motionGenerator.scalePlane(0.99);
			
			HyperbolicPoint currentPosition = positionAndDirection.transform(pointGenerator.diskCenter());
			positionAndDirection = positionAndDirection.composeWith(goForward);
			positionAndDirection = positionAndDirection.composeWith(turn);
			HyperbolicPoint newPosition = positionAndDirection.transform(pointGenerator.diskCenter());
			
			trail.getLineSet().addLine(new SimpleHyperbolicLine(currentPosition, newPosition));
			if (counter%50==0) {
				HyperbolicLineSet newLines = tessellation.makeLineSetNear(currentPosition);
				tessellationLines.setLines(newLines.getLines());
			}

			turns = turns.composeWith(turn);
			HyperbolicRigidMotion cameraAngle = positionAndDirection.composeWith(turns.inverse());
			
			picture.setRigidMotion(cameraAngle.inverse());

			counter++;
			if (counter%1==0) {
				frame.repaint();
			}
		}
	}

}
