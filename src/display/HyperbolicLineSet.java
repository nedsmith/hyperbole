/**
 * 
 */
package display;

import java.util.Set;

import hyperbolic.HyperbolicLine;

/**
 * @author Ned
 *
 */
public interface HyperbolicLineSet {
	
	void addLine(HyperbolicLine line);

	void clearLines();
	
	Set<HyperbolicLine> getLines();

}
