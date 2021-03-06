package ru.spbau.mit.circuit.logic.math.functions;

import android.support.annotation.NonNull;

import ru.spbau.mit.circuit.logic.math.algebra.Numerical;
import ru.spbau.mit.circuit.logic.math.algebra.QuotElement;

public class Function extends QuotElement<Numerical, PolyExponent, PolyFunction, Function> {

    /**
     * Base constructor. Creates zero / id function.
     */
    private Function() {
        up = PolyFunctions.zero().empty();
        down = PolyFunctions.zero().single();
    }

    /**
     * Constructs function of PolyFunction.
     */
    Function(PolyFunction f) {
        up = f;
        down = PolyFunctions.constant(1);
    }

    @NonNull
    @Override
    protected Function empty() {
        return new Function();
    }

    @NonNull
    @Override
    protected Function single() {
        Function f = new Function();
        f.up = PolyFunctions.zero().single();
        return f;
    }

    @NonNull
    @Override
    public Function getZero() {
        return Functions.constant(0);
    }

    @NonNull
    @Override
    public Function getIdentity() {
        return Functions.identity();
    }

    @Override
    protected void simplify() {
        super.simplify();
        if (down.isSingle()) {
            Numerical downValue = Numerical.number(down.doubleValue());
            up = up.multiplyConstant(downValue.reciprocal());
            down = down.multiplyConstant(downValue.reciprocal());
        }
    }

    @Override
    protected PolyExponent gcd() {
        if (down.isSingle()) {
            return down.front();
        }
        return PolyExponent.identity();
    }

    public Function integrate() {
        if (!down.isIdentity()) {
            throw new IllegalArgumentException();
        }
        return construct(up.integrate(), down);
    }

    public Function differentiate() {
        if (down.isIdentity()) {
            return construct(up.differentiate(), down);
        }
        throw new UnsupportedOperationException();
    }

    /**
     * Evaluates function in point
     *
     * @param x point to evaluate in.
     * @return result, represented as Numerical object
     */
    @SuppressWarnings("SameParameterValue")
    public Numerical apply(double x) {
        return up.apply(x).divide(down.apply(x));
    }
}
