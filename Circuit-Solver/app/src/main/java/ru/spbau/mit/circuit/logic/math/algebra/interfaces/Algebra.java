package ru.spbau.mit.circuit.logic.math.algebra.interfaces;


public interface Algebra<F extends Field<F>, A extends Algebra<F, A>>
        extends Field<A>, Linear<F, A> {

    @Override
    default A reciprocal() {
        throw new UnsupportedOperationException();
    }
}
