package ru.spbau.mit.circuit.model.circuitObjects.elements;

import android.support.annotation.NonNull;

import ru.spbau.mit.circuit.model.circuitObjects.nodes.Node;

public abstract class Battery extends Element {

    private double voltage = 10;

    public Battery(Node from, Node to) {
        super(from, to);
    }

    @Override
    public double getVoltage() {
        return voltage;
    }

    @Override
    public double getCharacteristicValue() {
        return voltage;
    }

    @Override
    public void setCharacteristicValue(double voltage) {
        this.voltage = voltage;
    }

    @NonNull
    @Override
    public String getCharacteristicName() {
        return "voltage";
    }
}
