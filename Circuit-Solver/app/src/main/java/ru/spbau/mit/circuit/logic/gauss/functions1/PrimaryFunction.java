package ru.spbau.mit.circuit.logic.gauss.functions1;

import android.support.annotation.NonNull;

import ru.spbau.mit.circuit.logic.gauss.algebra.Field;
import ru.spbau.mit.circuit.logic.gauss.algebra.Numerical;

interface PrimaryFunction extends Comparable<PrimaryFunction>, Field<PrimaryFunction> {

    int rank();

    int compare(PrimaryFunction o);

    PrimaryFunction mul(Numerical num);

//    Pair<PrimaryFunction, Double> integrate();

//    Pair<PrimaryFunction, Double> differentiate();

    @Override
    default int compareTo(@NonNull PrimaryFunction o) {
        if (rank() != o.rank()) {
            return rank() - o.rank();
        } else {
            return compare(o);
        }
    }

    @Override
    default boolean isIdentity() {
        throw new UnsupportedOperationException();
    }
}
