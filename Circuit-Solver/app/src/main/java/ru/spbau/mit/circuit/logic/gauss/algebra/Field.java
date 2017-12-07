package ru.spbau.mit.circuit.logic.gauss.algebra;


public interface Field<T extends Field<T>> {

    T copy();

    T add(T f);

    T mul(T f);

    T inverse();

    T negate();

    default T div(T f) {
        return mul(f.copy().inverse());
    }

    boolean isZero();

    boolean isIdentity();
}
