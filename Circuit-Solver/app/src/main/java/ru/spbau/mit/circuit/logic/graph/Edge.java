package ru.spbau.mit.circuit.logic.graph;


import ru.spbau.mit.circuit.logic.system_solving.variables.Derivative;
import ru.spbau.mit.circuit.logic.system_solving.variables.Function;
import ru.spbau.mit.circuit.logic.system_solving.variables.FunctionVariable;
import ru.spbau.mit.circuit.model.elements.Battery;
import ru.spbau.mit.circuit.model.elements.Item;
import ru.spbau.mit.circuit.model.elements.Resistor;

class Edge {

    private final FunctionVariable charge = new Function();
    private final FunctionVariable current = new Derivative(charge);
    private final FunctionVariable inductive = new Derivative(current);

    private final Item item;
    private final Vertex from;
    private final Vertex to;
    private int index = -1;
    private boolean inTree;

    Edge(Item item, Vertex from, Vertex to) {
        this.item = item;
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

    public FunctionVariable charge() {
        return charge;
    }

    public FunctionVariable current() {
        return current;
    }

    public FunctionVariable inductive() {
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
            return resistor.getResistance();
        }
        return 0;
    }

    public void updateCurrent() {
        item.setCurrent(current.function().getValue());
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
