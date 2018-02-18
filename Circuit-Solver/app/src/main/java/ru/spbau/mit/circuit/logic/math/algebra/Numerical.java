package ru.spbau.mit.circuit.logic.math.algebra;


import android.support.annotation.NonNull;

import org.apache.commons.math3.complex.Complex;

import ru.spbau.mit.circuit.logic.math.algebra.exceptions.IllegalInverseException;
import ru.spbau.mit.circuit.logic.math.algebra.interfaces.Field;

@SuppressWarnings("WeakerAccess")
public class Numerical implements Field<Numerical>, Comparable<Numerical> {

    private static final Numerical zero = Numerical.number(0);
    private static final Numerical identity = Numerical.number(1);

    private final Complex value;
    @SuppressWarnings("FieldCanBeLocal")
    private final double precision = 0.00000000001;

    private Numerical(Complex value) {
        this.value = value;
    }

    private Numerical(double value) {
        this.value = new Complex(value);
    }

    public static Numerical number(double value) {
        return new Numerical(value);
    }

    public static Numerical number(Complex value) {
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

    public double value() {
        return value.getReal();
    }

    @NonNull
    @Override
    public Numerical add(@NonNull Numerical f) {
        return new Numerical(value.add(f.value));
    }

    @NonNull
    @Override
    public Numerical multiply(@NonNull Numerical numerical) {
        return new Numerical(value.multiply(numerical.value));
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
            return new Numerical(value.reciprocal());
        }
        throw new IllegalInverseException();
    }

    @NonNull
    @Override
    public Numerical negate() {
        return new Numerical(value.negate());
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
        if (value.getImaginary() == 0) {
            return Double.toString(value.getReal());
        }
        if (value.getReal() == 0) {
            return Double.toString(value.getImaginary()) + "i";
        }
        return Double.toString(value.getReal()) + " " + Double.toString(value.getImaginary()) + "i";
    }

    public boolean isEquals(@NonNull Numerical num) {
        return Complex.equals(value, num.value, precision);
    }

    @Override
    public int compareTo(@NonNull Numerical o) {
        return Double.compare(value.getReal(), o.value.getReal());
    }
}
