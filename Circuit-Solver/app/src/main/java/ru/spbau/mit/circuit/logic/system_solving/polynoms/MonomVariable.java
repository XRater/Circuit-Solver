package ru.spbau.mit.circuit.logic.system_solving.polynoms;


import ru.spbau.mit.circuit.logic.system_solving.variables.FunctionVariable;

public class MonomVariable extends Monom<FunctionVariable> {

    public MonomVariable(FunctionVariable value) {
        super(value);
    }

    public MonomVariable(FunctionVariable value, double coefficient) {
        super(value, coefficient);
    }

    public String getStringValue() {
        return Math.abs(coefficient) + value.toString();
    }
}
