package ru.spbau.mit.circuit.logic.system_solving.variables;


import ru.spbau.mit.circuit.logic.system_solving.functions.FunctionExpression;

abstract public class FunctionVariable implements Variable<FunctionExpression> {

    protected final int id;
    protected final String name;
    protected FunctionExpression function;

    {
        this.id = Numerator.nextId();
    }

    public FunctionVariable() {
        name = "";
    }

    public FunctionVariable(String name) {
        this.name = name;
    }

    @Override
    public FunctionExpression function() {
        return function;
    }

    @Override
    public void setFunction(FunctionExpression f) {
        function = f;
    }

    @Override
    public int id() {
        return id;
    }

    @Override
    public int hashCode() {
        return id;
    }

//    @Override
//    public String toString() {
//        if (name.equals("")) {
//            return "x" + id;
//        }
//        return name;
//    }
}
