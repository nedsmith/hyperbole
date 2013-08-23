/**
 * 
 */
package display;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author Ned
 *
 */
public final class HyperbolicPolyDrawing {
	
	private final List<HyperbolicPoly> polys = new ArrayList<HyperbolicPoly>();
	private Color color;
	
	public HyperbolicPolyDrawing(Color color) {
		this.color = color;
	}
	
	public void addPoly(HyperbolicPoly poly) {
		this.polys.add(poly);
	}
	
	public void clearPolys() {
		this.polys.clear();
	}
	
	public Collection<HyperbolicPoly> getPolys() {
		return this.polys;
	}
	
	public void setPolys(Collection<HyperbolicPoly> polys) {
		this.polys.clear();
		this.polys.addAll(polys);
	}
	
	public Color color() {
		return this.color;
	}

}
