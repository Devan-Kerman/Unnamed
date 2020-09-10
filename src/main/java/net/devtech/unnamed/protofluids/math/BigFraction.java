package net.devtech.unnamed.protofluids.math;

import java.math.BigInteger;

import org.jetbrains.annotations.NotNull;

public class BigFraction implements Fraction {
	private final BigInteger numerator, denominator;
	private int hash;

	BigFraction(BigInteger numerator, BigInteger denominator) {
		this.numerator = numerator;
		this.denominator = denominator;
	}

	@Override
	public int compareTo(@NotNull Fraction o) {
		if(o instanceof LongFraction) return this.signum();

		return this.numerator.multiply(o.getDenominator())
		                     .subtract(o.getNumerator()
		                                .multiply(this.denominator))
		                     .signum();
	}

	@Override
	public Fraction div(long val) {
		return Fraction.of(this.numerator, this.denominator.multiply(BigInteger.valueOf(val)));
	}

	@Override
	public Fraction mul(long val) {
		return Fraction.of(this.numerator.multiply(BigInteger.valueOf(val)), this.denominator);
	}

	@Override
	public Fraction add(Fraction amount) {
		return Fraction.of(this.numerator.multiply(amount.getDenominator()
		                                                 .add(amount.getNumerator()
		                                                            .multiply(this.denominator))), this.denominator.multiply(amount.getDenominator()));
	}

	@Override
	public Fraction neg() {
		return new BigFraction(this.numerator.negate(), this.denominator);
	}

	@Override
	public Fraction inv() {
		return new BigFraction(this.denominator, this.numerator);
	}

	@Override
	public BigInteger getNumerator() {
		return this.numerator;
	}

	@Override
	public BigInteger getDenominator() {
		return this.denominator;
	}

	@Override
	public int signum() {
		return this.denominator.signum() * this.numerator.signum();
	}

	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}
		if (!(object instanceof BigFraction)) {
			return false;
		}

		BigFraction fraction = (BigFraction) object;
		return fraction.compareTo(this) == 0;
	}

	@Override
	public int hashCode() {
		int hash = this.hash;
		if(hash == -1) {
			int result = this.numerator != null ? this.numerator.hashCode() : 0;
			result = 31 * result + (this.denominator != null ? this.denominator.hashCode() : 0);
			if(result == -1) result++;
			hash = this.hash = result;
		}
		return hash;
	}
}
