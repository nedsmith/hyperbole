package game;

import gui.HyperbolicDiscPainter;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

/**
 * Main class for the Hyperbolic Game
 * 
 * @author Ned
 */
public class HyperbolicGame {

	private HyperbolicDiscPainter painter = new HyperbolicDiscPainter();
	private HyperbolicView view = new HyperbolicView();
	
	public void run() {
		int counter = 0;
		painter.setPicture(view.getPicture());
		
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
			view.update();

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
	
	private static final double TURN_FACTOR = 0.2;
	
	private void processInput() {
		while (Keyboard.next()) {
			int key = Keyboard.getEventKey();
			boolean pressed = Keyboard.getEventKeyState();
			if (pressed) {
				if (key==Keyboard.KEY_A) view.setTurn(TURN_FACTOR);
				if (key==Keyboard.KEY_D) view.setTurn(-TURN_FACTOR);
				if (key==Keyboard.KEY_W) view.setDirection(1);
				if (key==Keyboard.KEY_S) view.setDirection(-1);
				if (key==Keyboard.KEY_SPACE) view.setTrailOn(true);
			}
			else {
				if (key==Keyboard.KEY_A || key==Keyboard.KEY_D) view.setTurn(0);
				if (key==Keyboard.KEY_W || key==Keyboard.KEY_S) view.setDirection(0);
				if (key==Keyboard.KEY_SPACE) view.setTrailOn(false);
			}
		}
	}
	
	public static void main(String[] args) {
		HyperbolicGame game = new HyperbolicGame();
		game.run();
	}

}
