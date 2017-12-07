package ru.spbau.mit.circuit.logic.gauss.functions1;

@Deprecated
public class Pair<S, T> {

    private final S s;
    private final T t;

    public Pair(S s, T t) {
        this.s = s;
        this.t = t;
    }

    public S first() {
        return s;
    }

    public T second() {
        return t;
    }
}
