/**
 * 
 */
package random;


import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * @author Ned
 *
 */
public class RandomNumberGeneratorTest {
	
	@Test
	public void testDefaultRng() {
		RandomNumberGenerator rng = RandomNumberGeneratorFactory.makeDefaultRng();
		testRng(rng);
	}
	
	private void testRng(RandomNumberGenerator rng) {
		testDoubles(rng);
		testInts(rng);
	}
		
	/**
	 * Simple statistical test of a RandomNumberGenerator.
	 */
	private void testDoubles(RandomNumberGenerator rng) {
		int repeats = 1000;
		int partitions = 10;
		int[] occurrences = new int[partitions];
		
		long startTime = System.nanoTime();
		for (int i=0; i<repeats; i++) {
			double randomDouble = rng.random();
			int randomInt = (int)(randomDouble * (double)partitions);
			occurrences[randomInt]++;
		}
		long endTime = System.nanoTime();
		System.out.println("Time for "+rng+" doubles: "+(endTime-startTime)+" nanoseconds");
		
		for (int i=0; i<partitions; i++) {
			int expected = repeats / partitions;
			int actual = occurrences[i];
			double minimumExpected = (double)expected * 0.5;
			assertTrue("Should have been at least "+minimumExpected+" results in partition "+i+", were "+actual,
					actual > minimumExpected);
		}

	}
	
	/**
	 * Simple statistical test of a RandomNumberGenerator.
	 */
	private void testInts(RandomNumberGenerator rng) {
		int repeats = 1000;
		int options = 10;
		int[] occurrences = new int[options];
		
		long startTime = System.nanoTime();
		for (int i=0; i<repeats; i++) {
			int randomInt = rng.randomInt(options);
			occurrences[randomInt]++;
		}
		long endTime = System.nanoTime();
		System.out.println("Time for "+rng+" ints: "+(endTime-startTime)+" nanoseconds");
		
		for (int i=0; i<options; i++) {
			int expected = repeats / options;
			int actual = occurrences[i];
			double minimumExpected = (double)expected * 0.5;
			assertTrue("Should have been at least "+minimumExpected+" results for option "+i+", were "+actual,
					actual > minimumExpected);
		}

	}

}
