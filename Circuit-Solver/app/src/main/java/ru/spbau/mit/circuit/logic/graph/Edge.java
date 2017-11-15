package ru.spbau.mit.circuit.logic.graph;


import ru.spbau.mit.circuit.model.elements.Battery;
import ru.spbau.mit.circuit.model.elements.CircuitItem;
import ru.spbau.mit.circuit.model.elements.Resistor;

class Edge {

    private final CircuitItem circuitItem;
    private final Node from;
    private final Node to;
    private int index = -1;
    private boolean inTree;

    Edge(CircuitItem circuitItem, Node from, Node to) {
        this.circuitItem = circuitItem;
        this.from = from;
        this.to = to;
    }

    @Override
    public String toString() {
        return "Edge " + String.valueOf(index) + ": (" + from + ", " + to + ")";
    }

    public Node from() {
        return to;
    }

    public Node to() {
        return from;
    }

    int index() {
        return index;
    }

    void setIndex(int index) {
        this.index = index;
    }

    double getVoltage() {
        if (circuitItem instanceof Battery) {
            Battery battery = (Battery) circuitItem;
            return battery.getVoltage();
        }
        return 0;
    }

    double getResistance() {
        if (circuitItem instanceof Resistor) {
            Resistor resistor = (Resistor) circuitItem;
            return resistor.getResistance();
        }
        return 0;
    }

    void setCurrent(double current) {
        circuitItem.setCurrent(current);
    }

    void addToTree() {
        inTree = true;
    }

    void removeFromTree() {
        inTree = false;
    }

    boolean isInTree() {
        return inTree;
    }

    double getDirection(Node node) {
        if (node == from) {
            return -1;
        }
        if (node == to) {
            return 1;
        }
        return 0;
    }

    Node getAdjacent(Edge e) {
        if (to.equals(e.to) || to.equals(e.from)) {
            return to;
        }
        if (from.equals(e.from) || from.equals(e.to)) {
            return from;
        }
        return null;
    }

    boolean adjacent(Edge e) {
        return getAdjacent(e) != null;
    }

    Node getPair(Node node) {
        if (!from.equals(node) && !to.equals(node)) {
            throw new IllegalArgumentException();
        }
        return from.equals(node) ? to : from;
    }
}
