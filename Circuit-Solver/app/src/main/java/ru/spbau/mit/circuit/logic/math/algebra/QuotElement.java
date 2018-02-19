package ru.spbau.mit.circuit.logic.math.algebra;

import android.support.annotation.NonNull;

import ru.spbau.mit.circuit.logic.math.algebra.exceptions.IllegalInverseException;
import ru.spbau.mit.circuit.logic.math.algebra.interfaces.Field;
import ru.spbau.mit.circuit.logic.math.algebra.interfaces.OrderedGroup;
import ru.spbau.mit.circuit.logic.math.algebra.interfaces.Quot;

/**
 * This class is created to lift up group class to the quot class.
 * <p>
 * Any element of QuotElement type contains nominator and denominator (of PolyElement type),
 * therefore it is similar to simple fractions.
 *
 * @param <F> type of coefficients field
 * @param <G> type of group
 * @param <A> type of PolyElement raised from group
 * @param <I> type of our result class
 */
public abstract class QuotElement<
        F extends Field<F>,
        G extends OrderedGroup<G>,
        A extends PolyElement<F, G, A>,
        I extends QuotElement<F, G, A, I>> implements Quot<F, I> {

    protected A up;
    protected A down;

    /**
     * The method constructs new empty QuotElement
     */
    protected abstract I empty();

    /**
     * The method constructs new QuotElement, that equals to identity.
     */
    protected abstract I single();

    /**
     * The method to find greatest common division of nominator and denominator.
     * <p>
     * Required for simplify function.
     */
    protected abstract G gcd();

    /**
     * Constructs QuotElement from its nominator and denominator.
     *
     * @param up   nominator
     * @param down denominator
     * @return new QuotElement
     */
    protected I construct(A up, A down) {
        I answer = getZero();
        answer.up = up;
        answer.down = down;
        answer.simplify();
        return answer;
    }

    /**
     * Sort of simplify function for fractions. In base variant just divides nominator and
     * denominator on their greatest common division.
     */
    protected void simplify() {
        G gcd = gcd();
        up = up.div(gcd);
        down = down.div(gcd);
    }

    @Override
    public I add(@NonNull I other) {
        A nUp = up.multiply(other.down).add(other.up.multiply(down));
        if (nUp.isZero()) {
            return empty();
        }
        return construct(nUp, down.multiply(other.down));
    }

    @Override
    public I multiply(@NonNull I other) {
        A nUp = up.multiply(other.up);
        if (up.isZero()) {
            return empty();
        }
        return construct(nUp, down.multiply(other.down));
    }

    @Override
    public I multiplyConstant(@NonNull F f) {
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

    @NonNull
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
