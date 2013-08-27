/**
 * 
 */
package display;

import hyperbolic.HyperbolicRigidMotion;

import java.util.List;

/**
 * @author Ned
 *
 */
public interface HyperbolicPicture {
	
	void addDrawing(HyperbolicDrawing lineDrawing);

	void clearDrawings();
	
	void setRigidMotion(HyperbolicRigidMotion rigidMotion);
	
	List<HyperbolicDrawing> getTransformedDrawings();

}
