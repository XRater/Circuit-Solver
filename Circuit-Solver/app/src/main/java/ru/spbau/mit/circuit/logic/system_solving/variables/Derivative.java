package ru.spbau.mit.circuit.logic.system_solving.variables;


import ru.spbau.mit.circuit.logic.system_solving.FunctionExpression;

public class Derivative implements Variable {

    private final int id;
    private final Variable parent;
    private FunctionExpression functionExpression;

    public Derivative(Variable parent) {
        id = Numerator.nextId();
        this.parent = parent;
    }

    @Override
    public int id() {
        return id;
    }

    @Override
    public void setFunction(FunctionExpression f) {
        functionExpression = f;
    }

    @Override
    public FunctionExpression function() {
        return functionExpression;
    }

    @Override
    public String toString() {
        return parent.toString() + "'";
    }
}
