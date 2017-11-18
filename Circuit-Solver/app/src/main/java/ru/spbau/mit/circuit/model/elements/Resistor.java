package ru.spbau.mit.circuit.model.elements;

import ru.spbau.mit.circuit.model.node.Node;

public abstract class Resistor extends Element {
    private double resistance = 1;

    public Resistor(Node from, Node to) {
        super(from, to);
    }

    public double getResistance() {
        return resistance;
    }

    public void setResistance(double resistance) {
        this.resistance = resistance;
    }
}
