package ru.spbau.mit.circuit.logic.system_solving.polynoms;

public interface Vector<T extends Linear<T>> extends Linear<T> {

    double at(int index);

    int size();

}
