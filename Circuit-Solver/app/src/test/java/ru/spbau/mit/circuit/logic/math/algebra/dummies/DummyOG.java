package ru.spbau.mit.circuit.logic.math.algebra.dummies;


import android.support.annotation.NonNull;

import ru.spbau.mit.circuit.logic.math.algebra.interfaces.OrderedGroup;

public class DummyOG implements OrderedGroup<DummyOG> {

    private static final DummyOG identity = new DummyOG();
    final int ind;

    private DummyOG() {
        ind = 0;
    }

    private DummyOG(int i) {
        ind = i;
    }

    @NonNull
    public static DummyOG identity() {
        return identity;
    }

    public static DummyOG element(int n) {
        return new DummyOG(n);
    }

    @Override
    public int compareTo(@NonNull DummyOG o) {
        return ind - o.ind;
    }

    @NonNull
    @Override
    public DummyOG multiply(@NonNull DummyOG dummyOG) {
        return new DummyOG(ind + dummyOG.ind);
    }

    @NonNull
    @Override
    public DummyOG reciprocal() {
        return new DummyOG(-ind);
    }

    @NonNull
    @Override
    public DummyOG getIdentity() {
        return identity();
    }

    @NonNull
    @Override
    public String toString() {
        return String.valueOf(ind);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof DummyOG) {
            return ind == ((DummyOG) obj).ind;
        }
        return false;
    }
}
