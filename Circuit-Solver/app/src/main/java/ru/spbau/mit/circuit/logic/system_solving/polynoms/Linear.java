package ru.spbau.mit.circuit.logic.system_solving.polynoms;


public interface Linear<T extends Linear<T>> {

    void add(T item);

    void mul(double d);

    default void sub(T p) {
        mul(-1);
        add(p);
        mul(-1);
    }

}
