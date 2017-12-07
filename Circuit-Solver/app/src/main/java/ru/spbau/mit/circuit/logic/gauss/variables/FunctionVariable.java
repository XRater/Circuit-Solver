package ru.spbau.mit.circuit.logic.gauss.variables;


import ru.spbau.mit.circuit.logic.gauss.functions1.FunctionExpression;

public class FunctionVariable extends Variable<FunctionExpression> {

    public FunctionVariable() {
        super();
    }

    public FunctionVariable(String name) {
        super(name);
    }

    @Override
    public String toString() {
        if (name.equals("")) {
            return "x" + id;
        }
        return name;
    }
}
