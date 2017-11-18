package ru.spbau.mit.circuit.model.elements;

import ru.spbau.mit.circuit.model.interfaces.CircuitObject;

abstract public class Item implements CircuitObject {
    private double current; // TODO It should be a function.
    private double voltage;

    public double getCurrent() {
        return current;
    }

    public void setCurrent(double current) {
        this.current = current;
    }

    public double getVoltage() {
        return voltage;
    }

    public void setVoltage(double voltage) {
        this.voltage = voltage;
    }
}
