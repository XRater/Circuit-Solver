package ru.spbau.mit.circuit.model.elements;

import ru.spbau.mit.circuit.model.node.Node;

public abstract class Capacitor extends Element {
    private float capacity;

    protected Capacitor(Node from, Node to) {
        super(from, to);
    }
}
