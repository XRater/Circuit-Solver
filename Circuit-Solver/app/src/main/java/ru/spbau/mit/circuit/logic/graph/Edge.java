package ru.spbau.mit.circuit.logic.graph;


import ru.spbau.mit.circuit.model.Battery;
import ru.spbau.mit.circuit.model.Element;
import ru.spbau.mit.circuit.model.Resistor;

class Edge {

    private final Element element;
    private final Node from;
    private final Node to;
    private int index = -1;
    private boolean inTree;

    Edge(Element element, Node from, Node to) {
        this.element = element;
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
        if (element instanceof Battery) {
            Battery battery = (Battery) element;
            return battery.getVoltage();
        }
        return 0;
    }

    double getResistance() {
        if (element instanceof Resistor) {
            Resistor resistor = (Resistor) element;
            return resistor.getResistance();
        }
        return 0;
    }

    void setCurrent(double current) {
        element.setCurrent(current);
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

    boolean adjacent(Edge e) {
        return e.to.equals(to) || e.to.equals(from) ||
                e.from.equals(to) || e.from.equals(from);
    }

    Node getPair(Node node) {
        if (!from.equals(node) && !to.equals(node)) {
            throw new IllegalArgumentException();
        }
        return from.equals(node) ? to : from;
    }
}
