package ru.spbau.mit.circuit.logic.math.linearSystems;


import ru.spbau.mit.circuit.logic.math.algebra.Field;
import ru.spbau.mit.circuit.logic.math.algebra.Linear;

/**
 * Interface, that represents any object that may be used as an object in linear system.
 *
 * @param <C> type of coefficients
 * @param <T> type of stored value
 */
public interface Gauss<C extends Field<C>, T extends Linear<C, T>> extends Linear<C, T> {

    C coefficientAt(int index);

    int size();
}
