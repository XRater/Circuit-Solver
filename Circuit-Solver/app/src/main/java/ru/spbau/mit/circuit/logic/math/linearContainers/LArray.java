package ru.spbau.mit.circuit.logic.math.linearContainers;


import java.util.Arrays;

import ru.spbau.mit.circuit.logic.math.algebra.interfaces.Abel;
import ru.spbau.mit.circuit.logic.math.algebra.interfaces.Proportional;

public class LArray<F, L extends Abel<L> & Proportional<F, L>>
        implements Abel<LArray<F, L>>, Proportional<F, LArray<F, L>> {

    public static <F, L extends Abel<L> & Proportional<F, L>> LArray<F, L> array(int size, L
            element) {
        return new LArray<>(size, element);
    }

    @SafeVarargs
    public static <F, L extends Abel<L> & Proportional<F, L>> LArray<F, L> array(L... array) {
        if (array.length == 0) {
            throw new IllegalArgumentException();
        }
        LArray<F, L> result = new LArray<>(array.length, array[0]);
        for (int i = 0; i < result.size; i++) {
            result.set(i, array[i]);
        }
        return result;
    }

    private final L[] data;
    private final int size;

    private final L fieldZero;

    @SuppressWarnings("unchecked")
    protected LArray(int size, L element) {
        data = (L[]) new Object[size];
        this.size = size;
        fieldZero = element.getZero();
        for (int i = 0; i < size; i++) {
            set(i, fieldZero);
        }
    }

    public int size() {
        return size;
    }

    public void set(int i, L element) {
        data[i] = element;
    }

    public L get(int i) {
        return data[i];
    }

    @Override
    public LArray<F, L> add(LArray<F, L> item) {
        if (size != item.size()) {
            throw new IllegalArgumentException();
        }
        for (int i = 0; i < size; i++) {
            set(i, get(i).add(item.get(i)));
        }
        return this;
    }

    @Override
    public LArray<F, L> multiplyConstant(F f) {
        for (int i = 0; i < size; i++) {
            set(i, get(i).multiplyConstant(f));
        }
        return this;
    }

    @Override
    public LArray<F, L> negate() {
        for (int i = 0; i < size; i++) {
            set(i, get(i).negate());
        }
        return this;
    }

    @Override
    public boolean isZero() {
        for (int i = 0; i < size; i++) {
            if (!get(i).isZero()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public LArray<F, L> getZero() {
        return new LArray<>(size, fieldZero);
    }

    @Override
    public String toString() {
        return Arrays.toString(data);
    }
}
