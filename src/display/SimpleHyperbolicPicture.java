/**
 * 
 */
package display;

import hyperbolic.HalfPlaneMotion;
import hyperbolic.HyperbolicLine;
import hyperbolic.HyperbolicPoint;
import hyperbolic.HyperbolicRigidMotion;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ned
 *
 */
public final class SimpleHyperbolicPicture implements HyperbolicPicture {
	
	private final List<HyperbolicDrawing> lineDrawings = new ArrayList<HyperbolicDrawing>();
	private HyperbolicRigidMotion rigidMotion = new HalfPlaneMotion();

	@Override
	public synchronized void addDrawing(HyperbolicDrawing drawing) {
		lineDrawings.add(drawing);
	}

	@Override
	public synchronized void clearDrawings() {
		lineDrawings.clear();
	}

	@Override
	public synchronized void setRigidMotion(HyperbolicRigidMotion transform) {
		this.rigidMotion = transform;
	}

	@Override
	public synchronized List<HyperbolicDrawing> getTransformedDrawings() {
		List<HyperbolicDrawing> transformedDrawings = new ArrayList<HyperbolicDrawing>();
		for (HyperbolicDrawing drawing : lineDrawings) {
			if (drawing instanceof HyperbolicLineDrawing) {
				transformedDrawings.add(transformLineDrawing((HyperbolicLineDrawing)drawing));
			}
			else if (drawing instanceof HyperbolicPolyDrawing) {
				transformedDrawings.add(transformPolyDrawing((HyperbolicPolyDrawing)drawing));
			}
		}
		return transformedDrawings;
	}	
	
	private HyperbolicLineDrawing transformLineDrawing(HyperbolicLineDrawing lineDrawing) {
		HyperbolicLineSet transformedLines = new ArrayHyperbolicLineSet();
		for (HyperbolicLine line : lineDrawing.getLineSet().getLines()) {
			HyperbolicLine transformedLine = rigidMotion.transform(line);
			transformedLines.addLine(transformedLine);
		}
		return new HyperbolicLineDrawing(transformedLines, lineDrawing.getColor());
	}
	
	private HyperbolicPolyDrawing transformPolyDrawing(HyperbolicPolyDrawing polyDrawing) {
		HyperbolicPolyDrawing transformedDrawing = new HyperbolicPolyDrawing(polyDrawing.getColor());
		for (HyperbolicPoly poly : polyDrawing.getPolys()) {
			transformedDrawing.addPoly(transformPoly(poly));
		}
		return transformedDrawing;
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
