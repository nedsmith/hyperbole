/**
 * 
 */
package display;

import hyperbolic.HyperbolicRigidMotion;

import java.util.Set;

/**
 * @author Ned
 *
 */
public interface HyperbolicPicture {
	
	void addLineDrawing(HyperbolicLineDrawing lineDrawing);

	void clearLineDrawings();
	
	void setRigidMotion(HyperbolicRigidMotion rigidMotion);
	
	Set<HyperbolicLineDrawing> getTransformedDrawings();

}
