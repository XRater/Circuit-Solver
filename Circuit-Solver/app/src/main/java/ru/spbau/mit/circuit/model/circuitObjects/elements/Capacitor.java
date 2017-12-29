package ru.spbau.mit.circuit.model.circuitObjects.elements;

import ru.spbau.mit.circuit.model.circuitObjects.nodes.Node;

public abstract class Capacitor extends Element {
    private double capacity = 1;

    protected Capacitor(Node from, Node to) {
        super(from, to);
    }

    @Override
    public double getCharacteristicValue() {
        return capacity;
    }

    @Override
    public void setCharacteristicValue(double capacity) {
        this.capacity = capacity;
    }

    @Override
    public String getCharacteristicName() {
        return "capacity";
    }
}
