package game;

import gui.HyperbolicDiscPainter;
import hyperbolic.HalfPlaneMotionGenerator;
import hyperbolic.HalfPlanePointGenerator;
import hyperbolic.HyperbolicPoint;
import hyperbolic.HyperbolicPointGenerator;
import hyperbolic.HyperbolicRigidMotion;
import hyperbolic.HyperbolicRigidMotionGenerator;
import hyperbolic.SimpleHyperbolicLine;

import java.awt.Color;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import display.HyperbolicLineDrawing;
import display.HyperbolicLineSet;
import display.HyperbolicPicture;
import display.HyperbolicTessellation;
import display.SimpleHyperbolicPicture;
import display.UniformTessellation;

public class HyperbolicGame {
	
	private HyperbolicLineDrawing trail = new HyperbolicLineDrawing(Color.red);
	private HyperbolicPicture picture = new SimpleHyperbolicPicture();
	private HyperbolicDiscPainter painter = new HyperbolicDiscPainter();
	
	private HyperbolicPointGenerator pointGenerator = new HalfPlanePointGenerator();
	private HyperbolicRigidMotionGenerator motionGenerator = new HalfPlaneMotionGenerator();
	
	private HyperbolicRigidMotion positionAndDirection = motionGenerator.identity();
	private HyperbolicRigidMotion turns = motionGenerator.identity();
	private double currentTurn = 0;
	private double maxTurn = 0.05;
	private int direction = 0;
	
	public void run() {
		int counter = 0;
		
		HyperbolicTessellation tessellation = new UniformTessellation();
		HyperbolicLineSet tessellationLines = tessellation.makeLineSet();
		HyperbolicLineDrawing tessellationDrawing = new HyperbolicLineDrawing(tessellationLines, Color.green);
		picture.addLineDrawing(tessellationDrawing);
		picture.addLineDrawing(trail);
		painter.setPicture(picture);
		
		try {
			Display.setDisplayMode(new DisplayMode(800,800));
			Display.setTitle("Hyperbolic");
			Display.create();
			painter.initialise();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		for (;;) {
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				System.out.println("I was interrupted!");
			}
			
			processInput();
			double turnToUse = currentTurn;
			HyperbolicRigidMotion turn = motionGenerator.rotateAboutDiskCenter(turnToUse);
			HyperbolicRigidMotion goForward = motionGenerator.scalePlane(1.0 + (double)direction * 0.03);
			
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
				painter.update();
				Display.update();
			}
			if (Display.isCloseRequested()) {
				Display.destroy();
				return;
			}
		}
	}
	
	private void processInput() {
		while (Keyboard.next()) {
			int key = Keyboard.getEventKey();
			boolean pressed = Keyboard.getEventKeyState();
			if (pressed) {
				if (key==Keyboard.KEY_A) currentTurn = maxTurn;
				if (key==Keyboard.KEY_D) currentTurn = -maxTurn;
				if (key==Keyboard.KEY_W) direction = 1;
				if (key==Keyboard.KEY_S) direction = -1;
			}
			else {
				if (key==Keyboard.KEY_A || key==Keyboard.KEY_D) currentTurn = 0;
				if (key==Keyboard.KEY_W || key==Keyboard.KEY_S) direction = 0;
			}
		}
	}
	
	public static void main(String[] args) {
		HyperbolicGame game = new HyperbolicGame();
		game.run();
	}

}
