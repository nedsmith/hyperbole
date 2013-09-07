/**
 * 
 */
package hyperbolic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Ned
 *
 */
public class HalfPlaneCollisionDetector<E> implements HyperbolicCollisionDetector<E> {

	private final double collisionDistance;
	private final double boxSize;
	private static final double LOG_2 = Math.log(2);
	private final Map<Box, List<Element>> map = new HashMap<Box, List<Element>>();
	private final Map<E, Element> index = new HashMap<E, Element>();
	
	public HalfPlaneCollisionDetector() {
		this(0.05);
	}
	
	public HalfPlaneCollisionDetector(double collisionDistance) {
		this.collisionDistance = collisionDistance;
		this.boxSize = collisionDistance*2.0;
	}
	
	private static class Box {

		long x;
		long y;

		Box() {}
		Box(long x, long y) {this.x=x; this.y=y;}
		
		@Override
		public boolean equals(Object obj) {
			if (obj==null) return false;
			if (obj==this) return true;
			if (!(obj instanceof Box)) return false;
			Box other = (Box)obj;
			return x==other.x && y==other.y;
		}
		
		@Override
		public int hashCode() {
			return 37^(int)x + 23^(int)y;
		}
	}
	
	private class Element {
		E object;
		HyperbolicPoint position;
		Box box;
	}
	
	private Box getBox(HyperbolicPoint p) {
		double[] pos = p.getHalfPlanePosition();
		double x = pos[0];
		double y = pos[1];
		
		Box box = new Box();
		box.y = (long)(Math.log(y/boxSize)/LOG_2);
		double scale = Math.pow(2, box.y);
		box.x = (long)((x / boxSize)/ scale);
		return box;
	}
	
	private List<Element> getElements(Box box) {
		List<Element> list = map.get(box);
		if (list==null) {
			list = new ArrayList<Element>();
			map.put(box, list);
		}
		return list;
	}
	
	@Override
	public void add(E e, HyperbolicPoint point) {
		Element element = new Element();
		index.put(e, element);
		element.object = e;
		element.position = point;
		Box box = getBox(point);
		element.box = box;
		getElements(box).add(element);
	}

	@Override
	public void remove(E e) {
		Element elt = index.remove(e);
		List<Element> list = getElements(elt.box);
		list.remove(elt);
		if (list.isEmpty()) {
			map.remove(elt.box);
		}
	}
	
	private List<Box> getNeighbours(Box box) {
		List<Box> list = new ArrayList<Box>();
		list.add(new Box(box.x/2, box.y-1));
		list.add(new Box(box.x-1, box.y));
		list.add(new Box(box.x+1, box.y));
		list.add(new Box(box.x*2-1, box.y+1));
		list.add(new Box(box.x*2, box.y+1));
		list.add(new Box(box.x*2+1, box.y+1));
		list.add(new Box(box.x*2+2, box.y+1));
		return list;
	}
	
	private void addCollisionsFrom(Box box, HyperbolicPoint p, List<E> collisions) {
		List<Element> contents = map.get(box);
		if (contents==null) return;
		for (Element elt : contents) {
			if (p.distanceFrom(elt.position) < collisionDistance)
				collisions.add(elt.object);
		}
	}

	@Override
	public List<E> detectCollisions(HyperbolicPoint point) {
		List<E> collisions = new ArrayList<E>();
		Box box = getBox(point);
		addCollisionsFrom(box, point, collisions);
		List<Box> neighbours = getNeighbours(box);
		for (Box neighbour : neighbours) {
			addCollisionsFrom(neighbour, point, collisions);
		}
		return collisions;
	}

}
