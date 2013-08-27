/**
 * 
 */
package display;

import hyperbolic.HalfPlaneMotion;
import hyperbolic.HyperbolicLine;
import hyperbolic.HyperbolicPoint;
import hyperbolic.HyperbolicRigidMotion;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Ned
 *
 */
public final class SimpleHyperbolicPicture implements HyperbolicPicture {
	
	private final List<HyperbolicLineDrawing> lineDrawings = new ArrayList<HyperbolicLineDrawing>();
	private final List<HyperbolicPolyDrawing> polyDrawings = new ArrayList<HyperbolicPolyDrawing>();
	private HyperbolicRigidMotion rigidMotion = new HalfPlaneMotion();

	@Override
	public synchronized void addLineDrawing(HyperbolicLineDrawing lineDrawing) {
		lineDrawings.add(lineDrawing);
	}

	@Override
	public synchronized void clearLineDrawings() {
		lineDrawings.clear();
	}

	@Override
	public synchronized void setRigidMotion(HyperbolicRigidMotion transform) {
		this.rigidMotion = transform;
	}

	@Override
	public synchronized Set<HyperbolicLineDrawing> getTransformedDrawings() {
		Set<HyperbolicLineDrawing> transformedDrawings = new HashSet<HyperbolicLineDrawing>();
		for (HyperbolicLineDrawing drawing : lineDrawings) {
			HyperbolicLineSet transformedLines = new ArrayHyperbolicLineSet();
			for (HyperbolicLine line : drawing.getLineSet().getLines()) {
				HyperbolicLine transformedLine = rigidMotion.transform(line);
				transformedLines.addLine(transformedLine);
			}
			transformedDrawings.add(new HyperbolicLineDrawing(transformedLines, drawing.getColor()));
		}
		return transformedDrawings;
	}

	@Override
	public void addPolyDrawing(HyperbolicPolyDrawing polyDrawing) {
		this.polyDrawings.add(polyDrawing);
	}

	@Override
	public void clearPolyDrawings() {
		this.polyDrawings.clear();
	}

	@Override
	public List<HyperbolicPolyDrawing> getTransformedPolys() {
		List<HyperbolicPolyDrawing> transformedDrawings = new ArrayList<HyperbolicPolyDrawing>();
		for (HyperbolicPolyDrawing drawing : polyDrawings) {
			HyperbolicPolyDrawing transformedDrawing = new HyperbolicPolyDrawing(drawing.getColor());
			for (HyperbolicPoly poly : drawing.getPolys()) {
				transformedDrawing.addPoly(transformPoly(poly));
			}
			transformedDrawings.add(transformedDrawing);
		}
		return transformedDrawings;
	}
	
	private HyperbolicPoly transformPoly(HyperbolicPoly poly) {
		HyperbolicPoint[] points = poly.points();
		HyperbolicPoint[] newPoints = new HyperbolicPoint[points.length]; 
		for (int i=0; i<points.length; i++) {
			newPoints[i] = this.rigidMotion.transform(points[i]);
		}
		return new HyperbolicPoly(newPoints);
	}

}
