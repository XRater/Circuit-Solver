package ru.spbau.mit.circuit.logic.math.functions;

import android.support.annotation.NonNull;

import ru.spbau.mit.circuit.logic.math.algebra.Field;
import ru.spbau.mit.circuit.logic.math.algebra.Linear;
import ru.spbau.mit.circuit.logic.math.algebra.Numerical;
import ru.spbau.mit.circuit.logic.math.algebra.exceptions.IllegalInverseException;

public class Function implements Field<Function>, Linear<Numerical, Function> {

    private PolyFunction up;
    private PolyFunction down;

    Function(PolyFunction f) {
        up = f;
        down = PolyFunctions.constant(1);
    }

    private Function(PolyFunction up, @NonNull PolyFunction down) {
        if (down.isZero()) {
            throw new IllegalArgumentException();
        }
        this.down = down;
        this.up = up;
        simplify();
    }

    private void simplify() {
        up = up.div(down);
        down = down.div(down);
    }

    @Override
    public Function add(@NonNull Function other) {
        PolyFunction nUp = up.multiply(other.down).add(other.up.multiply(down));
        if (nUp.isZero()) {
            return Functions.zero();
        }
        return new Function(nUp, down.multiply(other.down));
    }

    @Override
    public Function multiply(@NonNull Function other) {
        PolyFunction nUp = up.multiply(other.up);
        if (up.isZero()) {
            return Functions.zero();
        }
        return new Function(nUp, down.multiply(other.down));
    }

    @NonNull
    @Override
    public Function multiplyConstant(Numerical d) {
        return new Function(up.multiplyConstant(d), down);
    }

    @NonNull
    @Override
    public Function reciprocal() {
        if (up.isZero()) {
            throw new IllegalInverseException();
        }
        return new Function(down, up);
    }

    @NonNull
    @Override
    public Function negate() {
        return new Function(up.multiplyConstant(Numerical.number(-1)), down);
    }

    @Override
    public boolean isZero() {
        return up.isZero();
    }

    @Override
    public boolean isIdentity() {
        return up.isIdentity() && down.isIdentity();
    }

    @NonNull
    @Override
    public Function getZero() {
        return Functions.constant(0);
    }

    @Override
    public Function getIdentity() {
        return Functions.identity();
    }

    @NonNull
    public Function integrate() {
        if (!down.isIdentity()) {
            throw new IllegalArgumentException();
        }
        return new Function(up.integrate(), down);
    }

    @NonNull
    public Function differentiate() {
        if (down.isIdentity()) {
            return new Function(up.differentiate(), down);
        }
        throw new UnsupportedOperationException();
    }

    @NonNull
    @Override
    public String toString() {
        if (isZero()) {
            return "0";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("").append(up.toString());
        if (down.isIdentity()) {
            return sb.append("").toString();
        }
        sb.append("/").append(down.toString()).append("");
        return sb.toString();
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
