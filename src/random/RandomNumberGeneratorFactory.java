/**
 * 
 */
package random;

/**
 * @author Ned
 *
 */
public class RandomNumberGeneratorFactory {

	// prevent instantiation
	private RandomNumberGeneratorFactory() {}
		
	/**
	 * @return The system default implementation of <code>RandomNumberGenerator</code>.
	 */
	public static RandomNumberGenerator makeDefaultRng() {
		return new QuickRng();
	}
	
}
