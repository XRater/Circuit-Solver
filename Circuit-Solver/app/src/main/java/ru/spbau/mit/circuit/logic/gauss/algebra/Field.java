package ru.spbau.mit.circuit.logic.gauss.algebra;


public interface Field<T extends Field<T>> extends Linear<T, T> {

    T copy();

    T inverse();

    T negate();

    default T div(T f) {
        return mul(f.copy().inverse());
    }

    boolean isZero();

    boolean isIdentity();
}
