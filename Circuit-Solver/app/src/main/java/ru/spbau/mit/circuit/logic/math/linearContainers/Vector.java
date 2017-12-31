package ru.spbau.mit.circuit.logic.math.linearContainers;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import ru.spbau.mit.circuit.logic.math.algebra.Field;
import ru.spbau.mit.circuit.logic.math.linearContainers.exceptions.IllegalAdditionException;
import ru.spbau.mit.circuit.logic.math.linearSystems.Gauss;

/**
 * Fixed size linear container. May store different values with their coefficients.
 * <p>
 * Stored values will be fixed at the moment of initialization, but their coefficients may alter.
 *
 * @param <C> type of the coefficient
 * @param <T> type of the stored value
 */
public class Vector<C extends Field<C>, T extends Comparable<? super T>> implements Gauss<C,
        Vector<C, T>> {

    private final ArrayList<T> data = new ArrayList<>();
    private final ArrayList<C> coefficients = new ArrayList<>();

    private final int size;

    public Vector(Collection<T> c, C initial) {
        data.addAll(c);
        Collections.sort(data);
        size = data.size();
        for (int i = 0; i < size; i++) {
            coefficients.add(initial);
        }
    }

    @SuppressWarnings("unused")
    public void setCoefficients(List<C> coefficients) {
        int index = 0;
        for (C c : coefficients) {
            this.coefficients.set(index++, c);
        }
    }

    @Override
    public Vector<C, T> add(Vector<C, T> item) {
        if (size != item.size()) {
            throw new IllegalAdditionException();
        }
        Iterator<T> our = data.iterator();
        Iterator<T> their = item.data.iterator();
        int index = 0;
        while (our.hasNext()) {
            if (our.next().compareTo(their.next()) != 0) {
                throw new IllegalAdditionException();
            }
            C newC = coefficients.get(index).add(item.coefficients.get(index));
            coefficients.set(index, newC);
            index++;
        }
        if (their.hasNext()) {
            throw new IllegalAdditionException();
        }
        return this;
    }

    public void add(T t, C cf) {
        int index = 0;
        for (T t1 : data) {
            if (t1.compareTo(t) == 0) {
                C newC = coefficients.get(index).add(cf);
                coefficients.set(index, newC);
                return;
            }
            index++;
        }
        throw new IllegalAdditionException();
    }

    @Override
    public Vector<C, T> multiplyConstant(C c) {
        for (int i = 0; i < size; i++) {
            C newC = coefficients.get(i).multiply(c);
            coefficients.set(i, newC);
        }
        return this;
    }

    @Override
    public C coefficientAt(int index) {
        return coefficients.get(index);
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
        int i = 0;
        for (C c : coefficients) {
            sb.append(c).append(data.get(i++)).append(" ");
        }
        return sb.toString();
    }
}
