package ru.spbau.mit.circuit.model.elements;


import java.io.Serializable;

import ru.spbau.mit.circuit.model.node.Node;

public class Wire extends Item implements Serializable {
    private final Node from;
    private final Node to;

    public Wire(Node from, Node to) throws IllegalWireException {
        if (from == to) {
            throw new IllegalWireException("From and to nodes were equal");
        }
        this.from = from;
        this.to = to;
    }

    public Node from() {
        return from;
    }

    public Node to() {
        return to;
    }

    public Node opposite(Node node) {
        return node == from ? to : from;
    }

    @Override
    public String toString() {
        return "Wire: " + this.from + ":" + this.to;
    }

    public boolean adjacent(Node node) {
        return node == from || node == to;
    }
}
