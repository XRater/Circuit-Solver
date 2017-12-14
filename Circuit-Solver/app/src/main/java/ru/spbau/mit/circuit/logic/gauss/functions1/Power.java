package ru.spbau.mit.circuit.logic.gauss.functions1;

import ru.spbau.mit.circuit.logic.gauss.algebra.Numerical;
import ru.spbau.mit.circuit.logic.gauss.functions1.exceptions.IllegalFunctionCompareException;
import ru.spbau.mit.circuit.logic.gauss.functions1.exceptions
        .IllegalFunctionTransformationException;

@Deprecated
public class Power implements PrimaryFunction {

    private double cf;
    private int power;

    public Power(int power) {
        this.power = power;
        cf = 1;
    }

    public Power(int power, double cf) {
        this.cf = cf;
        this.power = power;
    }

    public Power(Power x) {
        power = x.power;
        cf = x.cf;
    }

    @Override
    public int rank() {
        return 7;
    }

    @Override
    public PrimaryFunction copy() {
        return new Power(this);
    }

    @Override
    public PrimaryFunction add(PrimaryFunction f) {
        if (f instanceof Power) {
            Power pow = (Power) f;
            if (power != pow.power) {
                throw new IllegalFunctionTransformationException();
            }
            cf += pow.cf;
            return this;
        }
        throw new IllegalFunctionTransformationException();
    }

    @Override
    public PrimaryFunction mul(PrimaryFunction function) {
        if (function instanceof Power) {
            Power pow = (Power) function;
            power += pow.power;
            cf *= pow.cf;
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
    public int compare(PrimaryFunction o) {
        if (o instanceof Power) {
            return Double.compare(power, ((Power) o).power);
        }
        throw new IllegalFunctionCompareException();
    }

    @Override
    public PrimaryFunction mul(Numerical num) {
        cf *= num.value();
        return this;
    }

    @Override
    public String toString() {
        return cf + "x^" + power;
    }
}
