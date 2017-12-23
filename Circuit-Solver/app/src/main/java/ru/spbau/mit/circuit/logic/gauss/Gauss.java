package ru.spbau.mit.circuit.logic.gauss;

import org.apache.commons.math3.FieldElement;

import ru.spbau.mit.circuit.logic.gauss.algebra.Linear;

public interface Gauss<C extends FieldElement<C>, T extends Linear<C, T>> extends Linear<C, T> {

    C coefficientAt(int index);

    int size();
}
