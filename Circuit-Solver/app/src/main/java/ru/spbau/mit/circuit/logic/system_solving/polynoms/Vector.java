package ru.spbau.mit.circuit.logic.system_solving.polynoms;

import java.util.Map;
import java.util.TreeMap;

public class Vector<T extends Comparable<? super T>, S extends Linear<S>> implements
        Linear<Vector<T, S>> {

    private final TreeMap<T, Double> data = new TreeMap<>();
    private final S constant;

    public Vector(S constant) {
        this.constant = constant;
    }

    @Override
    public void add(Vector<T, S> item) {
        constant.add(constant);
        for (Map.Entry<T, Double> entry : item.data.entrySet()) {
            add(entry.getKey(), entry.getValue());
        }
    }

    public void add(T t, double cf) {
        if (data.containsKey(t)) {
            data.put(t, data.get(t) + cf);
        } else {
            data.put(t, cf);
        }
    }

    public void addConst(S s) {
        constant.add(s);
    }

    @Override
    public void mul(double d) {
        constant.mul(d);
        for (Map.Entry<T, Double> entry : data.entrySet()) {
            data.put(entry.getKey(), entry.getValue() * d);
        }
    }
}
