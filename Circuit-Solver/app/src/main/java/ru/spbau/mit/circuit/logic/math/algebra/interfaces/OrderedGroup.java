package ru.spbau.mit.circuit.logic.math.algebra.interfaces;

public interface OrderedGroup<G extends OrderedGroup<G>>
        extends Group<G>, Comparable<G> {

    @Override
    default boolean isIdentity() {
        return this.compareTo(getIdentity()) == 0;
    }
}
