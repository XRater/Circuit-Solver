package ru.spbau.mit.circuit.logic.gauss.functions1;


import org.apache.commons.math3.Field;
import org.apache.commons.math3.FieldElement;
import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.util.BigReal;

import ru.spbau.mit.circuit.logic.gauss.algebra.exceptions.IllegalInverseException;

public class Function implements FieldElement<Function> {

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
    public Function divide(Function a) throws NullArgumentException, MathArithmeticException {
        return this.multiply(a.reciprocal());
    }

    @Override
    public Function add(Function other) {
        return new Function(up.mul(other.down).add(other.up.mul(down)), down.mul(other.down));
    }

    @Override
    public Function multiply(Function other) {
        return new Function(up.mul(other.up), down.mul(other.down));
    }

    public Function mul(double d) {
        return new Function(up.mul(new BigReal(d)), down);
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
        return new Function(up.mul(new BigReal(-1)), down);
    }

    @Override
    public Function subtract(Function a) throws NullArgumentException {
        return new Function(up.mul(new BigReal(-1)), down);
    }

    @Override
    public Function multiply(int n) {
        throw new UnsupportedOperationException();
    }

    public boolean isZero() {
        return up.isZero();
    }

    //TODO
    public Function integrate() {
        return this;
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

    public static void main(String[] args) {
//        System.out.println(Functions.identity());
//        System.out.println(Functions.constant(1).mul(Functions.exponent(-2)));
        System.out.println(Functions.constant(1).add(Functions.constant(0)));
    }

    public Function differentiate() {
        if (down.isIdentity()) {
            return new Function(up.differentiate(), down);
        }
        throw new UnsupportedOperationException();
    }

    @Override
    public Field<Function> getField() {
        return new Functions();
    }
}
