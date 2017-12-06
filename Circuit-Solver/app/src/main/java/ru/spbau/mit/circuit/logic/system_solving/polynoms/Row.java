package ru.spbau.mit.circuit.logic.system_solving.polynoms;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

import ru.spbau.mit.circuit.logic.system_solving.functions.exceptions.IllegalAdditionException;

public class Row<T extends Comparable<? super T>> implements Gauss<Row<T>> {

    private final ArrayList<T> data = new ArrayList<>();
    private final double[] cfs;

    private final int size;

    public Row(Collection<T> c) {
        data.addAll(c);
        Collections.sort(data);
        size = data.size();
        cfs = new double[size];
    }

    @Override
    public void add(Row<T> item) {
        if (size != item.size()) {
            throw new IllegalAdditionException();
        }
        Iterator<T> our = data.iterator();
        Iterator<T> their = item.data.iterator();
        int i = 0;
        while (our.hasNext()) {
            if (our.next().compareTo(their.next()) != 0) {
                throw new IllegalAdditionException();
            }
            cfs[i] += item.cfs[i];
            i++;
        }
        if (their.hasNext()) {
            throw new IllegalAdditionException();
        }
    }

    public void add(T t, double cf) {
        int index = 0;
        for (T t1 : data) {
            if (t1.compareTo(t) == 0) {
                cfs[index] += cf;
                return;
            }
            index++;
        }
        throw new IllegalAdditionException();
    }

    @Override
    public void mul(double d) {
        for (int i = 0; i < size; i++) {
            cfs[i] *= d;
        }
    }

    @Override
    public double coefficientAt(int index) {
        return cfs[index];
    }

    public T valueAt(int i) {
        return data.get(i);
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (double d : cfs) {
            sb.append(d).append(" ");
        }
        return sb.toString();
    }
}
