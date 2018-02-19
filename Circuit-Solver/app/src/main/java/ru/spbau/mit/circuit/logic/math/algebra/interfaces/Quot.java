package ru.spbau.mit.circuit.logic.math.algebra.interfaces;


public interface Quot<F extends Field<F>, A extends Quot<F, A>> extends Algebra<F, A> {

    @Override
    A reciprocal();
}
