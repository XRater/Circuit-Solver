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

    public int index() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public double getVoltage() {
        if (element instanceof Battery) {
            Battery battery = (Battery) element;
            return battery.getVoltage();
        }
        return 0;
    }

    public double getResistance() {
        if (element instanceof Resistor) {
            Resistor resistor = (Resistor) element;
            return resistor.getResistance();
        }
        return 0;
    }

    public void addToTree() {
        inTree = true;
    }

    public boolean isInTree() {
        return inTree;
    }
}
