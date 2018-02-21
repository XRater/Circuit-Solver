package ru.spbau.mit.circuit.logic.math.algebra.interfaces;


/**
 * Field is widened with inverse elements over multiplication.
 */
public interface Quot<F extends Field<F>, A extends Quot<F, A>> extends Algebra<F, A> {

    @Override
    A reciprocal();
}
