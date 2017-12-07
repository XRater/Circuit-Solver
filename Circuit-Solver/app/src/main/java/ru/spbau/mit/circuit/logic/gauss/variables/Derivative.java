package ru.spbau.mit.circuit.logic.gauss.variables;


import ru.spbau.mit.circuit.logic.gauss.functions1.FunctionExpression;

public class Derivative extends FunctionVariable {

    private final FunctionVariable parent;

    public Derivative(FunctionVariable parent) {
        this.parent = parent;
    }

    public FunctionVariable parent() {
        return parent;
    }

    public void setValue() {

//        value = parent.value.differentiate();
        throw new UnsupportedOperationException();
    }

    @Override
    public void setValue(FunctionExpression f) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String toString() {
        return parent.toString() + "'";
    }

}
