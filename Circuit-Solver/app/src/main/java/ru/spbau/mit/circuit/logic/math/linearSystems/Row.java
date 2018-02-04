package ru.spbau.mit.circuit.logic.math.linearSystems;


import java.util.Map;
import java.util.TreeMap;

import ru.spbau.mit.circuit.logic.math.algebra.interfaces.Abel;
import ru.spbau.mit.circuit.logic.math.algebra.interfaces.Field;
import ru.spbau.mit.circuit.logic.math.algebra.interfaces.Proportional;

/**
 * Abel container that may store any number of elements with their coefficients in the right
 * order.
 * <p>
 * In addition may store one constant value of another linear type.
 *
 * @param <F> type of coefficients
 * @param <T> type of stored data
 * @param <F> type of constant
 */
public class Row<
        F extends Field<F>,
        T extends Comparable<? super T>>
        implements Proportional<F, Row<F, T>>, Abel<Row<F, T>> {

    protected final TreeMap<T, F> data = new TreeMap<>();
    protected F constant;

    public Row(F constant) {
        this.constant = constant;
    }

    @Override
    public Row<F, T> add(Row<F, T> item) {
        constant = constant.add(item.constant);
        for (Map.Entry<T, F> entry : item.data.entrySet()) {
            add(entry.getKey(), entry.getValue());
        }
        return this;
    }

    public void add(T t, F cf) {
        if (data.containsKey(t)) {
            data.put(t, data.get(t).add(cf));
        } else {
            data.put(t, cf);
        }
    }

    public void addConst(F f) {
        constant = constant.add(f);
    }

    // MAY RETURN NULL!
    public F get(T t) {
        return data.get(t);
    }

    @Override
    public Row<F, T> multiplyConstant(F d) {
        constant = constant.multiply(d);
        for (Map.Entry<T, F> entry : data.entrySet()) {
            data.put(entry.getKey(), entry.getValue().multiply(d));
        }
        return this;
    }

    @Override
    public Row<F, T> negate() {
        return null;
    }

    @Override
    public boolean isZero() {
        return false;
    }

    @Override
    public Row<F, T> getZero() {
        return null;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<T, F> entry : data.entrySet()) {
            sb.append(entry.getValue()).append(entry.getKey()).append(" ");
        }
        sb.append(constant);
        return sb.toString();
    }

    public F constant() {
        return constant;
    }

    //TODO
    public static <F extends Field<F>, T extends Comparable<? super T>> Row<F, T> zero() {
        return null;
    }
}
