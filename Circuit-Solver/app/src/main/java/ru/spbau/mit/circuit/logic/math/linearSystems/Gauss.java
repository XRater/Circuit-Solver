package ru.spbau.mit.circuit.logic.math.linearSystems;


import ru.spbau.mit.circuit.logic.math.algebra.interfaces.Field;
import ru.spbau.mit.circuit.logic.math.algebra.interfaces.Linear;

/**
 * Interface, that represents any object that may be used as an object in linear system.
 *
 * @param <F> type of coefficients
 * @param <T> type of stored value
 */
public interface Gauss<F extends Field<F>, T extends Linear<F, T>> extends Linear<F, T> {

    F coefficientAt(int index);

    int size();
}
