package ru.spbau.mit.circuit.logic.math.algebra;


import android.support.annotation.NonNull;

import java.math.BigDecimal;
import java.math.RoundingMode;

import ru.spbau.mit.circuit.logic.math.algebra.exceptions.IllegalInverseException;
import ru.spbau.mit.circuit.logic.math.algebra.interfaces.Field;

@SuppressWarnings("WeakerAccess")
public class Numerical implements Field<Numerical>, Comparable<Numerical> {

    private static final Numerical zero = Numerical.number(0);

    private static final Numerical identity = Numerical.number(1);
    @SuppressWarnings("FieldCanBeLocal")
    private static int roundingScale = 14;

    public static double round(double value) {
        value = new BigDecimal(value)
                .setScale(roundingScale, RoundingMode.HALF_UP).doubleValue();
        return value;
    }

    private final double value;

    @SuppressWarnings("FieldCanBeLocal")
    private final double precision = 0.000001;

    public static Numerical number(double value) {
        return new Numerical(value);
    }

    @NonNull
    public static Numerical zero() {
        return zero;
    }

    @NonNull
    public static Numerical identity() {
        return identity;
    }

    private Numerical(double value) {
        this.value = round(value);
    }

    public double value() {
        return value;
    }

    @NonNull
    @Override
    public Numerical add(@NonNull Numerical f) {
        return new Numerical(value + f.value);
    }

    @NonNull
    @Override
    public Numerical multiply(@NonNull Numerical numerical) {
        return new Numerical(value * numerical.value);
    }

    @NonNull
    @Override
    public Numerical multiplyConstant(@NonNull Numerical numerical) {
        return multiply(numerical);
    }

    @NonNull
    @Override
    public Numerical reciprocal() {
        if (!isZero()) {
            return new Numerical(1 / value);
        }
        throw new IllegalInverseException();
    }

    @NonNull
    @Override
    public Numerical negate() {
        return new Numerical(-value);
    }

    @Override
    public boolean isZero() {
        return isEquals(zero());
    }

    @Override
    public boolean isIdentity() {
        return isEquals(identity());
    }

    @NonNull
    @Override
    public Numerical getZero() {
        return Numerical.zero();
    }

    @NonNull
    @Override
    public Numerical getIdentity() {
        return Numerical.identity();
    }

    @Override
    public String toString() {
        double roundedValue = new BigDecimal(value)
                .setScale(2, RoundingMode.HALF_UP).doubleValue();
        return Double.toString(roundedValue);
    }

    public boolean isEquals(@NonNull Numerical num) {
        return (Math.abs(value - num.value) < precision);
    }

    @Override
    public int compareTo(@NonNull Numerical o) {
        return Double.compare(value, o.value);
    }
}
