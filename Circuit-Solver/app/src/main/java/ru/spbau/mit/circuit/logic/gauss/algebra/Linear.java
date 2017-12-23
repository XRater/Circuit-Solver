package ru.spbau.mit.circuit.logic.gauss.algebra;

import org.apache.commons.math3.FieldElement;

public interface Linear<C extends FieldElement<C>, T extends Linear<C, T>> {

    T add(T item);

    T mul(C cf);
}
