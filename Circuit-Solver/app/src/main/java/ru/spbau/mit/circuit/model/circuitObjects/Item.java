package ru.spbau.mit.circuit.model.circuitObjects;

import ru.spbau.mit.circuit.model.Result;
import ru.spbau.mit.circuit.model.interfaces.CircuitObject;

/**
 * Common class for Elements and Wires.
 * Represents that we can evaluate current and voltage for the object.
 */
abstract public class Item implements CircuitObject {
    private Result current;
    private Result voltage;

    public Result getCurrent() {
        return current;
    }

    public void setCurrent(Result current) {
        this.current = current;
    }

    public Result getVoltage() {
        return voltage;
    }

    public void setVoltage(Result voltage) {
        this.voltage = voltage;
    }
}
