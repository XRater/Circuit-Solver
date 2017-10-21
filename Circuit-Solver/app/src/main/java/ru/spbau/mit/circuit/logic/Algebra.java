package ru.spbau.mit.circuit.logic;


interface Algebra {
    boolean isZero();

    Algebra add(Algebra o);

    Algebra revert();

    Algebra mul(Algebra o);

    Algebra neg();

    Algebra div(Algebra o);

    Algebra sub(Algebra o);
}
