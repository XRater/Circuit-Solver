package ru.spbau.mit.circuit.logic.math.algebra.dummies;


import android.support.annotation.NonNull;

import ru.spbau.mit.circuit.logic.math.algebra.Numerical;
import ru.spbau.mit.circuit.logic.math.algebra.PolyElement;

public class DummyP extends PolyElement<Numerical, DummyOG, DummyP> {

    private static final DummyP zero = new DummyP();

    DummyP() {
    }

    DummyP(DummyOG og) {
        data.put(og, pair(Numerical.identity(), og));
    }

    @NonNull
    public static DummyP zero() {
        return zero;
    }

    @NonNull
    @Override
    protected DummyP empty() {
        return new DummyP();
    }

    @NonNull
    @Override
    protected DummyP single() {
        return new DummyP(DummyOG.identity());
    }
}
