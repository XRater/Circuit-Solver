package ru.spbau.mit.circuit.logic.system_solving.functions;

import android.support.annotation.NonNull;

interface PrimaryFunction extends Comparable<PrimaryFunction> {

    int rank();

    int compare(PrimaryFunction o);

    @Override
    default int compareTo(@NonNull PrimaryFunction o) {
        if (rank() != o.rank()) {
            return rank() - o.rank();
        } else {
            return compare(o);
        }
    }
}
