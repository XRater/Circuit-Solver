package ru.spbau.mit.circuit.logic.system_solving.variables;


import ru.spbau.mit.circuit.logic.system_solving.FunctionExpression;

public class Function implements Variable {

    private final int id;
    private final String name;
    private FunctionExpression functionExpression;

    {
        this.id = Numerator.nextId();
    }

    public Function() {
        name = "";
    }

    public Function(String name) {
        this.name = name;
    }

    @Override
    public FunctionExpression function() {
        return functionExpression;
    }

    @Override
    public void setFunction(FunctionExpression f) {

    }

    @Override
    public int id() {
        return id;
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public String toString() {
        if (name.equals("")) {
            return "x" + id;
        }
        return name;
    }
}
