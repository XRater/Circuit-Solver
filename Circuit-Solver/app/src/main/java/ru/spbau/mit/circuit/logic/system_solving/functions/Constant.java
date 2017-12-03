package ru.spbau.mit.circuit.logic.system_solving.functions;

public class Constant implements FunctionExpression {

    private double value;

    public Constant(double d) {
        value = d;
    }

    @Override
    public double getValue() {
        return value;
    }
}
