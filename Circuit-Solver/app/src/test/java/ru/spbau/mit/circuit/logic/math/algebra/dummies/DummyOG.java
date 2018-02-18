package ru.spbau.mit.circuit.logic.math.algebra.dummies;


import android.support.annotation.NonNull;

import ru.spbau.mit.circuit.logic.math.algebra.interfaces.OrderedGroup;

public class DummyOG implements OrderedGroup<DummyOG> {

    private static final DummyOG identity = new DummyOG();

    public static DummyOG identity() {
        return identity;
    }

    public static DummyOG element(int n) {
        return new DummyOG(n);
    }

    final int ind;

    private DummyOG() {
        ind = 0;
    }

    private DummyOG(int i) {
        ind = i;
    }

    @Override
    public int compareTo(@NonNull DummyOG o) {
        return ind - o.ind;
    }

    @Override
    public DummyOG multiply(DummyOG dummyOG) {
        return new DummyOG(ind + dummyOG.ind);
    }

    @Override
    public DummyOG reciprocal() {
        return new DummyOG(-ind);
    }

    @Override
    public DummyOG getIdentity() {
        return identity();
    }

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
