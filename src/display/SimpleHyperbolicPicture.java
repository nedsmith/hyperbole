/**
 * 
 */
package display;

import hyperbolic.HalfPlaneMotion;
import hyperbolic.HyperbolicLine;
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

}
