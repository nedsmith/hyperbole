/**
 * 
 */
package display;

import hyperbolic.HyperbolicPoint;

import java.util.List;

/**
 * @author Ned
 *
 */
public interface HyperbolicPolyTessellation {
	
	List<HyperbolicPoly> makePolys();
	
	List<HyperbolicPoly> makePolysNear(HyperbolicPoint point);

}
