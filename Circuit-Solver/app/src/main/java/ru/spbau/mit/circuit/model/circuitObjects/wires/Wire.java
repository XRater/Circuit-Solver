package ru.spbau.mit.circuit.model.circuitObjects.wires;


import java.io.Serializable;

import ru.spbau.mit.circuit.model.circuitObjects.Item;
import ru.spbau.mit.circuit.model.circuitObjects.exceptions.IllegalWireException;
import ru.spbau.mit.circuit.model.circuitObjects.nodes.Node;

public abstract class Wire extends Item implements Serializable {
    private Node from;
    private Node to;

    public Wire(Node from, Node to) throws IllegalWireException {
        if (from == to) {
            throw new IllegalWireException("From and to nodes were equal");
        }
        this.from = from;
        this.to = to;
    }

    // some getters
    public Node from() {
        return from;
    }

    public Node to() {
        return to;
    }

    // method for convenient work.
    public Node opposite(Node node) {
        return node == from ? to : from;
    }

    public boolean adjacent(Node node) {
        return node == from || node == to;
    }

    public static Node findCommon(Wire first, Wire second) {
        if (second.adjacent(first.to)) {
            return first.to;
        }
        if (second.adjacent(first.from)) {
            return first.from;
        }
        return null;
    }

    /**
     * The method changes ends of wire, so they match arguments.
     *
     * @param from end to leave unchanged.
     * @param to   new end
     */
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

    @Override
    public String toString() {
        return "Wire: " + this.from + ":" + this.to;
    }
}
