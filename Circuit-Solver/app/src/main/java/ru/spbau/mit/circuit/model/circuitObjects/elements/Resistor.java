package ru.spbau.mit.circuit.model.circuitObjects.elements;

import ru.spbau.mit.circuit.model.circuitObjects.nodes.Node;

public abstract class Resistor extends Element {
    private double resistance = 1;

    public Resistor(Node from, Node to) {
        super(from, to);
    }

    @SuppressWarnings("WeakerAccess")
    public double getResistance() {
        return resistance;
    }

    @Override
    public double getCharacteristicValue() {
        return resistance;
    }

    @Override
    public void setCharacteristicValue(double resistance) {
        this.resistance = resistance;
    }

    @Override
    public String getCharacteristicName() {
        return "resistance";
    }
}
