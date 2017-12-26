package ru.spbau.mit.circuit.logic.math.algebra;


import org.apache.commons.math3.complex.Complex;

import ru.spbau.mit.circuit.logic.math.algebra.exceptions.IllegalInverseException;

public class Numerical implements Field<Numerical>, Linear<Numerical, Numerical> {

    private final Complex value;
    private final double precision = 0.0001;

    public static Numerical number(double value) {
        return new Numerical(value);
    }

    public static Numerical number(Complex value) {
        return new Numerical(value);
    }

    public static Numerical zero() {
        return new Numerical(0);
    }

    public static Numerical identity() {
        return new Numerical(1);
    }

    private Numerical(Complex value) {
        this.value = value;
    }

    private Numerical(double value) {
        this.value = new Complex(value);
    }

    public double value() {
        return value.getReal();
    }

    @Override
    public Numerical add(Numerical f) {
        return new Numerical(value.add(f.value));
    }

    @Override
    public Numerical multiplyConstant(Numerical cf) {
        return multiply(cf);
    }

    @Override
    public Numerical multiply(Numerical f) {
        return new Numerical(value.multiply(f.value));
    }

    @Override
    public Numerical reciprocal() {
        if (!isZero()) {
            return new Numerical(value.reciprocal());
        }
        throw new IllegalInverseException();
    }

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

    @Override
    public Numerical getZero() {
        return Numerical.zero();
    }

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

    public boolean isEquals(Numerical num) {
        return Complex.equals(value, num.value, precision);
    }
}
