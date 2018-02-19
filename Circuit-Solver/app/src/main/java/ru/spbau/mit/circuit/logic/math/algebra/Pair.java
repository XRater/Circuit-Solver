package ru.spbau.mit.circuit.logic.math.algebra;


/**
 * Simple pair class. Stores two values of any types.
 */
public class Pair<U, V> {

    public static <U, V> Pair<U, V> pair(U f, V g) {
        return new Pair<>(f, g);
    }

    private final U first;
    private final V second;

    Pair(U u, V v) {
        first = u;
        second = v;
    }

    public U first() {
        return first;
    }

    public V second() {
        return second;
    }

    @Override
    public String toString() {
        return first + "*" + second;
    }
}
