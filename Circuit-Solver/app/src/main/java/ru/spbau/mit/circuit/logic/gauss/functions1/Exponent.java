package ru.spbau.mit.circuit.logic.gauss.functions1;


import ru.spbau.mit.circuit.logic.gauss.algebra.Numerical;
import ru.spbau.mit.circuit.logic.gauss.functions1.exceptions.IllegalFunctionCompareException;
import ru.spbau.mit.circuit.logic.gauss.functions1.exceptions
        .IllegalFunctionTransformationException;

@Deprecated
public class Exponent implements PrimaryFunction {

    private double cf;
    private double power;

    Exponent(double power) {
        this.power = power;
        this.cf = 1;
    }

    public Exponent(Exponent exponent) {
        power = exponent.power;
        cf = exponent.cf;
    }

    public Exponent(double power, double cf) {
        this.cf = cf;
        this.power = power;
    }

    @Override
    public int rank() {
        return 4;
    }

    @Override
    public int compare(PrimaryFunction o) {
        if (o instanceof Exponent) {
            return Double.compare(power, ((Exponent) o).power);
        }
        throw new IllegalFunctionCompareException();
    }

    @Override
    public PrimaryFunction copy() {
        return new Exponent(this);
    }

    @Override
    public PrimaryFunction add(PrimaryFunction f) {
        if (f instanceof Exponent) {
            if (power != ((Exponent) f).power) {
                throw new IllegalFunctionTransformationException();
            }
            return new Exponent(power, cf + ((Exponent) f).cf);
        }
        throw new IllegalFunctionTransformationException();
    }

    @Override
    public PrimaryFunction mul(PrimaryFunction function) {
        if (function instanceof Exponent) {
            Exponent exp = (Exponent) function;
            power += exp.power;
            cf *= exp.cf;
            return this;
        }
        throw new IllegalFunctionTransformationException();
    }

    @Override
    public PrimaryFunction inverse() {
        cf = 1 / cf;
        power = -power;
        return this;
    }

    @Override
    public PrimaryFunction negate() {
        cf = -cf;
        return this;
    }

    @Override
    public boolean isZero() {
        return cf == 0;
    }

    @Override
    public PrimaryFunction mul(Numerical num) {
        cf *= num.value();
        return this;
    }

    @Override
    public String toString() {
        if (Math.abs(power - Math.round(power)) < 0.0001) {
            return cf + "e^" + Math.round(power);
        }
        return cf + "e^" + power;
    }
}
