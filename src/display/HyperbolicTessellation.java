/**
 * 
 */
package display;

import hyperbolic.HyperbolicPoint;

/**
 * @author Ned
 *
 */
public interface HyperbolicTessellation {

	HyperbolicLineSet makeLineSet();
	
	HyperbolicLineSet makeLineSetNear(HyperbolicPoint position);
	
}
