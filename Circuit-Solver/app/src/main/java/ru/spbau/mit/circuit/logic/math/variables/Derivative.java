package ru.spbau.mit.circuit.logic.math.variables;


import android.support.annotation.NonNull;

import ru.spbau.mit.circuit.logic.math.functions.Function;

public class Derivative extends FunctionVariable {

    private final FunctionVariable parent;

    public Derivative(FunctionVariable parent) {
        this.parent = parent;
    }

    public static void main(String[] args) {
    }

    public FunctionVariable parent() {
        return parent;
    }

    /**
     * Sets values as a result of differentiation of the parent function.
     */
    public void setValue() {
        value = parent.value.differentiate();
    }

    /**
     * You may not set derivative value by yourself. Use setValue() method instead.
     */
    @Override
    public void setValue(Function f) {
        throw new UnsupportedOperationException();
    }

    @NonNull
    @Override
    public String toString() {
        return parent.toString() + "'";
    }
}
