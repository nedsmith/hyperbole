/**
 * 
 */
package display;

import hyperbolic.HyperbolicLine;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Collection;

/**
 * @author Ned
 *
 */
public class StraightLineDiscPainter implements HyperbolicPainter {

	private HyperbolicPicture picture;
	private int radius = 400;
	private int offsetX = 0;
	private int offsetY = 0;
	
	public StraightLineDiscPainter(HyperbolicPicture picture) {
		this.picture = picture;
	}
	
	public StraightLineDiscPainter() {
		this(new SimpleHyperbolicPicture());
	}
	
	@Override
	public synchronized void paint(Graphics g) {
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, 2000, 1000);
		
		g.setColor(Color.BLACK);
		g.drawOval(offsetX, offsetY, radius*2, radius*2);
		
		for (HyperbolicDrawing drawing : picture.getTransformedDrawings()) {
			g.setColor(drawing.getColor());
			Collection<HyperbolicLine> lines = ((HyperbolicLineDrawing)drawing).getLineSet().getLines();
			for (HyperbolicLine line : lines) {
				double[] start = line.getStartPoint().getDiskPosition();
				double[] end = line.getEndPoint().getDiskPosition();
				int[] startPos = plot(start);
				int[] endPos = plot(end);
				g.drawLine(startPos[0], startPos[1], endPos[0], endPos[1]);
			}
		}
	}
	
	private int[] plot(double[] coords) {
		int[] position = new int[2];
		position[0] = offsetX + radius + (int)(coords[0] * (double) radius);
		position[1] = offsetY + radius + (int)(coords[1] * (double) radius);
		return position;
	}

	@Override
	public synchronized void setPicture(HyperbolicPicture picture) {
		this.picture = picture;
	}

}
