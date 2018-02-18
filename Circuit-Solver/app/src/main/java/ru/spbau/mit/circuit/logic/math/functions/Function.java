package ru.spbau.mit.circuit.logic.math.functions;

import android.graphics.Canvas;
import android.support.annotation.NonNull;

import ru.spbau.mit.circuit.logic.math.algebra.Numerical;
import ru.spbau.mit.circuit.logic.math.algebra.QuotElement;

public class Function extends QuotElement<Numerical, PolyExponent, PolyFunction, Function> {

    // zero / id
    Function() {
        up = PolyFunctions.zero().empty();
        down = PolyFunctions.zero().single();
    }

    Function(PolyFunction f) {
        up = f;
        down = PolyFunctions.constant(1);
    }

    public PolyFunction getUp() {
        return up;
    }

    public PolyFunction getDown() {
        return down;
    }

    public int print(@NonNull Canvas canvas, int x, int y) {
        if (!down.isIdentity()) {
            throw new UnsupportedOperationException();
        }
        return up.print(canvas, x, y);
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
        System.out.println(this);
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
//    @SuppressWarnings("SameParameterValue")
    public Numerical apply(double x) {
        return up.apply(x).divide(down.apply(x));
    }
}
