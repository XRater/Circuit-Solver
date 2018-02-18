package ru.spbau.mit.circuit.model.circuitObjects;

import ru.spbau.mit.circuit.logic.math.ResultValue;
import ru.spbau.mit.circuit.logic.math.functions.Function;
import ru.spbau.mit.circuit.model.interfaces.CircuitObject;

/**
 * Common class for Elements and Wires.
 * Represents that we can evaluate current and voltage for the object.
 */
abstract public class Item implements CircuitObject {
    private ResultValue current;
    private ResultValue voltage;

    public ResultValue getCurrent() {
        return current;
    }

    public void setCurrent(Function current) {
        this.current = current;
    }

    public ResultValue getVoltage() {
        return voltage;
    }

    public void setVoltage(ResultValue voltage) {
        this.voltage = voltage;
    }
}
