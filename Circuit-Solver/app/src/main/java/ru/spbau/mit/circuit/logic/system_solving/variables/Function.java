package ru.spbau.mit.circuit.logic.system_solving.variables;


import ru.spbau.mit.circuit.logic.system_solving.functions.Constant;
import ru.spbau.mit.circuit.logic.system_solving.functions.FunctionExpression;

public class Function extends FunctionVariable {

    @Override
    public String toString() {
        if (name.equals("")) {
            return "x" + id;
        }
        return name;
    }
}
