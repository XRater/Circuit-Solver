package ru.spbau.mit.circuit.logic.math.functions;

import ru.spbau.mit.circuit.logic.math.algebra.Field;
import ru.spbau.mit.circuit.logic.math.algebra.Linear;
import ru.spbau.mit.circuit.logic.math.algebra.Numerical;
import ru.spbau.mit.circuit.logic.math.algebra.exceptions.IllegalInverseException;

public class Function implements Field<Function>, Linear<Numerical, Function> {

    private PolyFunction up;
    private PolyFunction down;

    public Function(PolyFunction f) {
        up = f;
        down = PolyFunctions.constant(1);
    }

    private Function(Function f) {
        up = f.up;
        down = f.down;
    }

    private Function(PolyFunction up, PolyFunction down) {
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
    public Function add(Function other) {
        return new Function(up.multiply(other.down).add(other.up.multiply(down)), down.multiply
                (other.down));
    }

    @Override
    public Function multiply(Function other) {
        return new Function(up.multiply(other.up), down.multiply(other.down));
    }

    @Override
    public Function multiplyConstant(Numerical d) {
        return new Function(up.multiplyConstant(d), down);
    }

    @Override
    public Function reciprocal() {
        if (up.isZero()) {
            throw new IllegalInverseException();
        }
        return new Function(down, up);
    }

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

    @Override
    public Function getZero() {
        return Functions.constant(0);
    }

    @Override
    public Function getIdentity() {
        return Functions.identity();
    }

    public Function integrate() {
        if (!down.isIdentity()) {
            throw new IllegalArgumentException();
        }
        return new Function(up.integrate(), down);
    }

    public Function differentiate() {
        if (down.isIdentity()) {
            return new Function(up.differentiate(), down);
        }
        throw new UnsupportedOperationException();
    }

    @Override
    public String toString() {
        if (isZero()) {
            return "(0)";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("(").append(up.toString());
        if (down.isIdentity()) {
            return sb.append(")").toString();
        }
        sb.append(")/(").append(down.toString()).append(")");
        return sb.toString();
    }

    public Numerical apply(double x) {
        return up.apply(x).divide(down.apply(x));
    }
}
