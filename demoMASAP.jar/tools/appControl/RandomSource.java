package tools.appControl;

import java.util.Random;

public class RandomSource {

	public final static Random	randomizer	= new Random();

	public static double randomDoubleBetween0And(final int max) {
		final double floatPart = RandomSource.randomizer.nextDouble();
		final int intPart = RandomSource.randomizer.nextInt(max);
		return intPart + floatPart;
	}

	public static double randomGaussianDoubleBetween(
			final double minValue,
			final double maxValue) {
		final double halfIntervalSize = (maxValue - minValue) / 2;
		final double intervalMiddle = halfIntervalSize + minValue;
		double result;
		do {
			result = RandomSource.randomizer.nextGaussian() * halfIntervalSize
					+ intervalMiddle;
		} while ((result < minValue) || (result > maxValue));
		return result;
	}

	public static int randomIntBetween0And(final int max) {
		return RandomSource.randomizer.nextInt(max + 1);
	}

	public static int randomPlusOrMinus() {
		return (RandomSource.randomIntBetween0And(1) == 1) ? 1 : -1;
	}

	public static double randomUpperHalfGaussianDoubleBetween(
			final double minValue,
			final double maxValue) {
		final double intervalSize = maxValue - minValue;
		double rand;
		do {
			rand = RandomSource.randomizer.nextGaussian();

		} while ((rand < 0) || (rand > 1));
		return rand * intervalSize + minValue;

	}
}
