/**
 * 
 */
package display;

import hyperbolic.HyperbolicLine;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Ned
 *
 */
public final class HashHyperbolicLineSet implements HyperbolicLineSet {
	
	private final Set<HyperbolicLine> lines = new HashSet<HyperbolicLine>();

	@Override
	public synchronized void addLine(HyperbolicLine line) {
		lines.add(line);
	}

	@Override
	public synchronized void clearLines() {
		lines.clear();
	}

	@Override
	public synchronized Set<HyperbolicLine> getLines() {
		return new HashSet<HyperbolicLine>(lines);
	}

	@Override
	public void setLines(Collection<HyperbolicLine> lines) {
		this.lines.clear();
		this.lines.addAll(lines);
	}

}
