package ru.spbau.mit.circuit.logic.math.matrices.matrixExponent;


import android.support.annotation.NonNull;

import org.apache.commons.math3.complex.Complex;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import ru.spbau.mit.circuit.logic.math.functions.Function;
import ru.spbau.mit.circuit.logic.math.functions.Functions;

class SubtractColumn {

    private static final Function functionZero = Functions.constant(0);
    private static final double precision = 0.0001;

    private final ArrayList<Element> elements = new ArrayList<>();

    public SubtractColumn(@NonNull List<Complex> roots) {
        for (Complex root : roots) {
            elements.add(new Element(root));
        }
    }

    public Function first() {
        if (elements.size() == 0) {
            throw new NoSuchElementException();
        }
        return elements.get(0).value;
    }

    public void next() {
        for (int i = 0; i < elements.size() - 1; i++) {
            elements.set(i, new Element(elements.get(i), elements.get(i + 1)));
        }
        elements.remove(elements.size() - 1);
    }

    private static class Element {
        private final Complex begin;
        private final Complex end;
        private final int size;

        private final Function value;

        public Element(@NonNull Complex root) {
            begin = root;
            end = root;
            size = 1;
            // TODO
            value = Functions.exponent(root.getReal());
        }

        public Element(@NonNull Element first, @NonNull Element second) {
            if (first.size != second.size) {
                throw new IllegalArgumentException();
            }
            begin = first.begin;
            end = second.end;
            size = first.size + 1;
            if (Complex.equals(first.begin, second.end, precision)) {
                value = second.value.multiply(Functions.power(1)).divide(Functions.constant(first
                        .size));
            } else {
                value = second.value.subtract(first.value)
                        .divide(Functions.constant((end.subtract(begin).getReal())));
            }
        }

    }
}
