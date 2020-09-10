package net.devtech.unnamed.protofluids.math;

import static java.lang.Math.min;

import java.math.BigInteger;

import com.google.common.math.LongMath;
import it.unimi.dsi.fastutil.HashCommon;
import org.jetbrains.annotations.NotNull;

// todo test compareTo
// todo fix all multiplication checks
public final class LongFraction implements Fraction {
	static final LongFraction[] CACHE = new LongFraction[256];

	static {
		for (int i = 0; i < CACHE.length; i++) {
			CACHE[i] = Fraction.of(i / 16, i % 16);
		}
	}

	public final long numerator, denominator;
	private final int hashCode;

	LongFraction(long numerator, long denominator) {
		if(denominator < 0) {
			numerator *= Long.signum(denominator);
		}

		this.numerator = numerator;
		this.denominator = denominator;
		// bad hash? I took this from some website, apparently it's good?
		this.hashCode = HashCommon.long2int((numerator / denominator + denominator / numerator));
	}

	@Override
	public Fraction add(Fraction amount) {
		if (amount instanceof LongFraction) {
			LongFraction fraction = (LongFraction) amount;

			// naive method
			if (canMultiply(fraction.denominator, this.denominator) && canMultiply(fraction.numerator, this.denominator) && canMultiply(fraction.denominator, this.numerator)) {
				long div = (fraction.denominator * this.denominator);
				long num = fraction.numerator * this.denominator, den = fraction.denominator * this.numerator;
				// check if we lost precision
				long sum = num + den;
				if (((num ^ sum) & (den ^ sum)) < 0) {
					return Fraction.of(num + den, div);
				}
			}

			// lcm, I think this is correct
			long gcd = LongMath.gcd(fraction.denominator, this.denominator);
			long left = (fraction.denominator / gcd), right = (this.denominator / gcd);
			if(canMultiply(left, right) && canMultiply(fraction.numerator, right) && canMultiply(this.numerator, left)) {
				return Fraction.of(fraction.numerator * right + this.numerator * left,left * right);
			}
		}

		return Fraction.of(BigInteger.valueOf(this.numerator)
		                             .multiply(amount.getDenominator())
		                             .add(BigInteger.valueOf(this.denominator)
		                                            .multiply(amount.getNumerator())),
		                   BigInteger.valueOf(this.denominator)
		                             .multiply(amount.getDenominator()));
	}

	@Override
	public Fraction neg() {
		return new LongFraction(-this.numerator, this.denominator);
	}

	@Override
	public Fraction inv() {
		return new LongFraction(this.denominator, this.numerator);
	}

	@Override
	public Fraction div(long val) {
		if (this.numerator % val == 0) { // if we can get away with just dividing the numerator
			return new LongFraction(this.numerator / val, this.denominator);
		} else if (canMultiply(this.denominator, val)) { // if we can get away with just increasing the denominator
			return new LongFraction(this.numerator, this.denominator * val);
		} else { // gcd time
			long gcd = gcd(this.numerator, this.denominator);
			long numerator = this.numerator / gcd, denominator = this.denominator / gcd;
			if (canMultiply(denominator, val)) {
				return new LongFraction(numerator, denominator * val);
			} else { // big fraction activated
				return new BigFraction(BigInteger.valueOf(numerator),
				                       BigInteger.valueOf(denominator)
				                                 .multiply(BigInteger.valueOf(val)));
			}
		}
	}

	@Override
	public int compareTo(@NotNull Fraction o) {
		if (o instanceof BigFraction) {
			return o.signum();
		} else if (o instanceof LongFraction) {
			LongFraction sec = (LongFraction) o;
			long num = sec.numerator * this.denominator, den = sec.denominator * this.numerator;
			// check if we lost precision
			long val = den - num;
			if (canMultiply(sec.numerator, this.denominator) && canMultiply(sec.denominator, this.numerator) && (((den ^ num) & (den ^ val)) < 0)) {
				return Long.signum(val);
			}
		}
		BigInteger num = o.getNumerator()
		                  .multiply(BigInteger.valueOf(this.denominator)), den = o.getDenominator()
		                                                                          .multiply(BigInteger.valueOf(this.numerator));
		return (den.subtract(num)).signum();
	}

	@Override
	public Fraction mul(long val) {
		if (this.denominator % val == 0) { // if we can get away with just dividing the denominator
			return new LongFraction(this.numerator, this.denominator / val);
		} else if (canMultiply(this.numerator, val)) { // if we can get away with just increasing the numerator
			return new LongFraction(this.numerator * val, this.denominator);
		} else { // gcd time
			long gcd = gcd(this.numerator, this.denominator);
			long numerator = this.numerator / gcd, denominator = this.denominator / gcd;
			if (canMultiply(denominator, val)) {
				return new LongFraction(numerator * val, denominator);
			} else { // big fraction activated
				return new BigFraction(BigInteger.valueOf(numerator),
				                       BigInteger.valueOf(denominator)
				                                 .multiply(BigInteger.valueOf(val)));
			}
		}
	}

	@Override
	public BigInteger getNumerator() {
		return BigInteger.valueOf(this.numerator);
	}

	@Override
	public BigInteger getDenominator() {
		return BigInteger.valueOf(this.denominator);
	}

	@Override
	public int signum() {
		return Long.signum(this.numerator * this.denominator);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof LongFraction)) {
			return false;
		}

		LongFraction fraction = (LongFraction) o;
		return this.compareTo(fraction) == 0;
	}

	@Override
	public int hashCode() {
		return this.hashCode;
	}

	/**
	 * stolen from {@link Math#multiplyExact(long, long)}
	 */
	public static boolean canMultiply(long a, long b) {
		long r = a * b;
		long ax = Math.abs(a);
		long ay = Math.abs(b);
		return (ax | ay) >>> 31 == 0 || ((b == 0) || (r / b == a)) && (a != Long.MIN_VALUE || b != -1);
	}

	/**
	 * @see LongMath#gcd(long, long)
	 * @param b positive
	 */
	public static long gcd(long a, long b) {
		if(b < 0) throw new IllegalArgumentException("b must be positive!");

		int sign = Long.signum(a);
		if (a == 0) {
			return b;
		} else if (b == 0) {
			return a;
		}

		int aTwos = Long.numberOfTrailingZeros(a);
		a >>= aTwos;
		int bTwos = Long.numberOfTrailingZeros(b);
		b >>= bTwos;
		while (a != b) {
			long delta = a - b;
			long minDeltaOrZero = delta & (delta >> (Long.SIZE - 1));
			a = delta - minDeltaOrZero - minDeltaOrZero;
			b += minDeltaOrZero;
			a >>= Long.numberOfTrailingZeros(a);
		}
		return (a << min(aTwos, bTwos)) * sign;
	}
}
