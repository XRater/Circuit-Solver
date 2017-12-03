package ru.spbau.mit.circuit.logic.system_solving.variables;


import android.support.annotation.NonNull;

public interface Variable<T> extends Comparable<Variable<T>> {

    T function();

    int id();

    @Override
    default int compareTo(@NonNull Variable<T> o) {
        return id() - o.id();
    }

    void setFunction(T f);
}
