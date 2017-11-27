package ru.spbau.mit.circuit.model.elements;

import ru.spbau.mit.circuit.model.node.Node;

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

    @Override
    public String getCharacteristicName() {
        return "voltage";
    }
}
