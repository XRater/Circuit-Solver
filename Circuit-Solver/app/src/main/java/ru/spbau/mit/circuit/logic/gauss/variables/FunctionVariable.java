package ru.spbau.mit.circuit.logic.gauss.variables;


import ru.spbau.mit.circuit.logic.gauss.functions1.Function;

public class FunctionVariable extends Variable<Function> {

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
