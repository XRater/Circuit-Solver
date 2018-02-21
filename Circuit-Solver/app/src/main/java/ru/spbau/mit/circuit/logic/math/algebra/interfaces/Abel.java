package ru.spbau.mit.circuit.logic.math.algebra.interfaces;

import android.support.annotation.NonNull;

/**
 * Abelian group over sum operation.
 */
public interface Abel<L extends Abel<L>> {

    L add(L item);

    L negate();

    default L subtract(@NonNull L l) {
        return add(l.negate());
    }

    boolean isZero();

    L getZero();
}
