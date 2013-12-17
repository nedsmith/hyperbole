/**
 * 
 */
package random;

/**
 * Interface for a random number generator.
 * 
 * Implementations of this interface are <b>not</b> thread-safe in general.
 * 
 * Also, no guarantee is made by this interface about the quality of the random numbers generated
 * by implementing classes, merely that they should have approximately the right statistical 
 * properties and generally be different between runs.
 * 
 * @author Ned
 */
public interface RandomNumberGenerator {

	/**
	 * Get a random double.
	 * @return A random <b>double</b> approximately uniformly distributed
	 * on the interval from 0.0 to 1.0.
	 */
	double random();
	
	/**
	 * Convenience method to get a random integer in a range.
	 * @param options The number of possible values to return.
	 * @return A random integer in the range from 0, inclusive, to
	 * <i>options</i>, exclusive, with all possible values equally
	 * likely.
	 */
	int randomInt(int options);
	
}
