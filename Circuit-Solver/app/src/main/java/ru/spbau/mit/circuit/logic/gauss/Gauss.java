package ru.spbau.mit.circuit.logic.gauss;


import ru.spbau.mit.circuit.logic.gauss.algebra.Field;
import ru.spbau.mit.circuit.logic.gauss.algebra.Linear;

public interface Gauss<C extends Field<C>, T extends Linear<C, T>> extends Linear<C, T> {

    C coefficientAt(int index);

    int size();
}
