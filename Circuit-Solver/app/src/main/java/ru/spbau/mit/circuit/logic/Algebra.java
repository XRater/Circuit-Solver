package ru.spbau.mit.circuit.logic;

@Deprecated
interface Algebra {
    boolean isZero();

    Algebra add(Algebra o);

    Algebra inverse();

    Algebra mul(Algebra o);

    Algebra neg();

    Algebra div(Algebra o);

    Algebra sub(Algebra o);
}
