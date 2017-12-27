package ru.spbau.mit.circuit.logic.graph;


import ru.spbau.mit.circuit.logic.math.algebra.Numerical;
import ru.spbau.mit.circuit.logic.math.variables.Derivative;
import ru.spbau.mit.circuit.logic.math.variables.FunctionVariable;
import ru.spbau.mit.circuit.model.elements.Battery;
import ru.spbau.mit.circuit.model.elements.Capacitor;
import ru.spbau.mit.circuit.model.elements.Item;
import ru.spbau.mit.circuit.model.elements.Resistor;

class Edge {

    private final Item item;

    private final Vertex from;
    private final Vertex to;

    private int index = -1; // number of edge in its component.
    private boolean inTree; // is edge in the tree structure

    private final FunctionVariable charge = new FunctionVariable();
    private final Derivative current = new Derivative(charge);
    private final Derivative inductive = new Derivative(current);

    Edge(Item item, Vertex from, Vertex to) {
        this.item = item;
        if (item instanceof Capacitor) {
            charge.setInitialValue(Numerical.number(item.getVoltage()));
        } else {
            charge.setInitialValue(Numerical.zero());
        }
        this.from = from;
        this.to = to;
    }

    public Vertex from() {
        return to;
    }

    public Vertex to() {
        return from;
    }

    int index() {
        return index;
    }

    void setIndex(int index) {
        this.index = index;
    }

    FunctionVariable charge() {
        return charge;
    }

    Derivative current() {
        return current;
    }

    @SuppressWarnings("unused")
    public Derivative inductive() {
        return inductive;
    }

    double getVoltage() {
        if (item instanceof Battery) {
            Battery battery = (Battery) item;
            return battery.getVoltage();
        }
        return 0;
    }

    double getResistance() {
        if (item instanceof Resistor) {
            Resistor resistor = (Resistor) item;
            return resistor.getCharacteristicValue();
        }
        if (item instanceof Capacitor) {
            return 0.01;
        }
        return 0;
    }

    double getCapacity() {
        if (item instanceof Capacitor) {
            Capacitor resistor = (Capacitor) item;
            return resistor.getCharacteristicValue();
        }
        return 0;
    }

    void updateCurrent() {
        item.setCurrent(current.value());
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

    double getDirection(Vertex vertex) {
        if (vertex == from) {
            return -1;
        }
        if (vertex == to) {
            return 1;
        }
        return 0;
    }

    Vertex getAdjacent(Edge e) {
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

    Vertex getPair(Vertex vertex) {
        if (!from.equals(vertex) && !to.equals(vertex)) {
            throw new IllegalArgumentException();
        }
        return from.equals(vertex) ? to : from;
    }

    @Override
    public String toString() {
        return "Edge " + String.valueOf(index) + ": (" + from + ", " + to + "): "
                + charge + " : " + current;
    }
}
