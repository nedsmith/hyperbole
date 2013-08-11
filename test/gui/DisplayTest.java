package gui;

import java.awt.Color;
import java.awt.Graphics;

import org.junit.Test;

import display.Painter;

public class DisplayTest {
	
	private static class MyPainter implements Painter {

		private int size = 10;
		private int diff = 10;
		
		@Override
		public void paint(Graphics g) {
			g.setColor(Color.WHITE);
			g.fillRect(0, 0, 2000, 1000);
			g.setColor(Color.GREEN);
			g.fillOval(400, 300, size, size);
			size += diff;
			if (size>100 || size<20) diff *= -1;
		}
	}
	
	
	@Test
	public void testDisplay() {
		
		Painter painter = new MyPainter();
		DisplayFrame frame = new DisplayFrame(painter);
		
		for (;;) {
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				System.out.println("I was interrupted!");
			}
			frame.repaint();
		}
	}
	

}
