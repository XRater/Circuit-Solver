package ru.spbau.mit.circuit.logic.gauss.functions1;


import ru.spbau.mit.circuit.logic.gauss.algebra.Numerical;
import ru.spbau.mit.circuit.logic.gauss.algebra.exceptions.IllegalInverseException;
import ru.spbau.mit.circuit.logic.gauss.functions1.exceptions.IllegalFunctionCompareException;
import ru.spbau.mit.circuit.logic.gauss.functions1.exceptions
        .IllegalFunctionTransformationException;

@Deprecated
class Constant implements PrimaryFunction {

    private final double value;

    public Constant(double value) {
        this.value = value;
    }

    public Constant(Constant constant) {
        value = constant.value;
    }

    @Override
    public int rank() {
        return 10;
    }

    @Override
    public PrimaryFunction copy() {
        return new Constant(this);
    }

    @Override
    public PrimaryFunction add(PrimaryFunction f) {
        if (f instanceof Constant) {
            return new Constant(value + ((Constant) f).value);
        }
        throw new IllegalFunctionTransformationException();
    }

    @Override
    public PrimaryFunction mul(PrimaryFunction f) {
        return f.copy().mul(Numerical.number(value));
    }

    @Override
    public PrimaryFunction mul(Numerical num) {
        return new Constant(value * num.value());
    }

    @Override
    public PrimaryFunction inverse() {
        if (isZero()) {
            throw new IllegalInverseException();
        }
        return new Constant(1 / value);
    }

    @Override
    public PrimaryFunction negate() {
        return new Constant(-value);
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
    public int compare(PrimaryFunction o) {
        if (o instanceof Constant) {
            return 0;
        }
        throw new IllegalFunctionCompareException();
    }

    @Override
    public String toString() {
        return Double.toString(value);
    }

}
