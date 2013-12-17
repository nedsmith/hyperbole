/**
 * 
 */
package random;

import java.util.concurrent.atomic.AtomicLong;


/**
 * Medium-quality fast random number generator.
 * 
 * Based on a XOR shift algorithm from Marsaglia, 2003.
 * 
 * @author Ned
 */
public class QuickRng implements RandomNumberGenerator {

    private final static AtomicLong initialSeed = new AtomicLong(7959127561643282L);
	
	private long seed;
	
    public QuickRng(long seed) {
    	this.seed = seed;
    }
    
    public QuickRng() {
    	this(initialSeed.incrementAndGet() + System.nanoTime());
    }

    
	@Override
	public double random() {
		seed ^= (seed << 6);
		seed ^= (seed >>> 21);
		seed ^= (seed << 7);
		return (seed > 0L ? seed : -seed) / (double) Long.MAX_VALUE;
	}

	@Override
	public int randomInt(int options) {
		if (options==0) return 0;
		seed ^= (seed << 6);
		seed ^= (seed >>> 21);
		seed ^= (seed << 7);
		return (int)((seed > 0L ? seed : -seed) % options);
	}
	
}