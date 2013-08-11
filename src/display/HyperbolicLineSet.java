/**
 * 
 */
package display;

import hyperbolic.HyperbolicLine;

import java.util.Collection;

/**
 * @author Ned
 *
 */
public interface HyperbolicLineSet {
	
	void addLine(HyperbolicLine line);

	void clearLines();
	
	Collection<HyperbolicLine> getLines();
	
	void setLines(Collection<HyperbolicLine> lines);

}
