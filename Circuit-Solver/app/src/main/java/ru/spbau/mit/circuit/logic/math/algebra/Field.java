package ru.spbau.mit.circuit.logic.math.algebra;


import org.apache.commons.math3.exception.NullArgumentException;

public interface Field<T extends Field<T>> {

    T add(T f);

    T multiply(T f);

    T negate();

    T reciprocal();

    default T subtract(T a) throws NullArgumentException {
        return add(a.negate());
    }

    default T divide(T f) {
        return multiply(f.reciprocal());
    }

    boolean isZero();

    boolean isIdentity();

    T getZero();

    T getIdentity();
}
