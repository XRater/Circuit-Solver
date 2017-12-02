package ru.spbau.mit.circuit.logic.graph;


import ru.spbau.mit.circuit.logic.system_solving.variables.Derivative;
import ru.spbau.mit.circuit.logic.system_solving.variables.Function;
import ru.spbau.mit.circuit.logic.system_solving.variables.Variable;
import ru.spbau.mit.circuit.model.elements.Battery;
import ru.spbau.mit.circuit.model.elements.Element;
import ru.spbau.mit.circuit.model.elements.Resistor;

class Edge {

    private final Variable charge = new Function();
    private final Variable current = new Derivative(charge);
    private final Variable inductive = new Derivative(current);

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
        return "Edge " + String.valueOf(index) + ": (" + from + ", " + to + "): "
                + charge + " : " + current;
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

    public Variable charge() {
        return charge;
    }

    public Variable current() {
        return current;
    }

    public Variable inductive() {
        return inductive;
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
