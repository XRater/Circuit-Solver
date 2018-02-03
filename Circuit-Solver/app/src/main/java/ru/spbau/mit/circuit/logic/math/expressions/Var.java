package ru.spbau.mit.circuit.logic.math.expressions;


import android.support.annotation.NonNull;

class Var implements Comparable<Var> {

    private final String name;

    Var(String name) {
        this.name = name;
    }

    public String name() {
        return name;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof Var) && name.equals(((Var) obj).name);
    }

    @Override
    public int compareTo(@NonNull Var o) {
        return name.compareTo(o.name);
    }

    @Override
    public String toString() {
        return name;
    }
}
