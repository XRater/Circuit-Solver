package ru.spbau.mit.circuit.model.circuitObjects.elements;

import android.support.annotation.NonNull;

import ru.spbau.mit.circuit.logic.math.ResultValue;
import ru.spbau.mit.circuit.model.circuitObjects.nodes.Node;

public abstract class Capacitor extends Element {

    private ResultValue capacity;
    private double initialVoltage;

    protected Capacitor(@NonNull Node from, @NonNull Node to) {
        super(from, to);
    }

    @Override
    public ResultValue getCharacteristicValue() {
        return capacity;
    }

    @Override
    public void setCharacteristicValue(ResultValue capacity) {
        this.capacity = capacity;
    }

    @NonNull
    public void setInitialVoltage(double voltage) {
        initialVoltage = voltage;
    }

    public double getInitialVoltage() {
        return initialVoltage;
    }

    @Override
    public String getCharacteristicName() {
        return "capacity";
    }
}
