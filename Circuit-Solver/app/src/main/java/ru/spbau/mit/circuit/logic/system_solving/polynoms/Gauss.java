package ru.spbau.mit.circuit.logic.system_solving.polynoms;

public interface Gauss<T extends Linear<T>> extends Linear<T> {

    double coefficientAt(int index);

    int size();
}
