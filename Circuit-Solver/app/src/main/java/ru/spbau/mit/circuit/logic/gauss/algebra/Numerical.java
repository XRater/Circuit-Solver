package ru.spbau.mit.circuit.logic.gauss.algebra;


import ru.spbau.mit.circuit.logic.gauss.algebra.exceptions.IllegalInverseException;

public class Numerical implements Field<Numerical> {

    private final double value;

    public static Numerical number(double value) {
        return new Numerical(value);
    }

    public static Numerical zero() {
        return new Numerical(0);
    }

    public static Numerical identity() {
        return new Numerical(1);
    }

    private Numerical(double value) {
        this.value = value;
    }

    public double value() {
        return value;
    }

    @Override
    public Numerical copy() {
        return new Numerical(value);
    }

    @Override
    public Numerical add(Numerical f) {
        return new Numerical(value + f.value);
    }

    @Override
    public Numerical mul(Numerical f) {
        return new Numerical(value * f.value);
    }

    @Override
    public Numerical inverse() {
        if (!isZero()) {
            return new Numerical(1 / value);
        }
        throw new IllegalInverseException();
    }

    @Override
    public Numerical negate() {
        return new Numerical(-value);
    }

    @Override
    public boolean isZero() {
        return value == 0;
    }

    @Override
    public boolean isIdentity() {
        return value == 1;
    }

    @Override
    public String toString() {
        return Double.toString(value);
    }
}
