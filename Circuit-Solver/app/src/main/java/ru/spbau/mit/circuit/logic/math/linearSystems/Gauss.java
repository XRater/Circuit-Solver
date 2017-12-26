package ru.spbau.mit.circuit.logic.math.linearSystems;


import ru.spbau.mit.circuit.logic.math.algebra.Field;
import ru.spbau.mit.circuit.logic.math.algebra.Linear;

public interface Gauss<C extends Field<C>, T extends Linear<C, T>> extends Linear<C, T> {

    C coefficientAt(int index);

    int size();
}
