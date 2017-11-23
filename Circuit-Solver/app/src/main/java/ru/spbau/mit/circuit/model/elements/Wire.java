package ru.spbau.mit.circuit.model.elements;


import ru.spbau.mit.circuit.model.node.Node;

public class Wire extends Item {
    private Node from;
    private Node to;

    public Wire(Node from, Node to) {
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
}
