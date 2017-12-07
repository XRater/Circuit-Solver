package ru.spbau.mit.circuit.model.elements;

import ru.spbau.mit.circuit.logic.gauss.functions1.FunctionExpression;
import ru.spbau.mit.circuit.model.interfaces.CircuitObject;

abstract public class Item implements CircuitObject {
    private FunctionExpression current; // TODO It should be a value.
    private double voltage;

    public FunctionExpression getCurrent() {
        return current;
    }

    public void setCurrent(FunctionExpression current) {
        this.current = current;
    }

    public double getVoltage() {
        return voltage;
    }

    public void setVoltage(double voltage) {
        this.voltage = voltage;
    }
}
