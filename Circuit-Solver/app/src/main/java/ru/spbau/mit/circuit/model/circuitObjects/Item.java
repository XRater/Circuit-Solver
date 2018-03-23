package ru.spbau.mit.circuit.model.circuitObjects;

import ru.spbau.mit.circuit.logic.math.functions.Function;
import ru.spbau.mit.circuit.model.interfaces.CircuitObject;

/**
 * Common class for Elements and Wires.
 * Represents that we can evaluate current and voltage for the object.
 */
abstract public class Item implements CircuitObject {
    private Function current;
    private double voltage;

    public Function getCurrent() {
        return current;
    }

    public void setCurrent(Function current) {
        this.current = current;
    }

    public double getVoltage() {
        return voltage;
    }

    public void setVoltage(double voltage) {
        this.voltage = voltage;
    }
}
