package ru.spbau.mit.circuit.logic.math.algebra.dummies;


import ru.spbau.mit.circuit.logic.math.algebra.Numerical;
import ru.spbau.mit.circuit.logic.math.algebra.PolyElement;

public class DummyP extends PolyElement<Numerical, DummyOG, DummyP> {

    private static final DummyP zero = new DummyP();

    public static DummyP zero() {
        return zero;
    }

    DummyP() {
    }

    DummyP(DummyOG og) {
        data.put(og, pair(Numerical.identity(), og));
    }

    @Override
    protected DummyP empty() {
        return new DummyP();
    }

    @Override
    protected DummyP single() {
        return new DummyP(DummyOG.identity());
    }
}
