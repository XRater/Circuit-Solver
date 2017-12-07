package ru.spbau.mit.circuit.logic.gauss.functions1;


import ru.spbau.mit.circuit.logic.gauss.algebra.Field;
import ru.spbau.mit.circuit.logic.gauss.algebra.Numerical;
import ru.spbau.mit.circuit.logic.gauss.algebra.exceptions.IllegalInverseException;

public class Function implements Field<Function> {

    private PolyFunction up;
    private PolyFunction down;

    private Function(Function f) {
        up = f.up;
        down = f.down;
    }

    @Override
    public Function copy() {
        return new Function(this);
    }

    @Override
    public Function add(Function other) {
        up.mul(other.down);
        up.add(other.up.copy().mul(down));
        down.mul(other.down);
        return this;
    }

    @Override
    public Function mul(Function other) {
        up.mul(other.up);
        down.mul(other.down);
        return this;
    }

    @Override
    public Function inverse() {
        if (up.isZero()) {
            throw new IllegalInverseException();
        }
        PolyFunction tmp = up;
        up = down;
        down = tmp;
        return this;
    }

    @Override
    public Function negate() {
        up.mul(Numerical.number(-1));
        return this;
    }

    @Override
    public boolean isZero() {
        return up.isZero();
    }

    @Override
    public boolean isIdentity() {
        throw new UnsupportedOperationException();
    }

}
