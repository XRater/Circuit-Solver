package ru.spbau.mit.circuit.logic.system_solving.variables;

public class Derivative extends FunctionVariable {

    private final FunctionVariable parent;

    public Derivative(FunctionVariable parent) {
        this.parent = parent;
    }

    public FunctionVariable parent() {
        return parent;
    }

    @Override
    public String toString() {
        return parent.toString() + "'";
    }
}
