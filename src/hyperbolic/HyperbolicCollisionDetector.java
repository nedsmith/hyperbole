/**
 * 
 */
package hyperbolic;

import java.util.List;

/**
 * Collision detector for use on the complex plane.
 * 
 * An implementation of this class must allow objects of type E to be
 * associated with points on the hyperbolic plane, and then allow all
 * objects close to a given point to be retrieved.
 * 
 * @author Ned
 */
public interface HyperbolicCollisionDetector<E> {

	void add(E e, HyperbolicPoint point);
	
	void remove(E e);
	
	List<E> detectCollisions(HyperbolicPoint point);
	
	List<HyperbolicPoint> points();
}
