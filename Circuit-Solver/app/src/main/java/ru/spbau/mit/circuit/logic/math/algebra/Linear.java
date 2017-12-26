package ru.spbau.mit.circuit.logic.math.algebra;

public interface Linear<C extends Field<C>, T extends Linear<C, T>> {

    T add(T item);

    T multiplyConstant(C cf);
}
