package ru.spbau.mit.circuit.logic.gauss.variables;


import android.support.annotation.NonNull;

abstract public class Variable<T> implements Comparable<Variable<T>> {

    protected final int id;
    protected final String name;
    protected T value;

    {
        this.id = Numerator.nextId();
    }

    public Variable() {
        name = "";
    }

    public Variable(String name) {
        this.name = name;
    }

    public T value() {
        return value;
    }

    public void setValue(T f) {
        value = f;
    }

    public int id() {
        return id;
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public int compareTo(@NonNull Variable o) {
        return id - o.id;
    }
}
