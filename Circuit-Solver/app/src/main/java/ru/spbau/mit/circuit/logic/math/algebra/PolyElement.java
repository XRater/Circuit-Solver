package ru.spbau.mit.circuit.logic.math.algebra;

import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import ru.spbau.mit.circuit.logic.math.algebra.interfaces.Algebra;
import ru.spbau.mit.circuit.logic.math.algebra.interfaces.Field;
import ru.spbau.mit.circuit.logic.math.algebra.interfaces.OrderedGroup;

/**
 * This class is created to lift any group class to the Algebra level.
 * <p>
 * For example if 1, x, x^2, ... are elements of the group, then any PolyElement
 * looks like c_0 + c_1x + c_2x^2 + ... (any polynom).
 * <p>
 * Easy to observe, that such elements may be easily multiplied/added to each other.
 *
 * @param <F> field class for coefficients
 * @param <G> group class
 * @param <I> type of our result class
 */
public abstract class PolyElement<
        F extends Field<F>,
        G extends OrderedGroup<G>,
        I extends PolyElement<F, G, I>> implements Algebra<F, I> {

    protected final Map<G, Pair<F, G>> data = new TreeMap<>();

    /**
     * The method constructs new empty PolyElement
     */
    protected abstract I empty();

    /**
     * The method constructs new PolyElement, that equals to identity.
     */
    protected abstract I single();

    /**
     * Creates new PolyElement from group element and coefficient
     *
     * @param cf target coefficient
     * @param g  target group element
     * @return new PolyElement
     */
    public I singleton(F cf, G g) {
        I result = empty();
        result.add(cf, g);
        return result;
    }

    /**
     * Creates new PolyElement identical to the given one.
     */
    private I copy(I p) {
        I result = empty();
        result.data.putAll(p.data);
        return result;
    }


    @Override
    public I add(I p) {
        I result = copy(p);
        for (Pair<F, G> pr : data.values()) {
            result.add(pr.first(), pr.second());
        }
        return result;
    }

    @SuppressWarnings("WeakerAccess")
    protected void add(F cf, G g) {
        if (cf.isZero()) {
            return;
        }
        Pair<F, G> p = new Pair<>(cf, g);
        if (data.containsKey(g)) {
            p = new Pair<>(cf.add(data.get(g).first()), g);
        }
        data.put(g, p);
        if (data.get(g).first().isZero()) {
            data.remove(g);
        }
    }

    @Override
    public I multiply(I other) {
        if (other.isZero() || isZero()) {
            return getZero();
        }
        I result = empty();
        for (Pair<F, G> p1 : data.values()) {
            for (Pair<F, G> p2 : other.data.values()) {
                result.add(p1.first().multiply(p2.first()), p1.second().multiply(p2.second()));
            }
        }
        return result;
    }

    @Override
    public I negate() {
        I result = empty();
        for (Pair<F, G> pr : data.values()) {
            result.add(pr.first().negate(), pr.second());
        }
        return result;
    }

    @Override
    public I multiplyConstant(F cf) {
        if (cf.isZero() || this.isZero()) {
            return getZero();
        }
        I result = empty();
        for (Pair<F, G> pr : data.values()) {
            result.add(pr.first().multiply(cf), pr.second());
        }
        return result;
    }

    @Override
    public boolean isZero() {
        return data.size() == 0;
    }

    @Override
    public I getZero() {
        return empty();
    }

    @Override
    public boolean isIdentity() {
        if (data.size() == 0) {
            return false;
        }
        Pair<F, G> pr = data.values().iterator().next();
        return pr.first().isIdentity() && pr.second().isIdentity();
    }

    @Override
    public I getIdentity() {
        return single();
    }

    /**
     * @return true if PolyElement contains only one monom and false otherwise.
     */
    public boolean isSingle() {
        return data.size() == 1;
    }

    /**
     * Divides every monom to the given group element.
     *
     * @param g group element to divide on.
     * @return new PolyElement object.
     */
    I div(G g) {
        I answer = empty();
        for (Pair<F, G> pair : data.values()) {
            answer.add(pair.first(), pair.second().divide(g));
        }
        return answer;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (data.values().isEmpty()) {
            return "empty";
        }
        Iterator<Pair<F, G>> iter = data.values().iterator();
        sb.append(iter.next().toString());
        while (iter.hasNext()) {
            sb.append(" + ").append(iter.next().toString());
        }
        return sb.toString();
    }

}
