/**
 * 
 */
package display;

import hyperbolic.HyperbolicLine;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Ned
 *
 */
public final class ArrayHyperbolicLineSet implements HyperbolicLineSet {
	
	private final List<HyperbolicLine> lines = new ArrayList<HyperbolicLine>();

	@Override
	public void addLine(HyperbolicLine line) {
		lines.add(line);
	}

	@Override
	public void clearLines() {
		lines.clear();
	}

	@Override
	public Set<HyperbolicLine> getLines() {
		return new HashSet<HyperbolicLine>(lines);
	}

	@Override
	public void setLines(Collection<HyperbolicLine> lines) {
		this.lines.clear();
		this.lines.addAll(lines);
	}

}
