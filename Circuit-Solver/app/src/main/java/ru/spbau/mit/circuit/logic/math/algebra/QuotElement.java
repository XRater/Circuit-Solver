package ru.spbau.mit.circuit.logic.math.algebra;

import ru.spbau.mit.circuit.logic.math.algebra.exceptions.IllegalInverseException;
import ru.spbau.mit.circuit.logic.math.algebra.interfaces.Field;
import ru.spbau.mit.circuit.logic.math.algebra.interfaces.OrderedGroup;
import ru.spbau.mit.circuit.logic.math.algebra.interfaces.Quot;

public abstract class QuotElement<
        F extends Field<F>,
        G extends OrderedGroup<G>,
        A extends PolyElement<F, G, A>,
        I extends QuotElement<F, G, A, I>> implements Quot<F, I> {

    protected A up;
    protected A down;

    protected abstract I empty();

    protected abstract I single();

    protected abstract G gcd();

    protected I construct(A up, A down) {
        I answer = getZero();
        answer.up = up;
        answer.down = down;
        answer.simplify();
        return answer;
    }

    protected void simplify() {
        if (up.isZero()) {
            down = down.getIdentity();
            return;
        }
        G gcd = gcd();
        up = up.div(gcd);
        down = down.div(gcd);
        if (down.isSingle()) {
            F constant = down.singleValue().reciprocal();
            up = up.multiplyConstant(constant);
            down = down.multiplyConstant(constant);
        }
    }

    @Override
    public I add(I other) {
        A nUp = up.multiply(other.down).add(other.up.multiply(down));
        if (nUp.isZero()) {
            return empty();
        }
        return construct(nUp, down.multiply(other.down));
    }

    @Override
    public I multiply(I other) {
        A nUp = up.multiply(other.up);
        if (up.isZero()) {
            return empty();
        }
        return construct(nUp, down.multiply(other.down));
    }

    @Override
    public I multiplyConstant(F f) {
        return construct(up.multiplyConstant(f), down);
    }

    @Override
    public I reciprocal() {
        if (up.isZero()) {
            throw new IllegalInverseException();
        }
        return construct(down, up);
    }

    @Override
    public I negate() {
        return construct(up.negate(), down);
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
    public I getZero() {
        return empty();
    }

    @Override
    public I getIdentity() {
        return single();
    }

    @Override
    public String toString() {
        if (isZero()) {
            return "empty";
        }
        StringBuilder sb = new StringBuilder();
        sb.append(up.toString());
        if (down.isIdentity()) {
            return sb.toString();
        }
        sb.append("/").append(down.toString());
        return sb.toString();
    }
}
