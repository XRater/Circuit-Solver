package ru.spbau.mit.circuit.model.elements;


import java.io.Serializable;

import ru.spbau.mit.circuit.model.node.Node;

public class Wire extends Item implements Serializable {
    private Node from;
    private Node to;

    public static Node findCommon(Wire first, Wire second) {
        if (second.adjacent(first.to)) {
            return first.to;
        }
        if (second.adjacent(first.from)) {
            return first.from;
        }
        return null;
    }

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

    public void replace(Node from, Node to) {
        if (this.from == from) {
            this.to.wires().remove(this);
            this.to = to;
            this.to.wires().add(this);
            return;
        }
        if (this.from == to) {
            this.to.wires().remove(this);
            this.to = from;
            this.to.wires().add(this);
            return;
        }
        if (this.to == from) {
            this.from.wires().remove(this);
            this.from = to;
            this.from.wires().add(this);
            return;
        }
        if (this.to == to) {
            this.from.wires().remove(this);
            this.from = from;
            this.from.wires().add(this);
            return;
        }
        throw new IllegalArgumentException();
    }
}
