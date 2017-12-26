package ru.spbau.mit.circuit.logic.math.variables;


import ru.spbau.mit.circuit.logic.math.functions.Function;

public class Derivative extends FunctionVariable {

    private final FunctionVariable parent;

    public Derivative(FunctionVariable parent) {
        this.parent = parent;
    }

    public FunctionVariable parent() {
        return parent;
    }

    public void setValue() {
        value = parent.value.differentiate();
    }

    @Override
    public void setValue(Function f) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String toString() {
        return parent.toString() + "'";
    }

    public static void main(String[] args) {
    }
}
