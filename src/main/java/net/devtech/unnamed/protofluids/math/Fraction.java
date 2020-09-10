package net.devtech.unnamed.protofluids.math;

import java.math.BigInteger;

/**
 * do <b>NOT</b> implement this interface!
 */
public interface Fraction extends Comparable<Fraction> {
	LongFraction MINUS_ONE = new LongFraction(-1, 1);
	LongFraction ZERO = new LongFraction(0, 1);
	LongFraction ONE_THIRD = new LongFraction(1, 3);
	LongFraction ONE_HALF = new LongFraction(1, 2);
	// half of 2
	LongFraction ONE = new LongFraction(1, 1);
	LongFraction TWO = new LongFraction(2, 1);



	static LongFraction of(long numerator, long denominator) {
		if(denominator == 0) throw new IllegalArgumentException("Denominator is 0!");
		if(numerator < 16 && denominator < 16) {
			return LongFraction.CACHE[(int) (numerator * 16 + denominator)];
		}
		return new LongFraction(numerator, denominator);
	}

	// would checking if it's prime speed it up?
	static Fraction of(BigInteger numerator, BigInteger denominator) {
		int num = numerator.signum();
		int den = denominator.signum();
		if (num == 0) { // if numerator is 0
			return ZERO;
		}

		if (den == 0) {
			throw new IllegalArgumentException("Denominator is 0!");
		}

		BigInteger gcd = numerator.gcd(denominator);
		numerator = numerator.divide(gcd);
		denominator = denominator.divide(gcd);
		if (numerator.bitCount() < 64 && denominator.bitCount() < 64) {
			return of(numerator.longValue(), denominator.longValue());
		} else {
			return new BigFraction(numerator, denominator);
		}
	}

	Fraction div(long val);

	Fraction mul(long val);

	Fraction add(Fraction amount);

	Fraction neg();

	Fraction inv();

	BigInteger getNumerator();

	BigInteger getDenominator();

	int signum();

	/**
	 * true if the actual value is equal
	 */
	@Override
	boolean equals(Object o);
}
