package ru.spbau.mit.circuit.logic.gauss.linear_containers;


import java.util.Map;
import java.util.TreeMap;

import ru.spbau.mit.circuit.logic.gauss.algebra.Field;
import ru.spbau.mit.circuit.logic.gauss.algebra.Linear;

public class Row<C extends Field<C>, T extends Comparable<? super T>, S extends Linear<C, S>>
        implements
        Linear<C, Row<C, T, S>> {

    protected final TreeMap<T, C> data = new TreeMap<>();
    protected S constant;

    public Row(S constant) {
        this.constant = constant;
    }

    @Override
    public Row<C, T, S> add(Row<C, T, S> item) {
        constant = constant.add(item.constant);
        for (Map.Entry<T, C> entry : item.data.entrySet()) {
            add(entry.getKey(), entry.getValue());
        }
        return this;
    }

    public void add(T t, C cf) {
        if (data.containsKey(t)) {
            data.put(t, data.get(t).add(cf));
        } else {
            data.put(t, cf);
        }
    }

    public void addConst(S s) {
        constant = constant.add(s);
    }

    // MAY RETURN NULL!
    public C get(T t) {
        return data.get(t);
    }

    @Override
    public Row<C, T, S> multiplyConstant(C d) {
        constant = constant.multiplyConstant(d);
        for (Map.Entry<T, C> entry : data.entrySet()) {
            data.put(entry.getKey(), entry.getValue().multiply(d));
        }
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<T, C> entry : data.entrySet()) {
            sb.append(entry.getValue()).append(entry.getKey()).append(" ");
        }
        sb.append(constant);
        return sb.toString();
    }

    public S constant() {
        return constant;
    }
}
