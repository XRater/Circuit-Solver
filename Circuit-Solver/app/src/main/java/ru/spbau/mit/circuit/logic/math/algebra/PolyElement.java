package ru.spbau.mit.circuit.logic.math.algebra;

<<<<<<< HEAD
=======
import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.annotation.NonNull;

>>>>>>> 6b82495abf2f455407fd3ea4d0df763b0fbbbdfc
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
 * @param <I>
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

    public I singleton(@NonNull F cf, G g) {
        I result = empty();
        result.add(cf, g);
        return result;
    }

    private I copy(@NonNull I p) {
        I result = empty();
        result.data.putAll(p.data);
        return result;
    }

    @Override
    public I add(@NonNull I p) {
        I result = copy(p);
        for (Pair<F, G> pr : data.values()) {
            result.add(pr.first, pr.second);
        }
        return result;
    }

    @SuppressWarnings("WeakerAccess")
    protected void add(@NonNull F cf, G g) {
        if (cf.isZero()) {
            return;
        }
        Pair<F, G> p = new Pair<>(cf, g);
        if (data.containsKey(g)) {
            p = new Pair<>(cf.add(data.get(g).first), g);
        }
        data.put(g, p);
        if (data.get(g).first.isZero()) {
            data.remove(g);
        }
    }

    @Override
    public I multiply(@NonNull I other) {
        if (other.isZero() || isZero()) {
            return getZero();
        }
        I result = empty();
        for (Pair<F, G> p1 : data.values()) {
            for (Pair<F, G> p2 : other.data.values()) {
                result.add(p1.first.multiply(p2.first), p1.second.multiply(p2.second));
            }
        }
        return result;
    }

    @Override
    public I negate() {
        I result = empty();
        for (Pair<F, G> pr : data.values()) {
            result.add(pr.first.negate(), pr.second);
        }
        return result;
    }

    @Override
    public I multiplyConstant(@NonNull F cf) {
        if (cf.isZero() || this.isZero()) {
            return getZero();
        }
        I result = empty();
        for (Pair<F, G> pr : data.values()) {
            result.add(pr.first.multiply(cf), pr.second);
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
        return pr.first.isIdentity() && pr.second.isIdentity();
    }

    public boolean isSingle() {
        return data.size() == 1;
    }

    @Override
    public I getIdentity() {
        return single();
    }

    I div(@NonNull G g) {
        I answer = empty();
        for (Pair<F, G> pair : data.values()) {
            answer.add(pair.first, pair.second.divide(g));
        }
        return answer;
    }

    @NonNull
    protected Pair<F, G> pair(F f, G g) {
        return new Pair<>(f, g);
    }

    @NonNull
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

<<<<<<< HEAD
=======
    public int print(@NonNull Canvas canvas, int x, int y) {
        Iterator<Pair<F, G>> iter = data.values().iterator();
        if (iter.hasNext())
            x += iter.next().print(canvas, x, y);
        else
            System.out.println("(((((((((((((((((");
        Rect textSize = new Rect();
        while (iter.hasNext()) {
            ELEMENTS_PAINT.getTextBounds(" + ", 0, " + ".length(), textSize);
            canvas.drawText(" + ", x, y, ELEMENTS_PAINT);
            x += textSize.width();
            x += iter.next().print(canvas, x, y);
        }
        return x;
    }

>>>>>>> 6b82495abf2f455407fd3ea4d0df763b0fbbbdfc
    protected class Pair<U, V> {

        private final U first;
        private final V second;

        Pair(U u, V v) {
            first = u;
            second = v;
        }

        public U first() {
            return first;
        }

        public V second() {
            return second;
        }

        @NonNull
        @Override
        public String toString() {
            return first + "*" + second;
        }
<<<<<<< HEAD
=======

        public int print(@NonNull Canvas canvas, int x, int y) {
            Rect textSize = new Rect();
            System.out.println(first.toString() + " " + second.toString());
            ELEMENTS_PAINT.getTextBounds(first.toString(), 0, first.toString().length(), textSize);
            canvas.drawText(first.toString(), x, y, ELEMENTS_PAINT);
            x += textSize.width();
            x += ((PolyExponent) second).print(canvas, x, y);
            return x;
        }
>>>>>>> 6b82495abf2f455407fd3ea4d0df763b0fbbbdfc
    }
}
