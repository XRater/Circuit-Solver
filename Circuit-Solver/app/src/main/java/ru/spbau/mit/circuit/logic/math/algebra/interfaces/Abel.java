package ru.spbau.mit.circuit.logic.math.algebra.interfaces;

public interface Abel<L extends Abel<L>> {

    L add(L item);

    L negate();

    default L subtract(L l) {
        return add(l.negate());
    }

    boolean isZero();

    L getZero();
}
