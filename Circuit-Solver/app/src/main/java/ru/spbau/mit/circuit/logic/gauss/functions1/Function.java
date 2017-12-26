package ru.spbau.mit.circuit.logic.gauss.functions1;


import ru.spbau.mit.circuit.logic.gauss.algebra.Field;
import ru.spbau.mit.circuit.logic.gauss.algebra.Numerical;
import ru.spbau.mit.circuit.logic.gauss.algebra.exceptions.IllegalInverseException;

public class Function implements Field<Function> {

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
//        PolyExponent gcd = gcd(up, down);
//        up = up.div(gcd);
//        down.div(gcd);
        up = up.div(down);
        down = down.div(down);
    }

//    private PolyExponent gcd(PolyFunction f, PolyFunction g) {
//        int ePow = f.
//        return null;
//    }

    @Override
    public Function copy() {
        return new Function(this);
    }

    @Override
    public Function add(Function other) {
        return new Function(up.mul(other.down).add(other.up.mul(down)), down.mul(other.down));
    }

    @Override
    public Function mul(Function other) {
        return new Function(up.mul(other.up), down.mul(other.down));
    }

    public Function mul(double d) {
        return new Function(up.mul(Numerical.number(d)), down);
    }

    @Override
    public Function inverse() {
        if (up.isZero()) {
            throw new IllegalInverseException();
        }
        return new Function(down, up);
    }

    @Override
    public Function negate() {
        return new Function(up.mul(Numerical.number(-1)), down);
    }

    @Override
    public boolean isZero() {
        return up.isZero();
    }

    @Override
    public boolean isIdentity() {
        throw new UnsupportedOperationException();
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
}
