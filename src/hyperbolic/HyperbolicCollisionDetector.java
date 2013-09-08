/**
 * 
 */
package hyperbolic;

import java.util.List;

/**
 * @author Ned
 *
 */
public interface HyperbolicCollisionDetector<E> {

	void add(E e, HyperbolicPoint point);
	
	void remove(E e);
	
	List<E> detectCollisions(HyperbolicPoint point);
	
	List<HyperbolicPoint> points();
}
