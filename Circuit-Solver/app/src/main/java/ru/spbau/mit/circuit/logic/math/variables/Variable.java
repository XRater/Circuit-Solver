package ru.spbau.mit.circuit.logic.math.variables;


import android.support.annotation.NonNull;

/**
 * Class to work with variables.
 *
 * @param <T> type of variable value.
 */
abstract public class Variable<T> implements Comparable<Variable<T>> {

    protected final int id;
    protected final String name;
    protected T value;

    {
        this.id = Numerator.nextId();
    }

    @SuppressWarnings("WeakerAccess")
    protected Variable() {
        name = "";
    }

    @SuppressWarnings("WeakerAccess")
    protected Variable(String name) {
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

    @Override
    public String toString() {
        if (name.equals("")) {
            return "x" + id;
        }
        return name;
    }
}
