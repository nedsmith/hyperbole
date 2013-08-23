/**
 * 
 */
package gui;

import hyperbolic.HyperbolicLine;
import hyperbolic.HyperbolicPoint;

import java.awt.Color;
import java.util.Collection;

import org.lwjgl.opengl.GL11;

import display.HyperbolicLineDrawing;
import display.HyperbolicPicture;
import display.HyperbolicPoly;
import display.HyperbolicPolyDrawing;


/**
 * @author Ned
 *
 */
public class HyperbolicDiscPainter implements GlPainter {

	private HyperbolicPicture picture;
	private int radius = 400;
	private int offsetX = 0;
	private int offsetY = 0;
		
	@Override
	public void initialise() {
		GL11.glMatrixMode(GL11.GL_PROJECTION_MATRIX);
		GL11.glLoadIdentity();
		GL11.glOrtho(0, 800, 0, 800, 1, -1);
	}

	@Override
	public void update() {
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		for (HyperbolicLineDrawing drawing : picture.getTransformedDrawings()) {
			paintLineDrawing(drawing);
		}
		for (HyperbolicPolyDrawing drawing : picture.getTransformedPolys()) {
			paintPolyDrawing(drawing);
		}
	}
	
	private void paintLineDrawing(HyperbolicLineDrawing drawing) {
		setColor(drawing.getColor());
		GL11.glBegin(GL11.GL_LINES);
		Collection<HyperbolicLine> lines = drawing.getLineSet().getLines();
		for (HyperbolicLine line : lines) {
			double[] start = line.getStartPoint().getDiskPosition();
			double[] end = line.getEndPoint().getDiskPosition();
			int[] startPos = plot(start);
			int[] endPos = plot(end);
			drawLine(startPos, endPos);
		}
		GL11.glEnd();
	}
	
	private void paintPolyDrawing(HyperbolicPolyDrawing drawing) {
		setColor(drawing.color());
		for (HyperbolicPoly poly : drawing.getPolys()) {
			drawPoly(poly);
		}
	}
	
	private void drawPoly(HyperbolicPoly poly) {
		GL11.glBegin(GL11.GL_POLYGON);
		for (HyperbolicPoint point : poly.points()) {
			double[] coords = point.getDiskPosition();
			int[] pos = plot(coords);
			GL11.glVertex2d(pos[0], pos[1]);
		}
		GL11.glEnd();
	}
	
	private void setColor(Color color) {
		double r = (double)color.getRed()/255.0;
		double g = (double)color.getGreen()/255.0;
		double b = (double)color.getBlue()/255.0;
		GL11.glColor3d(r,  g, b);
	}
	
	private void drawLine(int[] start, int[] end) {
		GL11.glVertex2d(start[0], start[1]);
		GL11.glVertex2d(end[0], end[1]);
	}
	
	public void setPicture(HyperbolicPicture picture) {
		this.picture = picture;
	}
	
	private int[] plot(double[] coords) {
		int[] position = new int[2];
		position[0] = offsetX + radius + (int)(coords[0] * (double) radius);
		position[1] = offsetY + radius + (int)(coords[1] * (double) radius);
		return position;
	}

}
