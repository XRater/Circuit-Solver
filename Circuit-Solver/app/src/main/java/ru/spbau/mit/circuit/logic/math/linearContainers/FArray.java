package ru.spbau.mit.circuit.logic.math.linearContainers;


import java.lang.reflect.Array;
import java.util.Arrays;

import ru.spbau.mit.circuit.logic.math.algebra.interfaces.Abel;
import ru.spbau.mit.circuit.logic.math.algebra.interfaces.Field;
import ru.spbau.mit.circuit.logic.math.algebra.interfaces.Proportional;
import ru.spbau.mit.circuit.logic.math.expressions.Expression;

public class FArray<F extends Field<F>> implements Abel<FArray<F>>, Proportional<F, FArray<F>> {

    private Expression last;

    public static <F extends Field<F>> FArray<F> array(int size, F element) {
        return new FArray<>(size, element);
    }

    @SafeVarargs
    public static <F extends Field<F>> FArray<F> array(F... array) {
        if (array.length == 0) {
            throw new IllegalArgumentException();
        }
        FArray<F> result = new FArray<>(array.length, array[0]);
        for (int i = 0; i < result.size; i++) {
            result.set(i, array[i]);
        }
        return result;
    }

    private final F[] data;
    private final int size;

    private final F fieldZero;

    private FArray(int size, F element) {
        //noinspection unchecked
        data = (F[]) Array.newInstance(element.getClass(), size);
        this.size = size;
        fieldZero = element.getZero();
        for (int i = 0; i < size; i++) {
            set(i, fieldZero);
        }
    }

    public int size() {
        return size;
    }

    public void set(int i, F element) {
        data[i] = element;
    }

    public F get(int i) {
        return data[i];
    }

    public F back() {
        return data[data.length - 1];
    }

    @Override
    public FArray<F> add(FArray<F> item) {
        if (size != item.size()) {
            throw new IllegalArgumentException();
        }
        for (int i = 0; i < size; i++) {
            set(i, get(i).add(item.get(i)));
        }
        return this;
    }

    @Override
    public FArray<F> multiplyConstant(F f) {
        for (int i = 0; i < size; i++) {
            set(i, get(i).multiplyConstant(f));
        }
        return this;
    }

    @Override
    public FArray<F> negate() {
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
    public FArray<F> getZero() {
        return new FArray<>(size, fieldZero);
    }

    @Override
    public String toString() {
        return Arrays.toString(data);
    }
}
